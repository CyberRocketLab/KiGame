package strategy;

import exceptions.MoveStrategyException;
import model.data.Terrain;
import model.state.PlayerPositionState;
import move.Node;
import network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CheckUnvisitedFields implements MoveStrategy {
    private static final Logger logger = LoggerFactory.getLogger(CheckUnvisitedFields.class);
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        Optional<Node> aimNode = nodeList.stream()
                .filter(node -> node.getField().getPositionX() >= axisX.start() && node.getField().getPositionX() <= axisX.end() &&
                        node.getField().getPositionY() >= axisY.start() && node.getField().getPositionY() <= axisY.end() &&
                        !node.getField().isVisited() && node.getField().getTerrain() == Terrain.GRASS
                )
                .findFirst();


        if (aimNode.isEmpty()) {
            logger.error("All Fields were found");
            throw new MoveStrategyException("All Fields were found");
        }
        return aimNode.get();
    }
}
