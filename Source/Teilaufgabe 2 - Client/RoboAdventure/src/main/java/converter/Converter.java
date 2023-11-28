package converter;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

public interface Converter {
    Terrain getConvertedTerrain(ETerrain eTerrain);
    PlayerPositionState getConvertedPlayerPositionState(EPlayerPositionState ePlayerPositionState);
    TreasureState getConvertedTreasureState(ETreasureState eTreasureStatetreasureState);
    FortState getConvertedFortState(EFortState eFortStatefortState);
}
