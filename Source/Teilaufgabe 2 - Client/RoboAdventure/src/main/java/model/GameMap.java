package model;

import java.util.*;

public class GameMap {
    private List<Field> map = new ArrayList<>();



    public void generateMap() {
        List<Terrain> randomField = new ArrayList<>();

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
            randomField.add(Terrain.WATER);
        }

        for (int i = 0; i < grass; ++i) {
            randomField.add(Terrain.GRASS);
        }

        for (int i = 0; i < mountain; ++i) {
            randomField.add(Terrain.MOUNTAIN);
        }

        Collections.shuffle(randomField, new Random());


        for (int posX = 0; posX < 10; ++posX) {
            for (int posY = 0; posY < 5; ++posY) {
                Field field = new FieldClient(
                        posX,
                        posY,
                        randomField.remove(0),
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
