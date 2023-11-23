package controller;

import model.data.ClientData;
import model.data.Field;
import model.data.GameID;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.state.GamePlayState;
import model.validator.MapValidator;
import move.EMoves;
import move.Move;
import move.NextFieldToCheck;
import move.Node;
import network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.*;
import view.GameStateView;

import java.net.URL;
import java.util.*;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    GameState game = new GameState();
    GameStateView gameView;
    NetworkCommunication networkCommunication;

    public ClientController(URL serverBaseURL, GameID gameID, ClientData clientData) {
        networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData);
        gameView = new GameStateView();
        game.addPropertyChangeListener(gameView);
    }

    public synchronized void play() {
        registerClient();
        sendClientMap();

        Queue<strategy.MoveStrategy> moveStrategies = setStrategies();
        Queue<strategy.MoveStrategy> findBurgStrategy = setStrategies();

        GamePlayState gamePlayState = getGamePlayState();
        game.updateMap(gamePlayState.getUpdatedMap());

        Node startPosition = null;
        boolean startIsSet = false;

        boolean treasureFoundOnce = false;
        boolean burgFoundOnce = false;

        boolean isPlaying = true;
        while (isPlaying) {
            logger.debug("Entering While loop");

            // Stop game if LOST or WON
            if(gamePlayState.getClientState() == ClientState.Lost || gamePlayState.getClientState() == ClientState.Won) {
                isPlaying = false;
                logger.debug("Game: {}", gamePlayState.getClientState());
                continue;
            }

            Move move = new Move(game.getListOfFields());

            if(!startIsSet) {
                startPosition = move.getPlayerPosition();
                startIsSet = true;
            }

            NextFieldToCheck nextFieldFinder = new NextFieldToCheck(game.getMap(), move.getNodeList(), startPosition);


            if(moveStrategies.isEmpty()) {
                moveStrategies.add(new CheckUnvisitedFields());
                logger.debug("Main Strategy is done. Moving to Plan B strategy!");
            }

            if(findBurgStrategy.isEmpty()) {
                findBurgStrategy.add(new CheckUnvisitedFields());
                logger.debug("Main Strategy is done. Moving to Plan B strategy!");
            }

            boolean isTreasureCollected = gamePlayState.isCollectedTreasure();
            Node nextFieldToCheck;

            if (game.isTreasureFound() && !isTreasureCollected) {
                logger.debug("Treasure has been found but not collected, setting next move towards the treasure");
                Node treasureNode = move.findNode(game.getTreasureField());

                logger.debug("Position of Treasure {}{}.", treasureNode.field.getPositionX(), treasureNode.field.getPositionY());
                move.setMovesToTargetField(treasureNode);

            } else if (isTreasureCollected && game.isFortFound()) {
                logger.debug("Treasure is collected and Fort has been foung!");
                Node fortNode = move.findNode(game.getFortField());

                logger.debug("Position of Treasure {}{}.", fortNode.field.getPositionX(), fortNode.field.getPositionY());
                move.setMovesToTargetField(fortNode);

            } else if(isTreasureCollected)  {
                logger.debug("Treasure is collected: {}", true);
                nextFieldToCheck = nextFieldFinder.getNextFieldToCheck(findBurgStrategy.poll(), isTreasureCollected);
                move.setMovesToTargetField(nextFieldToCheck);

            } else {
                logger.debug("Treasure is collected: {}", false);
                nextFieldToCheck = nextFieldFinder.getNextFieldToCheck(moveStrategies.poll(), isTreasureCollected);
                move.setMovesToTargetField(nextFieldToCheck);
            }

            List<EMoves> movesToTarget = move.getMoves();


            while(gamePlayState.getClientState() != ClientState.Lost || gamePlayState.getClientState() != ClientState.Won) {
                if(movesToTarget.isEmpty()) {
                    logger.debug("The List with moves is empty");
                    break;
                }

                if(!treasureFoundOnce) {
                    if (game.isTreasureFound()) {
                        treasureFoundOnce = true;
                        break;
                    }
                }

                if (!burgFoundOnce) {
                    if(game.isFortFound()) {
                        burgFoundOnce = true;
                        break;
                    }
                }

                sendMoveToServer(movesToTarget.remove(0));
                gamePlayState = getGamePlayState();

                game.updateMap(gamePlayState.getUpdatedMap());
            }

            logger.debug("End of WhileLoop!");
            logger.debug("Size of moveStrategies:{}", moveStrategies.size());

        }


    }

    private void registerClient() {
        networkCommunication.registerClient();
    }

    private List<Field> generateHalfMap(){
        List<Field> randomMap = GameMapGenerator.generateRandomMap();

        while (!validateMap(randomMap)) {
            randomMap = GameMapGenerator.generateRandomMap();
        }

        return randomMap;
    }

    private Queue<MoveStrategy> setStrategies() {
        Queue<MoveStrategy> moveStrategies = new LinkedList<>();
        moveStrategies.add(new RightCornerDown());
        moveStrategies.add(new RightCornerUp());
        moveStrategies.add(new LeftCornerDown());
        moveStrategies.add(new LeftCornerUp());

        return moveStrategies;
    }

    private boolean validateMap(List<Field> randomMap) {
        MapValidator mapValidator = new MapValidator();
        return mapValidator.validateMap(randomMap);
    }

    private   void sendClientMap() {
        List<Field> map = generateHalfMap();
        networkCommunication.sendClientMap(map);
    }

    private GamePlayState getGamePlayState() {
        return networkCommunication.getGameState();
    }

    private void sendMoveToServer(EMoves move) {
        networkCommunication.sendMove(move);
    }


}
