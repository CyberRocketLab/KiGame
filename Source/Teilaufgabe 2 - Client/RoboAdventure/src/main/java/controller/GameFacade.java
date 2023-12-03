package controller;

import converter.ClientConverter;
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
import java.util.List;

public class GameFacade {
    private static final Logger logger = LoggerFactory.getLogger(GameFacade.class);
    private static final int ALLOWED_GAME_ACTIONS = 160;
    private NetworkCommunication networkCommunication;
    private Game game;
    private GameMapGenerator mapGenerator;
    private BusinessLogicInterface businessLogic;
    private BusinessRules serverBusinessRules;
    private GameView gameView;
    private int gameActions = 0;

    public GameFacade(URL serverBaseURL, GameID gameID, ClientData clientData) {
        this.networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData, new ClientConverter(), new ServerConverter());
        this.game = new Game();
        this.gameView = new GameView();
        this.game.addPropertyChangeListener(gameView);
        this.businessLogic = new BalancedTerrainDistributionLogic();
        this.mapGenerator = new GameMapGenerator(businessLogic);
        this.serverBusinessRules = new ServerBusinessRules();
    }

    public void startGame() {
        registerClient();
        sendClientMap();
        game.updateGameState(networkCommunication.getGameState());
    }

    public void playGame() {
        Node startPosition = null;
        boolean startIsSet = false;
        boolean treasureFoundOnce = false;
        boolean burgFoundOnce = false;

        while (gameActions < ALLOWED_GAME_ACTIONS) {
            if (game.getClientState() == ClientState.Lost || game.getClientState() == ClientState.Won) {
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
        }
    }

    private void registerClient() {
        networkCommunication.registerClient();
    }

    private void sendClientMap() {
        List<Field> map = generateHalfMap();
        networkCommunication.sendClientMap(map);
    }

    private List<Field> generateHalfMap() {
        List<Field> randomMap = mapGenerator.generateRandomMap();

        while (!validateMap(randomMap)) {
            randomMap = mapGenerator.generateRandomMap();
        }

        return randomMap;
    }

    private boolean validateMap(List<Field> randomMap) {
        MapValidator mapValidator = new MapValidator(serverBusinessRules);
        return mapValidator.validateMap(randomMap);
    }

    private void sendMoveToServer(EMoves move) {
        networkCommunication.sendMove(move);
        logger.debug("Move was send");
        game.updateGameState(networkCommunication.getGameState());
    }

    private void updateGamePlayState() {
        game.updateGameState(networkCommunication.getGameState());
    }

    public ClientState getGameState() {
        return game.getClientState();
    }
}
