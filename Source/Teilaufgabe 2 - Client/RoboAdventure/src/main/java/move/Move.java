package move;

import model.data.Field;
import model.state.PlayerPositionState;

import java.util.LinkedList;
import java.util.List;

public class Move {
    List<Node> nodeList = new LinkedList<>();

    public Move(List<Field> map) {
        map.forEach(field -> this.nodeList.add(new Node(field)));
        nodeList.forEach(node -> node.addAdjacentNodes(nodeList));

        // Setting source node to be Home
        Node sourceNode = nodeList.stream()
                .filter(node -> node.field.getPlayerPositionState() == PlayerPositionState.ME)
                .findFirst().orElseThrow();

    }




}
