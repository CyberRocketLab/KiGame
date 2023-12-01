package strategy;

import exceptions.NoFoundException;
import model.data.Terrain;
import move.Node;

import java.util.List;
import java.util.Optional;

public interface MoveStrategy {
    Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList);

    default Node getAdjacentNonWaterField(Node node) {
        Optional<Node> adjacentNode = node.getAdjacentNodes().keySet().stream()
                .filter(n -> n.getField().getTerrain() != Terrain.WATER)
                .findFirst();

        if (adjacentNode.isEmpty()) {
            throw new NoFoundException();
        }

        return adjacentNode.get();
    }
}
