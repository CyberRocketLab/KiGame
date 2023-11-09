package move;

import model.data.GameMap;
import model.data.Terrain;
import model.state.MoveStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class NextFieldToCheck {

    private final int edgeOfX;
    private final int edgeOfY;
    private boolean treasureFound = false;
    private final List<Node> nodeList;


    MoveStrategy moveStrategy = MoveStrategy.RightCornerDown;



    public NextFieldToCheck(GameMap map, List<Node> nodeList ) {
        this.edgeOfX = map.getEdgeOfX();
        this.edgeOfY = map.getEdgeOfY();
        this.nodeList = nodeList;
    }

    public Node getNextFieldToCheck() {
        if(!treasureFound) {
            int fieldToCheckX;
            int fieldToCheckY;

            switch (moveStrategy) {
                case RightCornerDown:

                    // Position of RightCornerDown
                    fieldToCheckX = 9;
                    fieldToCheckY = 4;

                    moveStrategy = MoveStrategy.RightCornerUp;
                    return getFieldWithPosition(fieldToCheckX, fieldToCheckY);

                case RightCornerUp:

                    // Position of RightCornerUp
                    fieldToCheckX = 9;
                    fieldToCheckY = 0;

                    moveStrategy = MoveStrategy.LeftCornerDown;
                    return getFieldWithPosition(fieldToCheckX, fieldToCheckY);

                case LeftCornerDown:
                    // Position of LeftCornerDown
                    fieldToCheckX = 0;
                    fieldToCheckY = 4;

                    moveStrategy = MoveStrategy.CheckUnvisitedGrass;
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

        if(nodeToReturn.field.getTerrain() == Terrain.WATER) {
            return nodeToReturn.getAdjacentNodes().keySet().stream()
                    .filter(node -> node.field.getTerrain() != Terrain.WATER)
                    .findFirst().orElseThrow();
        }


        return nodeToReturn;
    }



}
