package client.move;

import client.exceptions.NullOrEmptyParameterException;
import client.model.validator.ServerBusinessRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Graph {
    private static final Logger logger = LoggerFactory.getLogger(Graph.class);

    public static void calculateShortestPathFromSource(Node source) {
        if (source == null) {
            logger.error("Provided Node was null");
            throw new NullOrEmptyParameterException();
        }

        source.setDistanceInMoves(0);

        Set<Node> visitedNodes = new HashSet<>();
        PriorityQueue<Node> unvisitedNodes = new PriorityQueue<>(Comparator.comparingInt(Node::getDistanceInMoves));

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
        if (nodeToEvaluate == null || sourceNode == null || moves < 0) {
            logger.error("Provided nodeToEvaluate or sourceNode was null or moves was < 0. nodeToEvaluate: {}, moves: {}, sourceNode: {}", nodeToEvaluate, moves, sourceNode);
            throw new NullOrEmptyParameterException();
        }

        int sumDistance = moves + sourceNode.getDistanceInMoves();

        if (sumDistance < nodeToEvaluate.getDistanceInMoves()) {
            nodeToEvaluate.setDistanceInMoves(sumDistance);

            List<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPathFromSource());
            shortestPath.add(sourceNode);
            nodeToEvaluate.setShortestPathFromSource(shortestPath);
        }
    }

}
