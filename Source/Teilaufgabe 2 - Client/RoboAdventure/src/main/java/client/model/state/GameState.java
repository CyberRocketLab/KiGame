package client.model.state;

import client.model.data.Field;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private ClientState clientState = ClientState.MustWait;
    private List<Field> updatedMap = new ArrayList<>();
    boolean collectedTreasure = false;

    public void addMap(List<Field> serverMap) {
        updatedMap = serverMap;
    }

    public void addClientState(ClientState clientState, boolean hasCollectedTreasure) {
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
