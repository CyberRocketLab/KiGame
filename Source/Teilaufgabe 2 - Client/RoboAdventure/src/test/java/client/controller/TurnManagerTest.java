package client.controller;

import client.model.state.ClientState;
import client.model.state.GameState;
import client.network.NetworkCommunication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {

    NetworkCommunication networkCommunication;
    TurnManager turnManager;

    @BeforeEach
    public void init() {
        networkCommunication = Mockito.mock(NetworkCommunication.class);
        GameState gameState = Mockito.mock(GameState.class);
        Mockito.when(gameState.getClientState()).thenReturn(ClientState.MustAct);
        Mockito.when(networkCommunication.getGameState()).thenReturn(gameState);


        turnManager = new TurnManager(networkCommunication);
    }

    @Test
    public void playerStateIsMustAct_whenCalling_shouldWork() {
        assertDoesNotThrow(() -> turnManager.waitForMyTurn());
    }

}