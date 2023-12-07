package client.strategy;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Terrain;
import client.model.state.PlayerPositionState;
import client.move.Node;
import client.exceptions.NoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class VisitedNearestMountainToKI implements MoveStrategy {
    private static final Logger logger = LoggerFactory.getLogger(VisitedNearestMountainToKI.class);

    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) throws NoFoundException {
        if (axisX == null || axisY == null || nodeList == null || nodeList.isEmpty()) {
            logger.error("Provided parameters to getFieldWithPosition was Null Or Empty: " +
                    "axisX: {}, axisY: {}, nodeList: {}", axisX, axisY, nodeList);
            throw new NullOrEmptyParameterException();
        }

        List<Node> allMountains = nodeList.stream()
                .filter(node -> node.getField().getPositionX() >= axisX.start() &&
                        node.getField().getPositionX() <= axisX.end() &&
                        node.getField().getPositionY() >= axisY.start() &&
                        node.getField().getPositionY() <= axisY.end() &&
                        !node.getField().isVisited() &&
                        node.getField().getTerrain() == Terrain.MOUNTAIN &&
                        node.getField().getPlayerPositionState() != PlayerPositionState.ME &&
                        node.getField().getPlayerPositionState() != PlayerPositionState.BOTH
                )
                .toList();

        Optional<Node> nearestNodeToSource = allMountains.stream()
                .min(Comparator.comparingInt(Node::getDistanceInMoves));


        if (nearestNodeToSource.isEmpty()) {
            logger.error("All Fields were found");
            throw new NoFoundException();
        }
        return nearestNodeToSource.get();
    }
}

