package strategy;

import model.data.Terrain;
import move.Node;

import java.util.List;

public class RightCornerUp implements MoveStrategy{
    @Override
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList) {

        Node nodeToReturn = nodeList
                .stream()
                .filter(node -> node.field.getPositionX() == axisX.end()
                        && node.field.getPositionY() == axisY.start() )
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
