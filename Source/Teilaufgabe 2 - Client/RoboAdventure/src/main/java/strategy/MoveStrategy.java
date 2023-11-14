package strategy;

import model.position.Position;
import move.Node;

import java.util.List;

public interface MoveStrategy {
    public Node getFieldWithPosition(StartAndEndOfAxis axisX, StartAndEndOfAxis axisY, List<Node> nodeList);
}
