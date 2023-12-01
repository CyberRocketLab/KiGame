package move;

import exceptions.MoveException;
import model.data.Field;
import model.state.PlayerPositionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

        assertThrows(MoveException.class, () -> new Move(map));
    }

    @Test
    public void givenNullMap_whenPassingToMoveConstructor_thenError() {
        assertThrows(MoveException.class, () -> new Move(null));
    }

    @Test
    public void givenNullTargetNode_whenPassingToSetTargetNode_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(MoveException.class, () -> move.setMovesToTargetField(null));
    }

    @Test
    public void givenNullTargetNode_whenPassingToSetMovesToTargetField_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(MoveException.class, () -> move.setMovesToTargetField(null));
    }

    @Test
    public void givenNullCurrentTerrain_whenPassingToCalculateMoveCost_thenError() {
        Field field = Mockito.mock(Field.class);
        Field myPositionField = Mockito.mock(Field.class);
        when(field.getPlayerPositionState()).thenReturn(PlayerPositionState.ME);

        List<Field> map = Arrays.asList(field, myPositionField);

        Move move = new Move(map);

        assertThrows(MoveException.class, () -> move.calculateMoveCost(null, null));
    }



}