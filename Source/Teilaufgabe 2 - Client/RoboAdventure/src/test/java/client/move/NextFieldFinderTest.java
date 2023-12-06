package client.move;

import client.exceptions.NoFoundException;
import client.model.data.Field;
import client.model.data.GameMap;
import client.move.NextFieldFinder;
import client.move.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import client.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NextFieldFinderTest {

    private GameMap gameMap;
    private Node startPosition;
    private List<Node> nodeList = new ArrayList<>();
    private NextFieldFinder nextFieldFinder;

    @BeforeEach
    void init() {
        gameMap = Mockito.mock(GameMap.class);
        Field field = Mockito.mock(Field.class);
        startPosition = Mockito.mock(Node.class);
        Node node = Mockito.mock(Node.class);

        for (int i = 0; i < 50 ; ++i) {
            nodeList.add(node);
        }

        int RECTANGLE_EDGE_X = 19;
        int RECTANGLE_EDGE_Y = 4;

        when(gameMap.getEdgeOfX()).thenReturn(RECTANGLE_EDGE_X);
        when(gameMap.getEdgeOfY()).thenReturn(RECTANGLE_EDGE_Y);


        int randomPosition = 3;
        when(startPosition.getField()).thenReturn(field);
        when(startPosition.getField().getPositionX()).thenReturn(randomPosition);
        when(startPosition.getField().getPositionY()).thenReturn(randomPosition);

        nextFieldFinder = new NextFieldFinder(gameMap, nodeList, startPosition);
    }

    @Test
    public void givenValidaParameters_whenCallingGetNextFieldToCheck_thenAssertWorks () throws NoFoundException {
        MoveStrategy moveStrategy = Mockito.mock(MoveStrategy.class);
        Node node = Mockito.mock(Node.class);
        when(moveStrategy.getFieldWithPosition(any(), any(), eq(nodeList))).thenReturn(node);

        assertDoesNotThrow(() -> {
            nextFieldFinder.getNextFieldToCheck(moveStrategy, true);
        });
    }

    @Test
    public void givenNodeThatCannotBeFound_whenCallingGetNextFieldToCheck_thenNoFoundException () throws NoFoundException {
        MoveStrategy moveStrategy = Mockito.mock(MoveStrategy.class);
        Node node = Mockito.mock(Node.class);
        when(moveStrategy.getFieldWithPosition(any(), any(), eq(nodeList))).thenThrow(new NoFoundException());

        assertThrows(NoFoundException.class, () -> {
            nextFieldFinder.getNextFieldToCheck(moveStrategy, true);
        });
    }
}