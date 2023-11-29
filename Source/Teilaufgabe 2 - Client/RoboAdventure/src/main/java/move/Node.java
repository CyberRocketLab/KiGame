package move;

import model.data.Field;
import model.data.Terrain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node {
    private final Field field;
    public Map<Node, Integer> adjacentNodes = new HashMap<>();
    private List<Node> shortestPathFromSource = new LinkedList<>();
    private Integer distanceInMoves = Integer.MAX_VALUE;

    public Node(Field field) {
        this.field = field;
    }

    public void addAdjacentNodes(List<Node> map) {
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
        this.distanceInMoves = distanceInMoves;
    }

    public List<Node> getShortestPathFromSource() {
        return shortestPathFromSource;
    }

    public void setShortestPathFromSource(List<Node> shortestPathFromSource) {
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
