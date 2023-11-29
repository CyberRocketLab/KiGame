package strategy;

import exceptions.MoveStrategyException;
import model.data.Terrain;
import move.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class LeftCornerDown implements MoveStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LeftCornerDown.class);
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        Optional<Node> nodeToReturn = nodeList
                .stream()
                .filter(node -> node.getField().getPositionX() == axisX.start()
                        && node.getField().getPositionY() == axisY.end())
                .findFirst();

        if (nodeToReturn.isEmpty()) {
            logger.error("Could not find Node");
            throw new MoveStrategyException("Could not find Node");
        }


        if (nodeToReturn.get().getField().getTerrain() == Terrain.WATER) {
            return getAdjacentNonWaterField(nodeToReturn.get());
        }

        return nodeToReturn.get();
    }
}
