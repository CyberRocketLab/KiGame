package client.view;

import client.model.data.Field;
import client.model.data.GameMap;
import client.model.data.Terrain;
import client.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameViewTest {
    private GameView gameView;
    private GameMap gameMap;
    private Field field;

    @BeforeEach
    public void init() {
        gameView = new GameView();
        gameMap = Mockito.mock(GameMap.class);
        field = Mockito.mock(Field.class);
    }


    @Test
    public void givenMapWithGrass_whenPassingToUpdateMap_thenPrintingTheMap() {
        when(field.getTerrain()).thenReturn(Terrain.GRASS);
        int RECTANGLE_EDGE_X = 19;
        when(gameMap.getEdgeOfX()).thenReturn(RECTANGLE_EDGE_X);
        List<Field> fieldList = new ArrayList<>();

        for (int i=0; i < 50; ++i) {
            fieldList.add(field);
        }

        when(gameMap.getMap()).thenReturn(fieldList);




    }

}