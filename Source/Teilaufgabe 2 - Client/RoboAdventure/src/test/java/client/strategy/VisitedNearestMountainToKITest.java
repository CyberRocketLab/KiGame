package client.strategy;

import client.exceptions.NoFoundException;
import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.data.Terrain;
import client.model.state.FortState;
import client.model.state.PlayerPositionState;
import client.model.state.TreasureState;
import client.move.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VisitedNearestMountainToKITest {

    @Test
    void listWithMountain_whenCalling_thenThrowException() {
        VisitedNearestMountainToKI strategy = new VisitedNearestMountainToKI();
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(new Node(new Field(
                0,
                0,
                Terrain.GRASS,
                PlayerPositionState.NOBODY,
                TreasureState.UnknownTreasure,
                FortState.UnknownFort)));

        assertThrows(NoFoundException.class, () ->
                strategy.getFieldWithPosition(new StartAndEndOfAxis(0, 9), new StartAndEndOfAxis(0, 4), nodeList));
    }

    @Test
    void givenNotValidaData_whenCalling_thenTrowsException() {
        VisitedNearestMountainToKI strategy = new VisitedNearestMountainToKI();

        assertThrows(NullOrEmptyParameterException.class, () -> {
            strategy.getFieldWithPosition(null,null,null);
        });
    }

}