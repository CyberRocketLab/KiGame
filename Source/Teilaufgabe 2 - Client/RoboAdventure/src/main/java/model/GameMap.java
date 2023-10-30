package model;

import java.util.*;

public class GameMap {
    private final List<Field> map = new ArrayList<>();


    public void generateMap() {
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


        for (int posY = 0; posY < 5; ++posY) {
            for (int posX = 0; posX < 10; ++posX) {
                Terrain randomTerrain = terrainList.remove(0);

                if(!fortExists && randomTerrain == Terrain.GRASS) {
                    Field field = new FieldClient(
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

                Field field = new FieldClient(
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

    public boolean validateMap() {
        int maxColumns = 10;
        int maxRows = 5;
        MapValidator mapValidator = new MapValidator(map, maxRows, maxColumns);
        return mapValidator.validateMap();
    }

    public List<Field> getMap() {
        return map;
    }
}
