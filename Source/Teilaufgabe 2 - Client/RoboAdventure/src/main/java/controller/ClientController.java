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


        Queue<MoveStrategy> moveStrategies = new LinkedList<>(
                Arrays.asList(
                    MoveStrategy.RightCornerDown,
                    MoveStrategy.RightCornerUp,
                    MoveStrategy.LeftCornerDown,
                    MoveStrategy.CheckUnvisitedGrass
                )
        );


        boolean isPlaying = true;
        while (isPlaying) {
            logger.debug("Entering While loop");
            // Receiving the latest updates from Server
            GamePlayState gamePlayState = getGamePlayState();

            // Stop game if lost
            if(gamePlayState.getClientState() == ClientState.Lost) {
                isPlaying = false;
                continue;
            }

            // Updating GameMap
            gamePlay.updateMap(gamePlayState.getUpdatedMap());

            // Setting Move object with PlayerPosition as source Node
            Move move = new Move(gamePlay.getMap().getMap()); // TODO: Fix this shit

            // Getting the nextField to check
            NextFieldToCheck nextFieldFinder = new NextFieldToCheck(gamePlay.getMap(), move.getNodeList());

            // Polling after each iteration a Strategy
            Node nextFieldToCheck =
                    moveStrategies.isEmpty() ?
                    nextFieldFinder.getNextFieldToCheck(MoveStrategy.CheckUnvisitedGrass) :
                    nextFieldFinder.getNextFieldToCheck(moveStrategies.poll());

            // Setting Moves to Targer
            move.setMovesToTargetField(nextFieldToCheck);
            List<EMoves> movesToTarget = move.getMoves();


            // Loop till Player has not collected the Treasure
            if (gamePlayState.isCollectedTreasure())
                logger.debug("Treasure is Collected");

            while(!gamePlayState.isCollectedTreasure()) {
                if(movesToTarget.isEmpty()) {
                    logger.debug("The List with moves is empty");
                    break;
                }

                sendMoveToServer(movesToTarget.remove(0));

                gamePlayState = getGamePlayState();
                gamePlay.updateMap(gamePlayState.getUpdatedMap());

                if (gamePlay.isTreasureFound()) {
                    logger.debug("TREASURE HAS BEEN FOUND");
                    Node treasureNode = move.findNode(gamePlay.getTreasureField());

                    logger.debug("Position of Treasure {}{}.", treasureNode.field.getPositionX(), treasureNode.field.getPositionY());
                    move.setMovesToTargetField(treasureNode);
                    movesToTarget = move.getMoves();
                }

            }
            // this method while ends because treassure was found
            // after treasure was found player was not going to it

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
