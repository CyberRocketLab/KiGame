package client.move;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.data.Terrain;
import client.model.state.PlayerPositionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Move {
    private static final Logger logger = LoggerFactory.getLogger(Move.class);
    private List<Node> nodeList = new LinkedList<>();
    private Node sourceNode;

    private List<Node> movesToTargetField = new ArrayList<>();
    private List<EMoves> moves = new ArrayList<>();

    public Move(List<Field> map) {
        if (map == null || map.isEmpty()) {
            logger.error("Provided List<Field> map was null or empty");
            throw new NullOrEmptyParameterException();
        }

        map.forEach(field -> this.nodeList.add(new Node(field)));
        nodeList.forEach(node -> node.addAdjacentNodes(nodeList));
        sourceNode = getPlayerPosition();

        Graph.calculateShortestPathFromSource(sourceNode);
    }

    public Node getPlayerPosition() {
        return nodeList.stream()
                .filter(node -> node.getField().getPlayerPositionState() == PlayerPositionState.ME || node.getField().getPlayerPositionState() == PlayerPositionState.BOTH)
                .findFirst().orElseThrow();
    }

    public Node findNode(Field field) {
        if (field == null) {
            logger.error("Provided Field was null");
            throw new NullOrEmptyParameterException();
        }

        return nodeList.stream()
                .filter(node -> node.getField().getPositionX() == field.getPositionX() && node.getField().getPositionY() == field.getPositionY())
                .findFirst().orElseThrow();
    }


    private void setTargetNode(Node targetNode) {
        if (targetNode == null) {
            logger.error("Provided Node was null");
            throw new NullOrEmptyParameterException();
        }

        sourceNode = getPlayerPosition();
        logger.debug("Source Node {}", sourceNode);

        Graph.calculateShortestPathFromSource(sourceNode);
        // movesToTargetField.addAll(targetNode.getShortestPathFromSource());
        movesToTargetField = targetNode.getShortestPathFromSource();

        // So the target Node also will be visited
        movesToTargetField.add(targetNode);
    }

    public void setMovesToTargetField(Node targetNode) {
        if (targetNode == null) {
            logger.error("Provided Node was null");
            throw new NullOrEmptyParameterException();
        }

        logger.debug("Setting moves to target Node: X:{} , Y:{}", targetNode.getField().getPositionX(), targetNode.getField().getPositionY());
        setTargetNode(targetNode);
        logger.debug("Moves to Target Node: {}", movesToTargetField);


        for (int i = 0; i < movesToTargetField.size() - 1; i++) {
            Node currentNode = movesToTargetField.get(i);
            Node nextNode = movesToTargetField.get(i + 1);

            int differenceBetweenPosX = nextNode.getField().getPositionX() - currentNode.getField().getPositionX();
            int differenceBetweenPosY = nextNode.getField().getPositionY() - currentNode.getField().getPositionY();

            int moveCost = calculateMoveCost(currentNode.getField().getTerrain(), nextNode.getField().getTerrain());

            for (int cost = 0; cost < moveCost; cost++) {
                if (differenceBetweenPosX > 0) {
                    moves.add(EMoves.Right);
                } else if (differenceBetweenPosX < 0) {
                    moves.add(EMoves.Left);
                } else if (differenceBetweenPosY > 0) {
                    moves.add(EMoves.Down);
                } else if (differenceBetweenPosY < 0) {
                    moves.add(EMoves.Up);
                }
            }
        }

        logger.debug("Moves to Target Node: {}", moves);
    }

    public List<EMoves> getMoves() {
        return moves;
    }

    public int calculateMoveCost(Terrain currentTerrain, Terrain nextTerrain) {
        if (currentTerrain == null || nextTerrain == null) {
            logger.error("Provided currentTerrain: {} or nextTerrain: {} for calculateMoveCost was null", currentTerrain, nextTerrain);
            throw new NullOrEmptyParameterException();
        }

        if (currentTerrain == Terrain.GRASS && nextTerrain == Terrain.GRASS) {
            return 2;
        } else if ((currentTerrain == Terrain.GRASS && nextTerrain == Terrain.MOUNTAIN) ||
                (currentTerrain == Terrain.MOUNTAIN && nextTerrain == Terrain.GRASS)) {
            return 3;
        } else if (currentTerrain == Terrain.MOUNTAIN && nextTerrain == Terrain.MOUNTAIN) {
            return 4;
        }

        return 1;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

}
