package controller;

import model.data.Field;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.validator.MapValidator;
import move.EMoves;
import move.Move;
import move.NextFieldFinder;
import move.Node;
import network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.GameView;

import java.util.List;

public class GameFacade {
    private static final Logger logger = LoggerFactory.getLogger(GameFacade.class);
    private final int ALLOWED_GAME_ACTIONS;
    private final NetworkCommunication networkCommunication;
    private final Game game;
    private final GameView gameView;
    private final GameMapGenerator mapGenerator;
    private final MapValidator mapValidator;
    private int gameActions = 0;

    public GameFacade(int ALLOWED_GAME_ACTIONS,
                      NetworkCommunication networkCommunication,
                      Game game,
                      GameView gameView,
                      GameMapGenerator mapGenerator,
                      MapValidator mapValidator) {
        this.ALLOWED_GAME_ACTIONS = ALLOWED_GAME_ACTIONS;
        this.networkCommunication = networkCommunication;
        this.game = game;
        this.gameView = gameView;
        game.addPropertyChangeListener(gameView);
        this.mapGenerator = mapGenerator;
        this.mapValidator = mapValidator;
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
        return mapValidator.validateMap(randomMap);
    }

    private void sendMoveToServer(EMoves move) {
        networkCommunication.sendMove(move);
        logger.debug("Move was send");
        game.updateGameState(networkCommunication.getGameState());
    }

    public ClientState getGameState() {
        return game.getClientState();
    }
}
