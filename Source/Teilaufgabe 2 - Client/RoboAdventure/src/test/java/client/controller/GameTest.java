package client.controller;

import client.model.data.Field;
import client.model.state.FortState;
import client.model.state.GameState;
import client.model.state.TreasureState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


class GameTest {
    private Game game;
    private GameState mockGameState;
    private PropertyChangeListener propertyChangeListener;

    @BeforeEach
    public void init() {
        game = new Game();
        mockGameState = Mockito.mock(GameState.class);
        propertyChangeListener = Mockito.mock(PropertyChangeListener.class);

        game.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    public void givenUpdatedMapWithTreasure_whenCalling_thenFinding() {
        Field field = Mockito.mock(Field.class);
        Field fieldWithTreasure = Mockito.mock(Field.class);
        Mockito.when(fieldWithTreasure.getTreasureState()).thenReturn(TreasureState.GoalTreasure);

        List<Field> fieldList = Arrays.asList(field, field, field, fieldWithTreasure);

        game.updateMap(fieldList);
        assertTrue(game.isTreasureFound());
    }

    @Test
    public void givenUpdatedMapWithFort_whenCalling_thenFinding() {
        Field field = Mockito.mock(Field.class);
        Field fieldWithFort = Mockito.mock(Field.class);
        Mockito.when(fieldWithFort.getFortState()).thenReturn(FortState.EnemyFort);

        List<Field> fieldList = Arrays.asList(field, field, field, fieldWithFort);

        game.updateMap(fieldList);
        assertTrue(game.isFortFound());
    }

}