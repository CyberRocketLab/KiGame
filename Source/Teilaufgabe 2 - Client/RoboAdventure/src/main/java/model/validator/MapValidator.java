package model.validator;

import exceptions.MapBusinessRuleException;
import model.position.Position;
import model.data.Field;
import model.data.Terrain;
import model.state.PlayerPositionState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class MapValidator {
    private static final int ROWS = 5;
    private static final int COLUMNS = 10;

    public boolean validateMap(List<Field> originalMap) {
        if (originalMap == null || originalMap.isEmpty()) {
            throw new MapBusinessRuleException("The map is empty or null");
        }

        if (!validateBusinessRules(originalMap)) {
            return false;
        }

        // Creating deep copy with purpose not to change original map
        List<Field> mapToValidate = new ArrayList<>();

        for (Field field : originalMap) {
            mapToValidate.add(new Field(field));
        }

        Field[][] matrix = new Field[ROWS][COLUMNS];

        for (Field field : mapToValidate) {
            matrix[field.getPositionY()][field.getPositionX()] = field;
        }

        if (!isAllowedAmountOfWaterOnEdges(matrix)) {
            System.out.println("Not Allowed Water on egdes");
            return false;
        }

        // Creating Stack with start Position
        Stack<Position> stack = new Stack<>();
        Optional<Position> nonWaterPosition = getFirstNonWaterField(mapToValidate);
        nonWaterPosition.ifPresent(stack::push);

        while (!stack.empty()) {
            Position currentFieldPosition = stack.pop();
            int horizontalX = currentFieldPosition.x();
            int verticalY = currentFieldPosition.y();

            Field currentField = matrix[verticalY][horizontalX];

            if (currentField.getPlayerPositionState() == PlayerPositionState.VISITED || currentField.getTerrain() == Terrain.WATER) {
                continue;
            }

            currentField.setPlayerPositionState(PlayerPositionState.VISITED);

            for (Position position : getAdjacentFieldsPosition(horizontalX, verticalY, matrix)) {
                stack.push(position);
            }
        }

        return allFieldsVisited(matrix);
    }

    private boolean validateBusinessRules(List<Field> map) {
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
                ( countMountain >= minAmountOfMountain);
    }

    private boolean isAllowedAmountOfWaterOnEdges(Field[][] matrix) {
        int shortEdgeMaxWater = 2;
        int longEdgeMaxWater = 4;

        int topEdgeWater = 0;
        int bottomEdgeWater = 0;
        for(int x = 0; x < COLUMNS; ++x) {
            if(matrix[0][x].getTerrain() == Terrain.WATER) {
                ++topEdgeWater;
            }
            if(matrix[4][x].getTerrain() == Terrain.WATER) {
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

    private Optional<Position> getFirstNonWaterField(List<Field> mapToValidate) {
        return mapToValidate.stream()
                .filter(field -> field.getTerrain() != Terrain.WATER)
                .findFirst()
                .map(field -> new Position(field.getPositionX(), field.getPositionY()));
    }

    private List<Position> getAdjacentFieldsPosition(int horizontalX, int verticalY, Field[][] matrix) {
        List<Position> adjacentFields= new ArrayList<>();

        // Right Field from current Field
        if (horizontalX + 1 < COLUMNS && matrix[verticalY][horizontalX + 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX + 1,verticalY));
        }

        // Left Field from current Field
        if (horizontalX - 1 >= 0 && matrix[verticalY][horizontalX - 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX - 1,verticalY));
        }

        // Up Field from current Field
        if (verticalY + 1 < ROWS && matrix[verticalY +1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX,verticalY + 1));
        }

        // Down Field from current Field
        if (verticalY - 1 >= 0 &&  matrix[verticalY -1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX,verticalY - 1));
        }

        return adjacentFields;
    }


    private boolean allFieldsVisited(Field[][] matrix) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (!(matrix[i][j].getPlayerPositionState() == PlayerPositionState.VISITED) && matrix[i][j].getTerrain() != Terrain.WATER) {
                    return false;
                }
            }
        }

        return true;
    }
}
