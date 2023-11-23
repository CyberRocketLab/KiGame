package model.validator;

import model.position.Position;
import model.data.Field;
import model.data.Terrain;
import model.state.PlayerPositionState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MapValidator {
    private final List<Field> mapToValidate = new ArrayList<>();

    public boolean validateMap(List<Field> originalMap) {
        for (Field field : originalMap) {
            mapToValidate.add(new Field(field));
        }

        // Initialising matrix with maxRows and maxCol
        int maxRows = 5;
        int maxCol = 10;
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
            int horizontalX = currentFieldPosition.x();
            int verticalY = currentFieldPosition.y();

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
