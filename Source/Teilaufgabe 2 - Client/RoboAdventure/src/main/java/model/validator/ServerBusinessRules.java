package model.validator;

import model.data.Field;
import model.data.Terrain;

import java.util.List;

public class ServerBusinessRules implements BusinessRules {

    private final int ROWS = 5;
    private final int COLUMNS = 10;

    @Override
    public boolean validateBusinessRules(List<Field> map) {
        int minAmountOfWater = 7;
        int minAmountOfGrass = 24;
        int minAmountOfMountain = 5;

        long countWater = map.stream()
                .filter(field -> field.getTerrain() == Terrain.WATER)
                .count();

        long countGrass = map.stream()
                .filter(field -> field.getTerrain() == Terrain.GRASS)
                .count();

        long countMountain = map.stream()
                .filter(field -> field.getTerrain() == Terrain.MOUNTAIN)
                .count();

        return (countWater >= minAmountOfWater) &&
                (countGrass >= minAmountOfGrass) &&
                (countMountain >= minAmountOfMountain);
    }

    @Override
    public boolean isAllowedAmountOfWaterOnEdges(Field[][] matrix) {
        int shortEdgeMaxWater = 2;
        int longEdgeMaxWater = 4;

        int topEdgeWater = 0;
        int bottomEdgeWater = 0;
        for (int x = 0; x < COLUMNS; ++x) {
            if (matrix[0][x].getTerrain() == Terrain.WATER) {
                ++topEdgeWater;
            }
            if (matrix[4][x].getTerrain() == Terrain.WATER) {
                ++bottomEdgeWater;
            }
        }

        int leftEdgeWater = 0;
        int rightEdgeWater = 0;
        for (int y = 0; y < ROWS; y++) {
            if (matrix[y][0].getTerrain() == Terrain.WATER) {
                ++leftEdgeWater;
            }
            if (matrix[y][9].getTerrain() == Terrain.WATER) {
                ++rightEdgeWater;
            }
        }

        return topEdgeWater <= longEdgeMaxWater &&
                bottomEdgeWater <= longEdgeMaxWater &&
                leftEdgeWater <= shortEdgeMaxWater &&
                rightEdgeWater <= shortEdgeMaxWater;
    }

    @Override
    public int getROWS() {
        return ROWS;
    }

    @Override
    public int getCOLUMNS() {
        return COLUMNS;
    }
}
