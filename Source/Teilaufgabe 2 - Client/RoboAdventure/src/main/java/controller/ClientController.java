package controller;

import model.data.ClientData;
import model.data.Field;
import model.data.GameMap;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.state.GamePlayState;
import model.validator.MapValidator;
import move.EMoves;
import move.Move;
import move.NextFieldToCheck;
import move.Node;
import network.NetworkCommunication;
import view.GameStateView;

import java.util.ArrayList;
import java.util.List;

public class ClientController {
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


        boolean isPlaying = true;
        while (isPlaying) {

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
            Node nextFieldToCheck = nextFieldFinder.getNextFieldToCheck();

            List<EMoves> movesToTarget = move.getMovesToTargetField(nextFieldToCheck);

            for(EMoves moves : movesToTarget) {
                sendMoveToServer(moves);

                gamePlayState = getGamePlayState();
                gamePlay.updateMap(gamePlayState.getUpdatedMap());



            }



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
