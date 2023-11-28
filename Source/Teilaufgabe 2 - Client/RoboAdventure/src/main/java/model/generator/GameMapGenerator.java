package model.generator;

import model.data.Field;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

import java.util.*;

public class GameMapGenerator {
    BusinessLogicInterface businessLogic;

    public GameMapGenerator(BusinessLogicInterface businessLogic) {
        this.businessLogic = businessLogic;
    }

    public List<Field> generateRandomMap() {
        int maxRows = 5;
        int maxColumns = 10;

        List<Field> map = new ArrayList<>();

        List<Terrain> terrainList = businessLogic.calculateAmountOfTerrains(maxRows, maxColumns);

        Collections.shuffle(terrainList, new Random());

        int firstTerrain = 0;
        for (int posY = 0; posY < maxRows; ++posY) {
            for (int posX = 0; posX < maxColumns; ++posX) {
                Terrain randomTerrain = terrainList.remove(firstTerrain);

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

        setRandomFortPosition(map);

        return map;
    }

    private void setRandomFortPosition(List<Field> map) {
        List<Field> grassFields = map.stream()
                .filter(field -> field.getTerrain() == Terrain.GRASS)
                .toList();

        int randomPosition = (int)(Math.random() * (grassFields.size() -1));
        grassFields.get(randomPosition).setFortState(FortState.MyFort);
    }


}
