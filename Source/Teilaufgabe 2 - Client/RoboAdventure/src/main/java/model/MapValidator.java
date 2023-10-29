package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MapValidator {
    private List<Field> mapToValidate = new ArrayList<>();
    private Field[][] matrix = new Field[5][10];
    private int maxRows = 5;
    private int maxCol = 10;

    public MapValidator(List<Field> originalMap) {
        for (Field field : originalMap) {
            mapToValidate.add(new FieldClient(field));
        }

        for (Field field : mapToValidate) {
            matrix[field.getPositionY()][field.getPositionX()] = field;
        }
    }

    public boolean validateMap() {
        Stack<Position> stack = new Stack<>();
        stack.push(new Position(0,0));

        while (!stack.empty()) {
            Position currentPosition = stack.pop();
            int positionX = currentPosition.getX();
            int positionY = currentPosition.getY();

            if (positionX < 0 || positionX >= maxCol || positionY < 0 || positionY >= maxRows) {
                continue;
            }

            Field currentField = matrix[positionY][positionX];

            if (currentField.getPlayerPositionState() == PlayerPositionState.ME || currentField.getTerrain() == Terrain.WATER) {
                continue;
            }


            matrix[positionY][positionX].setPlayerPositionState(PlayerPositionState.ME);

            if (positionX + 1 < maxCol && matrix[positionY][positionX + 1].getPlayerPositionState() != PlayerPositionState.ME) {
                stack.push(new Position(positionX + 1,positionY ));
            }

            if (positionX - 1 >= 0 && matrix[positionY][positionX - 1].getPlayerPositionState() != PlayerPositionState.ME) {
                stack.push(new Position(positionX - 1,positionY ));
            }

            if (positionY + 1 < maxRows && matrix[positionY +1][positionX].getPlayerPositionState() != PlayerPositionState.ME) {
                stack.push(new Position(positionX,positionY + 1));
            }

            if (positionY - 1 >= 0 &&  matrix[positionY -1][positionX].getPlayerPositionState() != PlayerPositionState.ME) {
                stack.push(new Position(positionX,positionY - 1));
            }

        }

        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (!(matrix[i][j].getPlayerPositionState() == PlayerPositionState.ME) && matrix[i][j].getTerrain() != Terrain.WATER) {
                    return false;
                }
            }
        }


        return true;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }
}
