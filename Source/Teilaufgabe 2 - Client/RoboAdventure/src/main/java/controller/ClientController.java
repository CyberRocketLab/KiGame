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
    }

    public void registerClient() {
        networkCommunication.registerClient();
    }

}
