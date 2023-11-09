package move;

import model.data.Field;
import model.data.Terrain;
import model.state.PlayerPositionState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Move {
    private List<Node> nodeList = new LinkedList<>();
    private Node sourceNode;

    private List<Node> movesToTargetField = new ArrayList<>();
    private List<EMoves> moves = new ArrayList<>();

    public Move(List<Field> map) {
        map.forEach(field -> this.nodeList.add(new Node(field)));
        nodeList.forEach(node -> node.addAdjacentNodes(nodeList));

        // Setting source node to be player Position
        sourceNode = getPlayerPosition();

        Graph.calculateShortestPathFromSource(sourceNode);
    }

    public Node getPlayerPosition() {

        return nodeList.stream()
                .filter(node -> node.field.getPlayerPositionState() == PlayerPositionState.ME)
                .findFirst().orElseThrow();
    }

    public Node findNode(Field field) {
        return nodeList.stream()
                .filter(node -> node.field.getPositionX() == field.getPositionX() && node.field.getPositionY() == field.getPositionY())
                .findFirst().orElseThrow();
    }


    private void setTargetNode(Node targetNode) {
        movesToTargetField.addAll(targetNode.getShortestPathFromSource());
    }

    public void setMovesToTargetField(Node targetNode) {
        setTargetNode(targetNode);


        for (int i = 0; i < movesToTargetField.size() - 1; i++) {
            Node currentNode = movesToTargetField.get(i);
            Node nextNode = movesToTargetField.get(i + 1);


            int deltaX = nextNode.field.getPositionX() - currentNode.field.getPositionX();
            int deltaY = nextNode.field.getPositionY() - currentNode.field.getPositionY();

            int moveCost = calculateMoveCost(currentNode.field.getTerrain(), nextNode.field.getTerrain());

            for (int cost = 0; cost < moveCost; cost++) {
                if (deltaX > 0) {
                    moves.add(EMoves.Right);
                } else if (deltaX < 0) {
                    moves.add(EMoves.Left);
                } else if (deltaY > 0) {
                    moves.add(EMoves.Down);
                } else if (deltaY < 0) {
                    moves.add(EMoves.Up);
                }
            }
        }

    }

    public List<EMoves> getMoves() {
        return moves;
    }

    public int calculateMoveCost(Terrain currentTerrain, Terrain nextTerrain) {

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
