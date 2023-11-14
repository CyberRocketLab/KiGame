package strategy;

import model.data.Terrain;
import move.Node;

import java.util.List;

public class CheckUnvisitedFields implements MoveStrategy{
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {
        return nodeList.stream()
                .filter(node -> node.field.getPositionX() >= axisX.start() &&  node.field.getPositionX() <= axisX.end() &&
                        node.field.getPositionY() >= axisY.start() && node.field.getPositionY() <= axisY.end() &&
                        !node.field.isVisited() && node.field.getTerrain() == Terrain.GRASS
                )
                .findFirst().orElse(null);
    }
}
