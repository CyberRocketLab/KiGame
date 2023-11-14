package move;

import controller.ClientController;
import model.data.GameMap;
import model.data.Terrain;
import model.state.PlayerPositionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.MoveStrategy;
import strategy.StartAndEndOfAxis;

import java.util.List;

public class NextFieldToCheck {
    private static final Logger logger = LoggerFactory.getLogger(NextFieldToCheck.class);

    private final List<Node> nodeList;
    private final StartAndEndOfAxis playerAxisX;
    private final StartAndEndOfAxis playerAxisY;

    private final StartAndEndOfAxis enemyAxisX;
    private final StartAndEndOfAxis enemyAxisY;


    public NextFieldToCheck(GameMap map, List<Node> nodeList, Node startPosition) {
        int edgeOfX = map.getEdgeOfX();
        int edgeOfY = map.getEdgeOfY();
        this.nodeList = nodeList;

        /*Node startPosition = nodeList.stream()
                .filter(node -> node.field.getPlayerPositionState() == PlayerPositionState.ME)
                .findFirst().orElseThrow();
                */

        int myHalfStartX;
        int myHalfEndX;
        int myHalfStartY;
        int myHalfEndY;


        if (edgeOfX > edgeOfY) {
            // Horizontal map (5x20)
            myHalfStartX = startPosition.field.getPositionX() <= edgeOfX / 2 ? 0 : (edgeOfX / 2) + 1;
            myHalfEndX = startPosition.field.getPositionX() <= edgeOfX / 2 ? edgeOfX / 2 : edgeOfX;
            myHalfStartY = 0;
            myHalfEndY = edgeOfY;

            enemyAxisX = myHalfStartX == 0 ? new StartAndEndOfAxis(myHalfEndX + 1, edgeOfX) : new StartAndEndOfAxis(0, myHalfStartX - 1);
            enemyAxisY = new StartAndEndOfAxis(0, edgeOfY);

        } else {
            // Vertical map (10x10)
            myHalfStartY = startPosition.field.getPositionY() <= edgeOfY / 2 ? 0 : (edgeOfY / 2) + 1;
            myHalfEndY = startPosition.field.getPositionY() <= edgeOfY / 2 ? edgeOfY / 2 : edgeOfY;
            myHalfStartX = 0;
            myHalfEndX = edgeOfX;

            enemyAxisY = myHalfStartY == 0 ? new StartAndEndOfAxis(myHalfEndY + 1, edgeOfY) : new StartAndEndOfAxis(0, myHalfStartY - 1);
            enemyAxisX = new StartAndEndOfAxis(0, edgeOfX);
        }

        playerAxisX = new StartAndEndOfAxis(myHalfStartX, myHalfEndX);
        playerAxisY = new StartAndEndOfAxis(myHalfStartY, myHalfEndY);
    }

    public Node getNextFieldToCheck(MoveStrategy moveStrategy, boolean isTreasureCollected) {
        // if collected then search in enemy area
        if(isTreasureCollected) {
            logger.debug("Enemy Start-X: {} End-X: {}", enemyAxisX.start(), enemyAxisX.end());
            logger.debug("Enemy Start-Y: {} End.Y: {}", enemyAxisY.start(), enemyAxisY.end());
            return moveStrategy.getFieldWithPosition(enemyAxisX, enemyAxisY, nodeList);
        }

        logger.debug("My Start-X: {} End-X: {}", playerAxisX.start(), playerAxisX.end());
        logger.debug("My Start-Y: {} End.Y: {}", playerAxisY.start(), playerAxisY.end());
        return moveStrategy.getFieldWithPosition(playerAxisX, playerAxisY, nodeList);
    }

/*    public Node getNextFieldToCheck(MoveStrategy moveStrategy) {
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
    }*/


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
