package controller;

import converter.ClientConverter;
import converter.IClientConverter;
import converter.IServerConverter;
import converter.ServerConverter;
import model.data.ClientData;
import model.data.Field;
import model.data.GameID;
import model.generator.BalancedTerrainDistributionLogic;
import model.generator.BusinessLogicInterface;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.validator.BusinessRules;
import model.validator.MapValidator;
import model.validator.ServerBusinessRules;
import move.EMoves;
import move.Move;
import move.NextFieldFinder;
import move.Node;
import network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.GameView;

import java.net.URL;
import java.util.*;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private static final int ALLOWED_GAME_ACTIONS = 160;
    Game game;
    GameView gameView;
    NetworkCommunication networkCommunication;
    private int gameActions = 0;

    public ClientController(URL serverBaseURL, GameID gameID, ClientData clientData) {
        IClientConverter clientConverter = new ClientConverter();
        IServerConverter serverConverter = new ServerConverter();

        networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData, clientConverter, serverConverter);
        game = new Game();
        gameView = new GameView();
        game.addPropertyChangeListener(gameView);
    }

    public synchronized void play() {
        initializeGame();
        updateGamePlayState();

        Node startPosition = null;
        boolean startIsSet = false;
        boolean treasureFoundOnce = false;
        boolean burgFoundOnce = false;

        while (gameActions < ALLOWED_GAME_ACTIONS) {
            logger.debug("Entering While loop");

            // Stop game if LOST or WON
            if (game.getClientState() == ClientState.Lost || game.getClientState() == ClientState.Won) {
                logger.debug("Game: {}", game.getClientState());
                break;
            }

            Move move = new Move(game.getListOfFields());
            if (!startIsSet) {
                startPosition = move.getPlayerPosition();
                startIsSet = true;
            }

            NextFieldFinder nextFieldFinder = new NextFieldFinder(game.getMap(), move.getNodeList(), startPosition);
            GameRoundHandler gameRoundHandler = new GameRoundHandler(game, move, nextFieldFinder);

            gameRoundHandler.handleNextRound();
            List<EMoves> movesToTarget = move.getMoves();

            logger.debug("CLIENT STATE BEFORE LOOP {}", game.getClientState());
            while (game.getClientState() != ClientState.Lost && game.getClientState() != ClientState.Won) {
                if (movesToTarget.isEmpty()) {
                    logger.debug("The List with moves is empty");
                    break;
                } else if (!treasureFoundOnce) {
                    if (game.isTreasureFound()) {
                        treasureFoundOnce = true;
                        break;
                    }
                } else if (!burgFoundOnce) {
                    if (game.isFortFound()) {
                        burgFoundOnce = true;
                        break;
                    }
                }

                sendMoveToServer(movesToTarget.remove(0));
                updateGamePlayState();

                if (++gameActions == ALLOWED_GAME_ACTIONS) {
                    break;
                }

                logger.info("Current Actions: {}", gameActions);
            }

            logger.debug("End of WhileLoop!");
        }

        if (gameActions == ALLOWED_GAME_ACTIONS) {
            logger.info("To much actions move: {}", gameActions);
            System.out.println("Game: " + game.getClientState());
        }

    }

    private void registerClient() {
        networkCommunication.registerClient();
    }

    private List<Field> generateHalfMap() {
        BusinessLogicInterface businessLogic = new BalancedTerrainDistributionLogic();
        GameMapGenerator mapGenerator = new GameMapGenerator(businessLogic);

        List<Field> randomMap = mapGenerator.generateRandomMap();

        while (!validateMap(randomMap)) {
            randomMap = mapGenerator.generateRandomMap();
        }

        return randomMap;
    }

    private void initializeGame() {
        registerClient();
        sendClientMap();
    }

    private boolean validateMap(List<Field> randomMap) {
        BusinessRules businessRules = new ServerBusinessRules();
        MapValidator mapValidator = new MapValidator(businessRules);
        return mapValidator.validateMap(randomMap);
    }

    private void sendClientMap() {
        List<Field> map = generateHalfMap();
        networkCommunication.sendClientMap(map);
    }

    private void updateGamePlayState() {
        game.updateGameState(networkCommunication.getGameState());
    }

    private void sendMoveToServer(EMoves move) {
        networkCommunication.sendMove(move);
    }


}
