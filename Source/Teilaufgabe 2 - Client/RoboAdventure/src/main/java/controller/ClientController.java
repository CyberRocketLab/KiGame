package controller;

import model.data.ClientData;
import model.data.Field;
import model.data.GameMap;
import model.generator.GameMapGenerator;
import model.state.GamePlayState;
import model.validator.MapValidator;
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

        // Setting Full Game map
        gamePlay.updateMap(getGamePlayState().getUpdatedMap());

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


}
