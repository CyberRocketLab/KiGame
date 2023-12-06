package client.controller;

import client.model.data.ClientData;
import client.model.data.GameID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    private URL serverBaseURL;
    private GameID gameID;
    private ClientData clientData;

    @BeforeEach
    void init() {
        serverBaseURL = Mockito.mock(URL.class);
        gameID = Mockito.mock(GameID.class);
        clientData = Mockito.mock(ClientData.class);
    }

    @Test
    public void initClient_shouldWork() {
        assertDoesNotThrow(() -> {
            ClientController controller = new ClientController(serverBaseURL, gameID, clientData);
        });
    }

}