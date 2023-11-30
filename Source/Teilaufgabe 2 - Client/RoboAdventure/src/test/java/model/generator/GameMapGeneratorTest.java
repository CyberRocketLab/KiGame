package model.generator;

import model.data.Field;
import model.data.Terrain;
import model.state.FortState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameMapGeneratorTest {

    @Mock
    BusinessLogicInterface businessLogicInterface;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void mapGenerate_shouldContain_OneFort() {
        businessLogicInterface = Mockito.mock(BusinessLogicInterface.class);
        when(businessLogicInterface.getAmountOfTerrains(5, 10)).thenReturn(generateTerrainList());

        GameMapGenerator gameMapGenerator = new GameMapGenerator(businessLogicInterface);

        List<Field> fieldList = gameMapGenerator.generateRandomMap();

        long countFort = fieldList.stream()
                .filter(field -> field.getFortState() == FortState.MyFort)
                .count();

        assertEquals(1, countFort);
    }

    private List<Terrain> generateTerrainList() {
        List<Terrain> terrainList = new ArrayList<>();

        for (int i = 0; i < 50; ++i) {
            terrainList.add(Terrain.GRASS);
        }

        return terrainList;
    }

    @Test
    public void givenEmptyList_whenReceiving_thenIllegalArgumentException() {
        businessLogicInterface = Mockito.mock(BusinessLogicInterface.class);

        List<Terrain> emptyList = new ArrayList<>();

        when(businessLogicInterface.getAmountOfTerrains(5, 10)).thenReturn(emptyList);

        GameMapGenerator gameMapGenerator = new GameMapGenerator(businessLogicInterface);

        assertThrows(IllegalArgumentException.class, gameMapGenerator::generateRandomMap);
    }

    @Test
    public void givenNullList_whenReceiving_thenIllegalArgumentException() {
        businessLogicInterface = Mockito.mock(BusinessLogicInterface.class);

        when(businessLogicInterface.getAmountOfTerrains(5, 10)).thenReturn(null);

        GameMapGenerator gameMapGenerator = new GameMapGenerator(businessLogicInterface);

        assertThrows(IllegalArgumentException.class, gameMapGenerator::generateRandomMap);
    }



}