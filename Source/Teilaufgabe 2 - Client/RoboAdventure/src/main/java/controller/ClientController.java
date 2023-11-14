package controller;

import model.data.ClientData;
import model.data.Field;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.state.GamePlayState;
import model.state.MoveStrategy;
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

import java.util.*;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    GameState gamePlay = new GameState();
    GameStateView gameStateView;
    NetworkCommunication networkCommunication;

    public ClientController(String serverBaseURL, String gameID, ClientData clientData) {
        networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData);
        gameStateView = new GameStateView();
        gamePlay.addPropertyChangeListener(gameStateView);
    }

    public synchronized void play() {
        // Starting & Registering Game
        registerClient();
        sendClientMap();


        Queue<strategy.MoveStrategy> moveStrategies = new LinkedList<>();
        moveStrategies.add(new RightCornerDown());
        moveStrategies.add(new RightCornerUp());
        moveStrategies.add(new LeftCornerDown());
        moveStrategies.add(new LeftCornerUp());

        Queue<strategy.MoveStrategy> findBurgStrategy = new LinkedList<>();
        findBurgStrategy.add(new RightCornerDown());
        findBurgStrategy.add(new RightCornerUp());
        findBurgStrategy.add(new LeftCornerDown());
        findBurgStrategy.add(new LeftCornerUp());


        GamePlayState gamePlayState = getGamePlayState();
        gamePlay.updateMap(gamePlayState.getUpdatedMap());
      //  Move move = new Move(gamePlay.getMap().getMap()); // TODO: Fix this shit
       // NextFieldToCheck nextFieldFinder = new NextFieldToCheck(gamePlay.getMap(), move.getNodeList());

        Node startPosition = null;
        boolean startIsSet = false;

        boolean treasureFoundOnce = false;
        boolean burgFoundOnce = false;

        boolean isPlaying = true;
        while (isPlaying) {
            logger.debug("Entering While loop");
            // Receiving the latest updates from Server
        //    GamePlayState gamePlayState = getGamePlayState();

            // Stop game if lost
            if(gamePlayState.getClientState() == ClientState.Lost || gamePlayState.getClientState() == ClientState.Won) {
                isPlaying = false;
                logger.debug("Game: {}", gamePlayState.getClientState());
                continue;
            }

            Move move = new Move(gamePlay.getMap().getMap()); // TODO: Fix this shit

            if(!startIsSet) {
                startPosition = move.getPlayerPosition();
                startIsSet = true;
            }

            NextFieldToCheck nextFieldFinder = new NextFieldToCheck(gamePlay.getMap(), move.getNodeList(), startPosition);

        /*    // Updating GameMap
            gamePlay.updateMap(gamePlayState.getUpdatedMap());

            // Setting Move object with PlayerPosition as source Node
            Move move = new Move(gamePlay.getMap().getMap()); // TODO: Fix this shit

            // Getting the nextField to check
            NextFieldToCheck nextFieldFinder = new NextFieldToCheck(gamePlay.getMap(), move.getNodeList());*/


            if(moveStrategies.isEmpty()) {
               /* moveStrategies.add(new RightCornerDown());
                moveStrategies.add(new RightCornerUp());
                moveStrategies.add(new LeftCornerDown());
                moveStrategies.add(new LeftCornerUp());*/

                moveStrategies.add(new CheckUnvisitedFields());
                logger.debug("Main Strategy is done. Moving to Plan B strategy!");
            }

            if(findBurgStrategy.isEmpty()) {
                findBurgStrategy.add(new CheckUnvisitedFields());
                logger.debug("Main Strategy is done. Moving to Plan B strategy!");
            }

            boolean isTreasureCollected = gamePlayState.isCollectedTreasure();
           // Node nextFieldToCheck = nextFieldFinder.getNextFieldToCheck(moveStrategies.poll(), isTreasureCollected);
            Node nextFieldToCheck;

            if (gamePlay.isTreasureFound() && !isTreasureCollected) {
                logger.debug("Treasure has been found but not collected, setting next move towards the treasure");
                Node treasureNode = move.findNode(gamePlay.getTreasureField());

                logger.debug("Position of Treasure {}{}.", treasureNode.field.getPositionX(), treasureNode.field.getPositionY());
                move.setMovesToTargetField(treasureNode);

            } else if (isTreasureCollected && gamePlay.isFortFound()) {
                logger.debug("Treasure is collected and Fort has been foung!");
                Node fortNode = move.findNode(gamePlay.getFortField());

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


            // Setting Moves to Target
           // move.setMovesToTargetField(nextFieldToCheck);
            List<EMoves> movesToTarget = move.getMoves();


            while(gamePlayState.getClientState() != ClientState.Lost || gamePlayState.getClientState() != ClientState.Won) {
                if(movesToTarget.isEmpty()) {
                    logger.debug("The List with moves is empty");
                    break;
                }

                if(!treasureFoundOnce) {
                    if (gamePlay.isTreasureFound()) {
                        treasureFoundOnce = true;
                        break;
                    }
                }

                if (!burgFoundOnce) {
                    if(gamePlay.isFortFound()) {
                        burgFoundOnce = true;
                        break;
                    }
                }

                sendMoveToServer(movesToTarget.remove(0));
                gamePlayState = getGamePlayState();

                gamePlay.updateMap(gamePlayState.getUpdatedMap());
            }

            logger.debug("End of WhileLoop!");
            logger.debug("Size of moveStrategies:{}", moveStrategies.size());

        }


    }

    public void registerClient() {
        networkCommunication.registerClient();
    }

    public List<Field> generateHalfMap(){
        int maxRows = 5;
        int maxColumns = 10;

        GameMapGenerator gameMap = new GameMapGenerator();
        gameMap.generateRandomMap(maxRows, maxColumns);

        MapValidator mapValidator = new MapValidator(maxRows, maxColumns);

        while (!mapValidator.validateMap(gameMap.getMap())) {
            gameMap.generateRandomMap(maxRows, maxColumns);
        }

        return gameMap.getMap();
    }

    public  void sendClientMap() {
        List<Field> map = generateHalfMap();
        networkCommunication.sendClientMap(map);
    }

    public GamePlayState getGamePlayState() {
        return networkCommunication.getGameState();
    }

    public void sendMoveToServer(EMoves move) {
        networkCommunication.sendMove(move);
    }


}
