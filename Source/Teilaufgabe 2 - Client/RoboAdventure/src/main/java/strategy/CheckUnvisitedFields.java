package strategy;

import exceptions.MoveStrategyException;
import model.data.Terrain;
import move.Node;

import java.util.List;
import java.util.Optional;

public class CheckUnvisitedFields implements MoveStrategy {
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        Optional<Node> aimNode = nodeList.stream()
                .filter(node -> node.getField().getPositionX() >= axisX.start() && node.getField().getPositionX() <= axisX.end() &&
                        node.getField().getPositionY() >= axisY.start() && node.getField().getPositionY() <= axisY.end() &&
                        !node.getField().isVisited() && node.getField().getTerrain() == Terrain.GRASS
                )
                .findFirst();

        if (aimNode.isEmpty()) {
            throw new MoveStrategyException("All Fields were found");
        }
        return aimNode.get();
    }
}
