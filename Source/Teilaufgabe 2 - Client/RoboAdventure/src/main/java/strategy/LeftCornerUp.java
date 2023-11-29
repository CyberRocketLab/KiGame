package strategy;

import model.data.Terrain;
import move.Node;

import java.util.List;

public class LeftCornerUp implements MoveStrategy {
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        Node nodeToReturn = nodeList
                .stream()
                .filter(node -> node.getField().getPositionX() == axisX.start()
                        && node.getField().getPositionY() == axisY.start())
                .findFirst().orElseThrow();


        if (nodeToReturn.getField().getTerrain() == Terrain.WATER) {
            return getAdjacentNonWaterField(nodeToReturn);
        }

        return nodeToReturn;
    }
}
