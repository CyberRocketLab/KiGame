package client.converter;


import client.model.data.Field;
import client.model.state.ClientState;
import messagesbase.messagesfromserver.*;


import java.util.List;

public interface IClientConverter {
    List<Field> getConvertedMap(FullMap serverFullMap);
    ClientState getConvertedClientState(EPlayerGameState ePlayerGameState);

}
