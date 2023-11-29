package move;

import java.util.*;

public class Graph {

    public static void calculateShortestPathFromSource(Node source) {
        source.setDistanceInMoves(0);

        Set<Node> visitedNodes = new HashSet<>();
        PriorityQueue<Node> unvisitedNodes = new PriorityQueue<>(Comparator.comparingInt(Node::getDistanceInMoves));

        /*PriorityQueue<Node> unvisitedNodes = new PriorityQueue<>((node1, node2) -> {
            if (node1.getField().isVisited() && !node2.getField().isVisited()) {
                return 1;
            } else if (!node1.getField().isVisited() && node2.getField().isVisited()) {
                return -1;
            }
            return Integer.compare(node1.getDistanceInMoves(), node2.getDistanceInMoves());
        });*/

        unvisitedNodes.add(source);

        while (!unvisitedNodes.isEmpty()) {
            Node currentNode = unvisitedNodes.poll();

            for (Map.Entry<Node, Integer> adjacentNode : currentNode.getAdjacentNodes().entrySet()) {
                Node node = adjacentNode.getKey();
                int moves = adjacentNode.getValue();

                if (!visitedNodes.contains(node)) {
                    calculateMinimumDistance(node, moves, currentNode);
                    unvisitedNodes.add(node);
                }
            }

            visitedNodes.add(currentNode);
        }

    }

    private static void calculateMinimumDistance(Node nodeToEvaluate, int moves, Node sourceNode) {
       /* if (nodeToEvaluate.getField().isVisited()) {
            return;
        }
*/
        int sumDistance = moves + sourceNode.getDistanceInMoves();

        if (sumDistance < nodeToEvaluate.getDistanceInMoves()) {
            nodeToEvaluate.setDistanceInMoves(sumDistance);

            List<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPathFromSource());
            shortestPath.add(sourceNode);
            nodeToEvaluate.setShortestPathFromSource(shortestPath);
        }
    }

}
