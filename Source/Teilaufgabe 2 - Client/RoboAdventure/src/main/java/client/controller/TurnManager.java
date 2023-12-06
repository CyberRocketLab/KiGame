package client.controller;

import client.model.state.ClientState;
import client.model.state.GameState;
import client.network.NetworkCommunication;

public class TurnManager {

    NetworkCommunication networkCommunication;

    public TurnManager(NetworkCommunication networkCommunication) {
        this.networkCommunication = networkCommunication;
    }

    public void waitForMyTurn() {
        ClientState state = networkCommunication.getGameState().getClientState();
        while (state != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (state == ClientState.Lost || state == ClientState.Won) {
                break;
            }

            state = networkCommunication.getGameState().getClientState();
        }
    }
}
