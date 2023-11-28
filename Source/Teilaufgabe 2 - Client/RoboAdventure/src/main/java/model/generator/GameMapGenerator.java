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
        boolean fortExists = false;

        List<Terrain> terrainList = businessLogic.calculateAmountOfTerrains(maxRows, maxColumns);

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


}
