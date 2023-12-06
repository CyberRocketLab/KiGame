package client.controller;

import client.exceptions.NoFoundException;
import client.model.data.Field;
import client.move.Move;
import client.move.NextFieldFinder;
import client.move.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameRoundHandlerTest {

    private GameRoundHandler gameRoundHandler;
    private Game game;
    private Move move;
    private NextFieldFinder nextFieldFinder;
    private Node node;

    @BeforeEach
    void init() {
        game = Mockito.mock(Game.class);
        move = Mockito.mock(Move.class);
        nextFieldFinder = Mockito.mock(NextFieldFinder.class);
        node = mock(Node.class);
        gameRoundHandler = new GameRoundHandler(game, move, nextFieldFinder);
    }

    @Test
    public void treasureFoundButNotCollected_whenCalling_shouldWork() {
        when(game.isTreasureFound()).thenReturn(true);
        when(game.isCollectedTreasure()).thenReturn(false);
        when(move.findNode(any())).thenReturn(node);

        Field field = Mockito.mock(Field.class);
        when(node.getField()).thenReturn(field);
        when(field.getPositionX()).thenReturn(1);
        when(field.getPositionY()).thenReturn(1);

        gameRoundHandler.handleNextRound();

        verify(move, times(1)).setMovesToTargetField(node);
    }

    @Test
    public void treasureFoundAndFortFound_whenCalling_shouldWork() {
        when(game.isFortFound()).thenReturn(true);
        when(game.isCollectedTreasure()).thenReturn(true);
        when(move.findNode(any())).thenReturn(node);

        Field field = Mockito.mock(Field.class);
        when(node.getField()).thenReturn(field);
        when(field.getPositionX()).thenReturn(1);
        when(field.getPositionY()).thenReturn(1);

        gameRoundHandler.handleNextRound();

        verify(move, times(1)).setMovesToTargetField(node);
    }

    @Test
    public void givenTreasureNotFound_whenNoFoundingNextField_shouldThrowException() {
        when(game.isTreasureFound()).thenReturn(false);
        when(game.isCollectedTreasure()).thenReturn(false);
        when(move.findNode(any())).thenReturn(node);


        when(nextFieldFinder.getNextFieldToCheck(any(), anyBoolean())).thenThrow(new NoFoundException());

        assertThrows(NoFoundException.class, () -> {
            gameRoundHandler.handleNextRound();
        });
    }

}