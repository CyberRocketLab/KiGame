package strategy;

import exceptions.NoFoundException;
import model.data.Terrain;
import move.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitedNearestMountainToKI implements MoveStrategy {
    private static final Logger logger = LoggerFactory.getLogger(VisitedNearestMountainToKI.class);

    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        List<Node> allMountains = nodeList.stream()
                .filter(node -> node.getField().getPositionX() >= axisX.start() && node.getField().getPositionX() <= axisX.end() &&
                        node.getField().getPositionY() >= axisY.start() && node.getField().getPositionY() <= axisY.end() &&
                        !node.getField().isVisited() && node.getField().getTerrain() == Terrain.MOUNTAIN
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

