package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MapValidator {
    private final List<Field> mapToValidate = new ArrayList<>();
    private final int maxRows;
    private final int maxCol;

    public MapValidator(List<Field> originalMap, int maxRows, int maxCol) {
        for (Field field : originalMap) {
            mapToValidate.add(new FieldClient(field));
        }

        this.maxRows = maxRows;
        this.maxCol = maxCol;
    }

    public boolean validateMap() {
        // Initialising matrix with maxRows and maxCol
        Field[][] matrix = new Field[maxRows][maxCol];

        for (Field field : mapToValidate) {
            matrix[field.getPositionY()][field.getPositionX()] = field;
        }

        // Creating Stack with start Position
        Stack<Position> stack = new Stack<>();
        int horX = 0;
        int vertY = 0;

        // If Start Position (0,0) is on WATTER peek random position what is not on WATTER
        while (matrix[vertY][horX].getTerrain() == Terrain.WATER) {
            vertY = (int) (Math.random() * maxRows);
            horX = (int) (Math.random() * maxCol);
        }

        stack.push(new Position(horX,vertY));

        while (!stack.empty()) {
            Position currentFieldPosition = stack.pop();
            int horizontalX = currentFieldPosition.getX();
            int verticalY = currentFieldPosition.getY();

            Field currentField = matrix[verticalY][horizontalX];

            if (currentField.getPlayerPositionState() == PlayerPositionState.VISITED || currentField.getTerrain() == Terrain.WATER) {
                continue;
            }

            // Field that was discovered are marked as PlayerPositionState.VISITED
            currentField.setPlayerPositionState(PlayerPositionState.VISITED);

            // Push Field to Stack that is RIGHT from current Field
            if (horizontalX + 1 < maxCol && matrix[verticalY][horizontalX + 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
                stack.push(new Position(horizontalX + 1,verticalY));
            }

            // Push Field to Stack that is LEFT from current Field
            if (horizontalX - 1 >= 0 && matrix[verticalY][horizontalX - 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
                stack.push(new Position(horizontalX - 1,verticalY));
            }

            // Push Field to Stack that is UP from current Field
            if (verticalY + 1 < maxRows && matrix[verticalY +1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
                stack.push(new Position(horizontalX,verticalY + 1));
            }

            // Push Field to Stack that is DOWN from current Field
            if (verticalY - 1 >= 0 &&  matrix[verticalY -1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
                stack.push(new Position(horizontalX,verticalY - 1));
            }

        }

        // Checking if all Fields was visited
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (!(matrix[i][j].getPlayerPositionState() == PlayerPositionState.VISITED) && matrix[i][j].getTerrain() != Terrain.WATER) {
                    return false;
                }
            }
        }

        return true;
    }
}
