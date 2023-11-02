package model.state;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import model.data.Field;
import model.data.Terrain;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;

import java.util.ArrayList;
import java.util.List;

public class GamePlayState {
    private ClientState clientState = ClientState.MustWait;
    private List<Field> updatedMap = new ArrayList<>();

    public GamePlayState() {
    }

    public void addFieldToMap(int posX,
                              int posY,
                              ETerrain eTerrain,
                              EPlayerPositionState ePlayerPositionState,
                              ETreasureState eTreasureStatetreasureState,
                              EFortState eFortStatefortState)
    {
        Terrain terrain = null;
        PlayerPositionState playerPositionState = null;
        TreasureState treasureState = null;
        FortState fortState = null;

        switch (eTerrain) {
            case Grass -> terrain = Terrain.GRASS;
            case Water -> terrain = Terrain.WATER;
            case Mountain -> terrain = Terrain.MOUNTAIN;
        }

        switch (ePlayerPositionState) {
            case NoPlayerPresent -> playerPositionState = PlayerPositionState.NOBODY;
            case MyPlayerPosition -> playerPositionState = PlayerPositionState.ME;
            case EnemyPlayerPosition -> playerPositionState = PlayerPositionState.ENEMY;
            case BothPlayerPosition -> playerPositionState = PlayerPositionState.BOTH;
        }

        switch (eTreasureStatetreasureState) {
            case MyTreasureIsPresent -> treasureState = TreasureState.GoalTreasure;
            case NoOrUnknownTreasureState -> treasureState = TreasureState.UnknownTreasure;
        }

        switch (eFortStatefortState) {
            case MyFortPresent -> fortState = FortState.MyFort;
            case EnemyFortPresent -> fortState = FortState.EnemyFort;
            case NoOrUnknownFortState -> fortState = FortState.UnknownFort;
        }

        updatedMap.add(new Field(posX, posY, terrain, playerPositionState, treasureState, fortState));

    }

    public void addClientState(EPlayerGameState ePlayerGameState) {
        clientState = switch (ePlayerGameState) {
            case Won -> ClientState.Won;
            case Lost -> ClientState.Lost;
            case MustAct -> ClientState.MustAct;
            case MustWait -> ClientState.MustWait;
        };
    }

    public ClientState getClientState() {
        return clientState;
    }

    public List<Field> getUpdatedMap() {
        return updatedMap;
    }
}
