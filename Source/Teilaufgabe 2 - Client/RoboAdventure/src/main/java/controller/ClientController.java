package controller;

import model.data.ClientData;
import model.data.Field;
import model.generator.GameMapGenerator;
import model.validator.MapValidator;
import network.NetworkCommunication;

import java.util.List;

public class ClientController {
    ClientData clientData;
    GamePlay gamePlay;
    NetworkCommunication networkCommunication;

    public ClientController(String serverBaseURL, String gameID, ClientData clientData) {
        this.clientData = clientData;
        networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData);
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


}
