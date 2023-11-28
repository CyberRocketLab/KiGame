package model.state;

import converter.Converter;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import model.data.Field;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private ClientState clientState = ClientState.MustWait;
    private List<Field> updatedMap = new ArrayList<>();
    boolean collectedTreasure = false;

    private final Converter converterForClient;

    public GameState(Converter converter) {
        this.converterForClient = converter;
    }

    public void addFieldToMap(int posX,
                              int posY,
                              ETerrain eTerrain,
                              EPlayerPositionState ePlayerPositionState,
                              ETreasureState eTreasureStatetreasureState,
                              EFortState eFortStatefortState)
    {
       Field field = new Field(
               posX,
               posY,
               converterForClient.getConvertedTerrain(eTerrain),
               converterForClient.getConvertedPlayerPositionState(ePlayerPositionState),
               converterForClient.getConvertedTreasureState(eTreasureStatetreasureState),
               converterForClient.getConvertedFortState(eFortStatefortState)
       );


        updatedMap.add(field);

    }

    public void addClientState(EPlayerGameState ePlayerGameState, boolean hasCollectedTreasure) {
        clientState = switch (ePlayerGameState) {
            case Won -> ClientState.Won;
            case Lost -> ClientState.Lost;
            case MustAct -> ClientState.MustAct;
            case MustWait -> ClientState.MustWait;
        };

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
