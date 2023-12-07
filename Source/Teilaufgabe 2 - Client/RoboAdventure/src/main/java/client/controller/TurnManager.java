package client.controller;

import client.exceptions.NullOrEmptyParameterException;
import client.model.state.ClientState;
import client.network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurnManager {

    private static final Logger logger = LoggerFactory.getLogger(TurnManager.class);
    NetworkCommunication networkCommunication;

    public TurnManager(NetworkCommunication networkCommunication) {
        if (networkCommunication == null) {
            logger.error("Provided NetworkCommunication was null");
            throw new NullOrEmptyParameterException();
        }

        this.networkCommunication = networkCommunication;
    }

    public void waitForMyTurn() {
        logger.info("Waiting for KI turn");
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
