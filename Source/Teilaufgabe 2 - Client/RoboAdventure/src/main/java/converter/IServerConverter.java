package converter;

import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import model.data.Field;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;
import move.EMoves;

import java.util.List;

public interface IServerConverter {
    List<PlayerHalfMapNode> convertToPlayerHalfMapNode(List<Field> clientMap);
    EMove convertToEMove(EMoves move);
}
