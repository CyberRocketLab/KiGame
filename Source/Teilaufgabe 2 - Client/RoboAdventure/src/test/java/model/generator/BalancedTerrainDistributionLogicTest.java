package model.generator;

import model.data.Terrain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.channels.OverlappingFileLockException;
import java.util.List;

class BalancedTerrainDistributionLogicTest {

    @Test
    public void balancedTerrainDistributionLogic_whenGettingAmountOfTerrains_thenReturnCorrectAmount() {
        BalancedTerrainDistributionLogic logic = new BalancedTerrainDistributionLogic();
        int maxRows = 5;
        int maxColumns = 10;
        int totalQuantityOfFields = maxRows * maxColumns;

        List<Terrain> terrainList = logic.getAmountOfTerrains(maxRows, maxColumns);

        long countWater = terrainList.stream().filter(terrain -> terrain == Terrain.WATER).count();
        long countGrass = terrainList.stream().filter(terrain -> terrain == Terrain.GRASS).count();
        long countMountain = terrainList.stream().filter(terrain -> terrain == Terrain.MOUNTAIN).count();

        assertEquals((int) (totalQuantityOfFields * BalancedTerrainDistributionLogic.percentOfWater), countWater);
        assertEquals((int) (totalQuantityOfFields * BalancedTerrainDistributionLogic.percentOfGrass), countGrass);
        assertEquals((int) (totalQuantityOfFields * BalancedTerrainDistributionLogic.percentOfMountain), countMountain);
    }

}