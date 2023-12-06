package client.converter;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.state.FortState;
import client.move.EMoves;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ServerConverter implements IServerConverter {
    private static final Logger logger = LoggerFactory.getLogger(ClientConverter.class);

    @Override
    public List<PlayerHalfMapNode> convertToPlayerHalfMapNode(List<Field> clientMap) {
        if (clientMap == null) {
            logger.error("Provided ClientMap was null");
            throw new NullOrEmptyParameterException();
        }

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
        if (move == null) {
            logger.error("Provided EMove was null");
            throw new NullOrEmptyParameterException();
        }

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
