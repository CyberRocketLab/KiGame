package client.model.data;

import client.exceptions.NullOrEmptyParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameMap {
    private static final Logger logger = LoggerFactory.getLogger(GameMap.class);
    private static final int SQUARE_EDGE_X = 9;
    private static final int SQUARE_EDGE_Y = 9;
    private static final int RECTANGLE_EDGE_X = 19;
    private static final int RECTANGLE_EDGE_Y = 4;
    private final List<Field> fields;
    private int edgeOfX;
    private int edgeOfY;

    public GameMap(List<Field> fields) {
        logger.info("Initializing GameMap");
        if (fields == null || fields.isEmpty()) {
            logger.error("Provided List<Field> fields was null");
            throw new NullOrEmptyParameterException();
        }

        this.fields = fields;
        setEdges();
    }

    private void setEdges() {
        boolean isRectangleMap = fields.stream()
                .anyMatch(field -> field.getPositionX() == RECTANGLE_EDGE_X);

        if (isRectangleMap) {
            edgeOfX = RECTANGLE_EDGE_X;
            edgeOfY = RECTANGLE_EDGE_Y;
        } else {
            edgeOfX = SQUARE_EDGE_X;
            edgeOfY = SQUARE_EDGE_Y;
        }
    }

    public List<Field> getMap() {
        return fields;
    }

    public int getEdgeOfX() {
        return edgeOfX;
    }

    public int getEdgeOfY() {
        return edgeOfY;
    }

}
