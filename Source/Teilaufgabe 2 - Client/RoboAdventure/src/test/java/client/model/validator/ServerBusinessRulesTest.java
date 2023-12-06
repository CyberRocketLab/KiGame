package client.model.validator;

import client.model.data.Field;
import client.model.data.Terrain;
import client.model.validator.ServerBusinessRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerBusinessRulesTest {

    ServerBusinessRules serverBusinessRules;

    @BeforeEach
    public void init() {
        serverBusinessRules = new ServerBusinessRules();
    }

    @Test
    public void givenMapWithNotValidAmountOfTerrains_whenValidateBusinessRule_thenFalse() {
        List<Field> map = IntStream.range(0, 50)
                .mapToObj(i -> {
                    Field field = Mockito.mock(Field.class);
                    when(field.getTerrain()).thenReturn(Terrain.WATER);
                    return field;
                })
                .collect(Collectors.toList());

        assertFalse(serverBusinessRules.validateBusinessRules(map));
    }

    @Test
    public void givenNotValidSizeOfMap_whenValidateBusinessRule_thenFalse() {
        Field field = Mockito.mock(Field.class);
        List<Field> map = Arrays.asList(field, field);

        assertFalse(serverBusinessRules.validateBusinessRules(map));
    }

    @Test
    public void givenEmptyMap_whenValidateBusinessRule_thenFalse() {
        List<Field> map = new ArrayList<>();

        assertFalse(serverBusinessRules.validateBusinessRules(map));
    }

    @Test
    public void givenNullMap_whenValidateBusinessRule_thenFalse() {
        assertFalse(serverBusinessRules.validateBusinessRules(null));
    }

    @Test
    public void givenMapWithNotAllowedAmountOfWaterOnEdges_whenCallingAllowedAmountOfWaterOnEdges_thenFalse() {
        Field[][] matrix = new Field[5][10];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                Field field = mock(Field.class);
                when(field.getTerrain()).thenReturn(Terrain.WATER);
                matrix[i][j] = field;
            }
        }

        assertFalse(serverBusinessRules.isAllowedAmountOfWaterOnEdges(matrix));
    }

    @Test
    public void givenEmptyMatrix_whenCallingAllowedAmountOfWaterOnEdges_thenFalse() {
        Field[][] matrix = new Field[0][];

        assertFalse(serverBusinessRules.isAllowedAmountOfWaterOnEdges(matrix));
    }

    @Test
    public void givenNullMatrix_whenCallingAllowedAmountOfWaterOnEdges_thenFalse() {
        Field[][] matrix = null;

        assertFalse(serverBusinessRules.isAllowedAmountOfWaterOnEdges(matrix));
    }

}