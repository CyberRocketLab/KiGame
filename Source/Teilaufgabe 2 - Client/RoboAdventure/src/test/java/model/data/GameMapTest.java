package model.data;

import exceptions.NullOrEmptyParameterException;
import model.generator.BalancedTerrainDistributionLogic;
import model.generator.BusinessLogicInterface;
import model.generator.GameMapGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    public void should_throwException_when_initializeWithEmptyList() {
        List<Field> fieldList = new ArrayList<>();

        assertThrows(NullOrEmptyParameterException.class, () -> new GameMap(fieldList));
    }

    @Test
    public void should_throwException_when_initializeWithNULLList() {
        assertThrows(NullOrEmptyParameterException.class, () -> new GameMap(null));
    }


}