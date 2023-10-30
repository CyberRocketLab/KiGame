package controller;

import model.ClientData;
import network.NetworkCommunication;

public class ClientController {
    ClientData clientData;
    GamePlay gamePlay;
    NetworkCommunication networkCommunication;

    public ClientController(String serverBaseURL, String gameID, ClientData clientData) {
        this.clientData = clientData;
        networkCommunication = new NetworkCommunication(serverBaseURL, gameID, clientData);

        gamePlay = new GamePlay();
        gamePlay.generateClientHalfMap();
    }

    public void registerClient() {
        networkCommunication.registerClient();
    }

    public void sendClientMap() {networkCommunication.sendClientMap(gamePlay.getClientHalfMap());}

}
