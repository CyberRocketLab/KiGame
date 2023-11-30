package model.generator;

import model.data.Terrain;

import java.util.ArrayList;
import java.util.List;

public class BalancedTerrainDistributionLogic implements BusinessLogicInterface {
    public static final double percentOfWater = 0.14;
    public static final double percentOfGrass = 0.6;
    public static final double percentOfMountain = 0.26;


    @Override
    public List<Terrain> getAmountOfTerrains(int maxRows, int maxColumns) {
        List<Terrain> terrainList = new ArrayList<>();

        int totalQuantityOfFields = maxRows * maxColumns;

        // Calculating exact amount of each Field in the Map
        int amountOfWater = (int) (totalQuantityOfFields * percentOfWater);
        int amountOfGrass = (int) (totalQuantityOfFields * percentOfGrass);
        int amountOfMountain = (int) (totalQuantityOfFields * percentOfMountain);

        for (int i = 0; i < amountOfWater; ++i) {
            terrainList.add(Terrain.WATER);
        }

        for (int i = 0; i < amountOfGrass; ++i) {
            terrainList.add(Terrain.GRASS);
        }

        for (int i = 0; i < amountOfMountain; ++i) {
            terrainList.add(Terrain.MOUNTAIN);
        }

        return terrainList;
    }
}
