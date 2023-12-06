package client.converter;

import client.model.data.Field;
import client.move.EMoves;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

import java.util.List;

public interface IServerConverter {
    List<PlayerHalfMapNode> convertToPlayerHalfMapNode(List<Field> clientMap);
    EMove convertToEMove(EMoves move);
}
