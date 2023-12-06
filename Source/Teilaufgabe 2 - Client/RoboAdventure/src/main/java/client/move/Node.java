package client.move;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.data.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
    private static final Logger logger = LoggerFactory.getLogger(Node.class);
    private final Field field;
    public Map<Node, Integer> adjacentNodes = new HashMap<>();
    private List<Node> shortestPathFromSource = new LinkedList<>();
    private Integer distanceInMoves = Integer.MAX_VALUE;

    public Node(Field field) {
        if (field == null) {
            logger.error("Provided Field was null");
            throw new NullOrEmptyParameterException();
        }

        this.field = field;
    }

    public void addAdjacentNodes(List<Node> map) {
        if (map == null || map.isEmpty()) {
            logger.error("Provided List<Node> map was null or empty");
            throw new NullOrEmptyParameterException();
        }

        List<Node> nodes =
                map.stream()
                        .filter(node ->
                                ((node.getField().getPositionX() == (field.getPositionX() + 1) && node.getField().getPositionY() == field.getPositionY()) ||
                                        (node.getField().getPositionX() == (field.getPositionX() - 1) && node.getField().getPositionY() == field.getPositionY()) ||
                                        (node.getField().getPositionY() == (field.getPositionY() + 1) && node.getField().getPositionX() == field.getPositionX()) ||
                                        (node.getField().getPositionY() == (field.getPositionY() - 1) && node.getField().getPositionX() == field.getPositionX()))
                                        && node.getField().getTerrain() != Terrain.WATER
                        ).toList();


        for (Node adjacentField : nodes) {
            int moves = 0;
            switch (field.getTerrain()) {
                case GRASS -> {
                    moves = (adjacentField.getField().getTerrain() == Terrain.GRASS) ? 2 : 3;
                }
                case MOUNTAIN -> {
                    moves = (adjacentField.getField().getTerrain() == Terrain.GRASS) ? 3 : 4;
                }
            }

            adjacentNodes.put(adjacentField, moves);
        }

    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public Integer getDistanceInMoves() {
        return distanceInMoves;
    }

    public void setDistanceInMoves(Integer distanceInMoves) {
        if (distanceInMoves == null) {
            logger.error("Provided Integer was null");
            throw new NullOrEmptyParameterException();
        }

        this.distanceInMoves = distanceInMoves;
    }

    public List<Node> getShortestPathFromSource() {
        return shortestPathFromSource;
    }

    public void setShortestPathFromSource(List<Node> shortestPathFromSource) {
        if (shortestPathFromSource == null || shortestPathFromSource.isEmpty()) {
            logger.error("Provided List<Node> shortestPathFromSource was null");
            throw new NullOrEmptyParameterException();
        }

        this.shortestPathFromSource = shortestPathFromSource;
    }

    @Override
    public String toString() {
        return "Node{" +
                "field= X:" + field.getPositionX() + " Y:" + field.getPositionY() +
                '}';
    }

    public Field getField() {
        return field;
    }
}
