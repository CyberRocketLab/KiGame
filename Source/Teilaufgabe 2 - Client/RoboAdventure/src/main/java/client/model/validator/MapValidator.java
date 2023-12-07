package client.model.validator;

import client.model.data.Terrain;
import client.model.position.Position;
import client.model.state.PlayerPositionState;
import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class MapValidator {
    private static final Logger logger = LoggerFactory.getLogger(MapValidator.class);
    private final int ROWS;
    private final int COLUMNS;
    private final BusinessRules businessRules;

    public MapValidator(BusinessRules businessRules) {
        logger.info("Initialising MapValidator with BusinessRules");
        if (businessRules == null) {
            logger.error("Provided BusinessRules was null");
            throw new NullOrEmptyParameterException();
        }

        this.businessRules = businessRules;
        this.ROWS = businessRules.getROWS();
        this.COLUMNS = businessRules.getCOLUMNS();
    }

    public boolean validateMap(List<Field> originalMap) {
        logger.info("Starting Validating Map");
        if (originalMap == null || originalMap.isEmpty()) {
            logger.error("Provided Map was null or empty");
            throw new NullOrEmptyParameterException();
        }

        if (!businessRules.validateBusinessRules(originalMap)) {
            logger.warn("Business rule was violating");
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


        if (!businessRules.isAllowedAmountOfWaterOnEdges(matrix)) {
            logger.warn("To much water on edges of map");
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


    private Optional<Position> getFirstNonWaterField(List<Field> mapToValidate) {
        return mapToValidate.stream()
                .filter(field -> field.getTerrain() != Terrain.WATER)
                .findFirst()
                .map(field -> new Position(field.getPositionX(), field.getPositionY()));
    }

    private List<Position> getAdjacentFieldsPosition(int horizontalX, int verticalY, Field[][] matrix) {
        List<Position> adjacentFields = new ArrayList<>();

        // Right Field from current Field
        if (horizontalX + 1 < COLUMNS && matrix[verticalY][horizontalX + 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX + 1, verticalY));
        }

        // Left Field from current Field
        if (horizontalX - 1 >= 0 && matrix[verticalY][horizontalX - 1].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX - 1, verticalY));
        }

        // Up Field from current Field
        if (verticalY + 1 < ROWS && matrix[verticalY + 1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX, verticalY + 1));
        }

        // Down Field from current Field
        if (verticalY - 1 >= 0 && matrix[verticalY - 1][horizontalX].getPlayerPositionState() != PlayerPositionState.VISITED) {
            adjacentFields.add(new Position(horizontalX, verticalY - 1));
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
