package client.move;

import client.strategy.MoveStrategy;
import client.strategy.StartAndEndOfAxis;
import client.exceptions.NoFoundException;
import client.exceptions.NullOrEmptyParameterException;
import client.model.data.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NextFieldFinder {
    private static final Logger logger = LoggerFactory.getLogger(NextFieldFinder.class);

    private final List<Node> nodeList;
    private final StartAndEndOfAxis playerAxisX;
    private final StartAndEndOfAxis playerAxisY;

    private final StartAndEndOfAxis enemyAxisX;
    private final StartAndEndOfAxis enemyAxisY;


    public NextFieldFinder(GameMap map, List<Node> nodeList, Node startPosition) {
        if (map == null || nodeList == null || startPosition == null) {
            logger.error("One of provided variables was null. Map: {}, nodeList: {}, startPosition: {}", map, nodeList, startPosition );
            throw new NullOrEmptyParameterException();
        }

        int edgeOfX = map.getEdgeOfX();
        int edgeOfY = map.getEdgeOfY();
        this.nodeList = nodeList;

        int myHalfStartX;
        int myHalfEndX;
        int myHalfStartY;
        int myHalfEndY;

        if (edgeOfX > edgeOfY) {
            // Horizontal map (5x20)
            myHalfStartX = startPosition.getField().getPositionX() <= edgeOfX / 2 ? 0 : (edgeOfX / 2) + 1;
            myHalfEndX = startPosition.getField().getPositionX() <= edgeOfX / 2 ? edgeOfX / 2 : edgeOfX;
            myHalfStartY = 0;
            myHalfEndY = edgeOfY;

            enemyAxisX = myHalfStartX == 0 ? new StartAndEndOfAxis(myHalfEndX + 1, edgeOfX) : new StartAndEndOfAxis(0, myHalfStartX - 1);
            enemyAxisY = new StartAndEndOfAxis(0, edgeOfY);

        } else {
            // Vertical map (10x10)
            myHalfStartY = startPosition.getField().getPositionY() <= edgeOfY / 2 ? 0 : (edgeOfY / 2) + 1;
            myHalfEndY = startPosition.getField().getPositionY() <= edgeOfY / 2 ? edgeOfY / 2 : edgeOfY;
            myHalfStartX = 0;
            myHalfEndX = edgeOfX;

            enemyAxisY = myHalfStartY == 0 ? new StartAndEndOfAxis(myHalfEndY + 1, edgeOfY) : new StartAndEndOfAxis(0, myHalfStartY - 1);
            enemyAxisX = new StartAndEndOfAxis(0, edgeOfX);
        }

        playerAxisX = new StartAndEndOfAxis(myHalfStartX, myHalfEndX);
        playerAxisY = new StartAndEndOfAxis(myHalfStartY, myHalfEndY);
    }

    public Node getNextFieldToCheck(MoveStrategy moveStrategy, boolean isTreasureCollected) throws NoFoundException{
        if (moveStrategy == null) {
            logger.error("Provided MoveStrategy parameter for getNextFieldToCheck was null");
            throw new NullOrEmptyParameterException();
        }

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


}
