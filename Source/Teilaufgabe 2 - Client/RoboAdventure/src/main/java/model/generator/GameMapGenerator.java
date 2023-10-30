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
    private final List<Field> map = new ArrayList<>();

    public GameMapGenerator() {
    }

    public void generateRandomMap(int maxRows, int maxColumns) {
        List<Terrain> terrainList = new ArrayList<>();
        boolean fortExists = false;

        // If Map was generated WRONG the Map will be erased
        if (!map.isEmpty()) {
            map.clear();
        }

        // Allowed percent of Fields in the map
        double percentOfWater = 0.14;
        double percentOfGrass = 0.6;
        double percentOfMountain = 0.26;
        int totalQuantityOfFields = 50;

        // Calculating exact amount of each Field in the Map
        int water = (int) (totalQuantityOfFields * percentOfWater);
        int grass = (int) (totalQuantityOfFields * percentOfGrass);
        int mountain = (int) (totalQuantityOfFields * percentOfMountain);

        for (int i = 0; i < water; ++i) {
            terrainList.add(Terrain.WATER);
        }

        for (int i = 0; i < grass; ++i) {
            terrainList.add(Terrain.GRASS);
        }

        for (int i = 0; i < mountain; ++i) {
            terrainList.add(Terrain.MOUNTAIN);
        }

        Collections.shuffle(terrainList, new Random());


        for (int posY = 0; posY < maxRows; ++posY) {
            for (int posX = 0; posX < maxColumns; ++posX) {
                Terrain randomTerrain = terrainList.remove(0);

                if(!fortExists && randomTerrain == Terrain.GRASS) {
                    Field field = new Field(
                            posX,
                            posY,
                            randomTerrain,
                            PlayerPositionState.NOBODY,
                            TreasureState.UnknownTreasure,
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
    }

    public List<Field> getMap() {
        return map;
    }
}
