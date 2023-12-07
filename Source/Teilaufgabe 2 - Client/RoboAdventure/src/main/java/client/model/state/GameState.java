package client.model.state;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.generator.GameMapGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static final Logger logger = LoggerFactory.getLogger(GameState.class);
    private ClientState clientState = ClientState.MustWait;
    private List<Field> updatedMap = new ArrayList<>();
    boolean collectedTreasure = false;

    public void addMap(List<Field> serverMap) {
        logger.info("Adding Server Map to Client GameState");
        if (serverMap == null) {
            logger.error("Provided List<Field> serverMap was null");
            throw new NullOrEmptyParameterException();
        }
        updatedMap = serverMap;
    }

    public void addClientState(ClientState clientState, boolean hasCollectedTreasure) {
        logger.info("Adding ClientState to GameState");
        if (clientState == null) {
            logger.error("Provided ClientState was null");
            throw new NullOrEmptyParameterException();
        }

        this.clientState = clientState;
        this.collectedTreasure = hasCollectedTreasure;
    }

    public ClientState getClientState() {
        return clientState;
    }

    public List<Field> getUpdatedMap() {
        return updatedMap;
    }

    public boolean isCollectedTreasure() {
        return collectedTreasure;
    }
}
