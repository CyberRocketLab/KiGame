package client.controller;

import client.exceptions.GameFacadeException;
import client.exceptions.Notification;
import client.model.state.ClientState;
import client.move.EMoves;
import client.move.Move;
import client.network.NetworkCommunication;
import client.model.data.Field;
import client.model.generator.GameMapGenerator;
import client.model.validator.MapValidator;
import client.move.NextFieldFinder;
import client.move.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.view.GameView;

import java.util.List;

public class GameFacade {
    private static final Logger logger = LoggerFactory.getLogger(GameFacade.class);
    private final int ALLOWED_GAME_ACTIONS;
    private final NetworkCommunication networkCommunication;
    private final Game game;
    private final GameView gameView;
    private final GameMapGenerator mapGenerator;
    private final MapValidator mapValidator;
    private final TurnManager turnManager;
    private int gameActions = 0;

    public GameFacade(int ALLOWED_GAME_ACTIONS,
                      NetworkCommunication networkCommunication,
                      Game game,
                      GameView gameView,
                      GameMapGenerator mapGenerator,
                      MapValidator mapValidator,
                      TurnManager turnManager) {

        Notification notification = new Notification();

        validateParameters(
                ALLOWED_GAME_ACTIONS,
                networkCommunication,
                game,
                gameView,
                mapGenerator,
                mapValidator,
                turnManager,
                notification);

        if (notification.hasErrors()) {
            logger.error("Parameter validation errors: {}", notification.getErrors());
            throw new GameFacadeException(notification.getErrors());
        }

        this.ALLOWED_GAME_ACTIONS = ALLOWED_GAME_ACTIONS;
        this.networkCommunication = networkCommunication;
        this.game = game;
        this.gameView = gameView;
        game.addPropertyChangeListener(gameView);
        this.mapGenerator = mapGenerator;
        this.mapValidator = mapValidator;
        this.turnManager = turnManager;
    }


    public void startGame() {
        logger.info("Starting Game");
        registerClient();
        sendClientMap();
        game.updateGameState(networkCommunication.getGameState());
    }

    public void playGame() {
        logger.info("Starting playing Game");
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
        turnManager.waitForMyTurn();

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
        return mapValidator.validateMap(randomMap);
    }

    private void sendMoveToServer(EMoves move) {
        turnManager.waitForMyTurn();

        networkCommunication.sendMove(move);
        logger.debug("Move:{} was send successfully", move);

        game.updateGameState(networkCommunication.getGameState());
    }

    public ClientState getGameState() {
        return game.getClientState();
    }

    private void validateParameters(int ALLOWED_GAME_ACTIONS,
                                    NetworkCommunication networkCommunication,
                                    Game game,
                                    GameView gameView,
                                    GameMapGenerator mapGenerator,
                                    MapValidator mapValidator,
                                    TurnManager turnManager,
                                    Notification notification) {

        if (ALLOWED_GAME_ACTIONS <= 0)
            notification.addError("ALLOWED_GAME_ACTIONS cannot be less than zero");
        if (networkCommunication == null)
            notification.addError("NetworkCommunication cannot be null");
        if (game == null)
            notification.addError("Game cannot be null");
        if (gameView == null)
            notification.addError("GameView cannot be null");
        if (mapGenerator == null)
            notification.addError("GameMapGenerator cannot be null");
        if (mapValidator == null)
            notification.addError("MapValidator cannot be null");
        if (turnManager == null)
            notification.addError("MapValidator cannot be null");

    }
}
