package model.generator;

import exceptions.NullOrEmptyParameterException;
import model.data.Field;
import model.data.Terrain;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameMapGenerator {
    private static final Logger logger = LoggerFactory.getLogger(GameMapGenerator.class);
    BusinessLogicInterface businessLogic;

    public GameMapGenerator(BusinessLogicInterface businessLogic) {
        this.businessLogic = businessLogic;
    }

    public List<Field> generateRandomMap() {
        logger.info("Starting to generate random Map");
        int maxRows = 5;
        int maxColumns = 10;

        List<Field> map = new ArrayList<>();

        List<Terrain> terrainList = businessLogic.getAmountOfTerrains(maxRows, maxColumns);

        if (terrainList == null || terrainList.isEmpty()) {
            logger.error("Invalid terrain list received from business logic");
            throw new NullOrEmptyParameterException();
        }

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
