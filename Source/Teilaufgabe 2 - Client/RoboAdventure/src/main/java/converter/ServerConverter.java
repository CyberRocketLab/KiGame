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
import move.Move;

import java.util.ArrayList;
import java.util.List;

public class ServerConverter implements IServerConverter{

    @Override
    public List<PlayerHalfMapNode> convertToPlayerHalfMapNode(List<Field> clientMap) {
        List<PlayerHalfMapNode> clientMapToSend = new ArrayList<>();

        for (Field field : clientMap) {
            boolean fortPresent = false;
            ETerrain terrain = null;

            if (field.getFortState() == FortState.MyFort) {
                fortPresent = true;
            }

            terrain = switch (field.getTerrain()) {
                case GRASS -> ETerrain.Grass;
                case WATER -> ETerrain.Water;
                case MOUNTAIN -> ETerrain.Mountain;
            };

            clientMapToSend.add(new PlayerHalfMapNode(field.getPositionX(), field.getPositionY(), fortPresent, terrain));

        }

        return clientMapToSend;
    }

    @Override
    public EMove convertToEMove(EMoves move) {
        EMove moveToSend = null;

        switch (move) {
            case Up -> moveToSend = EMove.Up;
            case Down -> moveToSend = EMove.Down;
            case Left -> moveToSend = EMove.Left;
            case Right -> moveToSend = EMove.Right;
        }

        return moveToSend;
    }
}
