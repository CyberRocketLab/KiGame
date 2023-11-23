package model.generator;

import model.data.Field;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameMapGenerator {

    public static List<Field> generateRandomMap() {
        int maxRows = 5;
        int maxColumns = 10;

        List<Field> map = new ArrayList<>();
        boolean fortExists = false;

        List<Terrain> terrainList = calculatingAmountOfTerrains(maxRows, maxColumns);
        Collections.shuffle(terrainList, new Random());

        int firstTerrain = 0;
        for (int posY = 0; posY < maxRows; ++posY) {
            for (int posX = 0; posX < maxColumns; ++posX) {

                Terrain randomTerrain = terrainList.remove(firstTerrain);

                if(!fortExists && randomTerrain == Terrain.GRASS) {
                    Field field = new Field(
                            posX,
                            posY,
                            randomTerrain,
                            PlayerPositionState.ME,
                            TreasureState.NoTreasure,
                            FortState.MyFort
                    );

                    fortExists = true;
                    map.add(field);
                    continue;
                }

                Field field = new Field(
                        posX,
                        posY,
                        randomTerrain,
                        PlayerPositionState.NOBODY,
                        TreasureState.UnknownTreasure,
                        FortState.NoFort
                );

                map.add(field);
            }
        }

        return map;
    }

    private static List<Terrain> calculatingAmountOfTerrains(int maxRows, int maxColumns) {
        List<Terrain> terrainList = new ArrayList<>();

        // Allowed percent of Fields in the map
        double percentOfWater = 0.14;
        double percentOfGrass = 0.6;
        double percentOfMountain = 0.26;
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
