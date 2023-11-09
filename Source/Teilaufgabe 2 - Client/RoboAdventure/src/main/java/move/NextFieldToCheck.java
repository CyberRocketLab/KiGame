package move;

import model.data.GameMap;
import model.data.Terrain;
import model.state.MoveStrategy;
import model.state.PlayerPositionState;

import java.util.List;
import java.util.stream.Collectors;

public class NextFieldToCheck {

    private final int edgeOfX;
    private final int edgeOfY;
    private boolean treasureFound = false;
    private final List<Node> nodeList;

    private final int myHalfStartX;
    private final int myHalfEndX;
    private final int myHalfStartY;
    private final int myHalfEndY;


    public NextFieldToCheck(GameMap map, List<Node> nodeList ) {
        this.edgeOfX = map.getEdgeOfX();
        this.edgeOfY = map.getEdgeOfY();
        this.nodeList = nodeList;

        Node startPosition = nodeList.stream()
                .filter(node -> node.field.getPlayerPositionState() == PlayerPositionState.ME)
                .findFirst().orElseThrow();

        if (edgeOfX > edgeOfY) {
            // Horizontal map (5x20)
            myHalfStartX = startPosition.field.getPositionX() <= edgeOfX / 2 ? 0 : (edgeOfX / 2) + 1;
            myHalfEndX = startPosition.field.getPositionX() <= edgeOfX / 2 ? edgeOfX / 2 : edgeOfX;
            myHalfStartY = 0;
            myHalfEndY = edgeOfY;
        } else {
            // Vertical map (10x10)
            myHalfStartY = startPosition.field.getPositionY() <= edgeOfY / 2 ? 0 : (edgeOfY / 2) + 1;
            myHalfEndY = startPosition.field.getPositionY() <= edgeOfY / 2 ? edgeOfY / 2 : edgeOfY;
            myHalfStartX = 0; // On a vertical map, X spans the entire width
            myHalfEndX = edgeOfX;
        }

    }

    public Node getNextFieldToCheck(MoveStrategy moveStrategy) {
        if(!treasureFound) {
            int fieldToCheckX;
            int fieldToCheckY;

            switch (moveStrategy) {
                case RightCornerDown:
                    // Position of RightCornerDown
                    fieldToCheckX = myHalfEndX;
                    fieldToCheckY = myHalfEndY;

                    return getFieldWithPosition(fieldToCheckX, fieldToCheckY);

                case RightCornerUp:
                    // Position of RightCornerUp
                    fieldToCheckX = myHalfEndX;
                    fieldToCheckY = myHalfStartY;

                    return getFieldWithPosition(fieldToCheckX, fieldToCheckY);

                case LeftCornerDown:
                    // Position of LeftCornerDown
                    fieldToCheckX = myHalfStartX;
                    fieldToCheckY = myHalfEndY;

                    return getFieldWithPosition(fieldToCheckX, fieldToCheckY);
            }

            treasureFound = true;
        }

        return null;
    }


    private Node getFieldWithPosition(int fieldToCheckX, int fieldToCheckY) {
        Node nodeToReturn = nodeList
                .stream()
                .filter(node -> node.field.getPositionX() == fieldToCheckX
                        && node.field.getPositionY() == fieldToCheckY )
                .findFirst().orElseThrow();

        // If Node is Water get first adjacent non water Field
        if(nodeToReturn.field.getTerrain() == Terrain.WATER) {
            return nodeToReturn.getAdjacentNodes().keySet().stream()
                    .filter(node -> node.field.getTerrain() != Terrain.WATER)
                    .findFirst().orElseThrow();
        }


        return nodeToReturn;
    }



}
