package client.move;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.state.PlayerPositionState;
import client.move.Move;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    public void givenEmptyMap_whenPassingToMoveConstructor_thenError() {
        List<Field> map = new ArrayList<>();

        assertThrows(NullOrEmptyParameterException.class, () -> new Move(map));
    }

    @Test
    public void givenNullMap_whenPassingToMoveConstructor_thenError() {
        assertThrows(NullOrEmptyParameterException.class, () -> new Move(null));
    }

    @Test
    public void givenNullTargetNode_whenPassingToSetTargetNode_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(NullOrEmptyParameterException.class, () -> move.setMovesToTargetField(null));
    }

    @Test
    public void givenNullTargetNode_whenPassingToSetMovesToTargetField_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(NullOrEmptyParameterException.class, () -> move.setMovesToTargetField(null));
    }

    @Test
    public void givenNullCurrentTerrain_whenPassingToCalculateMoveCost_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(NullOrEmptyParameterException.class, () -> move.calculateMoveCost(null, null));
    }



}