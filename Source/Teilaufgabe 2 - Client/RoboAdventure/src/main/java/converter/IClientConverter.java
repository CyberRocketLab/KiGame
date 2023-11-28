package converter;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.*;
import model.data.Field;
import model.data.Terrain;
import model.state.ClientState;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

import java.util.List;

public interface IClientConverter {
    List<Field> getConvertedMap(FullMap serverFullMap);
    ClientState getConvertedClientState(EPlayerGameState ePlayerGameState);

}
