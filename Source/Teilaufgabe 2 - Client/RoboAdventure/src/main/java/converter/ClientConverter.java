package converter;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;


public class ClientConverter implements Converter{

    @Override
    public Terrain getConvertedTerrain(ETerrain eTerrain) {
        Terrain terrain = null;

        switch (eTerrain) {
            case Grass -> terrain = Terrain.GRASS;
            case Water -> terrain = Terrain.WATER;
            case Mountain -> terrain = Terrain.MOUNTAIN;
        }

        return terrain;
    }

    @Override
    public PlayerPositionState getConvertedPlayerPositionState(EPlayerPositionState ePlayerPositionState) {
        PlayerPositionState playerPositionState = null;


        switch (ePlayerPositionState) {
            case NoPlayerPresent -> playerPositionState = PlayerPositionState.NOBODY;
            case MyPlayerPosition -> playerPositionState = PlayerPositionState.ME;
            case EnemyPlayerPosition -> playerPositionState = PlayerPositionState.ENEMY;
            case BothPlayerPosition -> playerPositionState = PlayerPositionState.BOTH;
        }

        return playerPositionState;
    }

    @Override
    public TreasureState getConvertedTreasureState(ETreasureState eTreasureStatetreasureState) {
        TreasureState treasureState = null;

        switch (eTreasureStatetreasureState) {
            case MyTreasureIsPresent -> treasureState = TreasureState.GoalTreasure;
            case NoOrUnknownTreasureState -> treasureState = TreasureState.UnknownTreasure;
        }

        return treasureState;
    }

    @Override
    public FortState getConvertedFortState(EFortState eFortStatefortState) {
        FortState fortState = null;

        switch (eFortStatefortState) {
            case MyFortPresent -> fortState = FortState.MyFort;
            case EnemyFortPresent -> fortState = FortState.EnemyFort;
            case NoOrUnknownFortState -> fortState = FortState.UnknownFort;
        }

        return fortState;
    }
}
