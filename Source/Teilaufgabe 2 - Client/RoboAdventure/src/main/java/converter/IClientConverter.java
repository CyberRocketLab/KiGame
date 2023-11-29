package converter;


import messagesbase.messagesfromserver.*;
import model.data.Field;
import model.state.ClientState;


import java.util.List;

public interface IClientConverter {
    List<Field> getConvertedMap(FullMap serverFullMap);
    ClientState getConvertedClientState(EPlayerGameState ePlayerGameState);

}
