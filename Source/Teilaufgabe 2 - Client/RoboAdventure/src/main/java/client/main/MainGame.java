package client.main;

import controller.ClientController;
import model.data.ClientData;
import model.data.Field;
import model.data.GameMap;
import model.data.Terrain;
import model.generator.GameMapGenerator;
import model.state.FortState;
import move.Graph;
import move.Node;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import view.GameStateView;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainGame {
    public static void main(String[] args) throws InterruptedException {


        // Getting URL and GameID from user input;
       String serverBaseUrl = args[1];
        String gameId = args[2];

        ClientData clientData = new ClientData(
                "Alexandr",
                "Curanov",
                "curanova98");

        ClientData clientData2 = new ClientData(
                "Valeria",
                "SecondName",
                "curanova98");

        ClientController controller = new ClientController(serverBaseUrl, gameId, clientData);

       // ClientController controller2 = new ClientController(serverBaseUrl, gameId, clientData2);

       /* Thread t1 = new Thread(() -> {
            controller.play();
        });

        Thread t2 = new Thread(() -> {
            controller2.play();
        });

        t1.start();
        t2.start();*/

        controller.play();



/*

        GameMapGenerator mapGenerator = new GameMapGenerator();
        mapGenerator.generateRandomMap(5,10);

        GameMap map = new GameMap(mapGenerator.getMap());

        GameStateView gameStateView = new GameStateView();
        gameStateView.updateMap(map);

        List<Node> nodeList = new LinkedList<>();

        for (Field field : map.getMap()) {
            nodeList.add(new Node(field));
        }

        for (Node node : nodeList) {
            node.addAdjacentNodes(nodeList);
        }

        Graph graph = new Graph();

        Graph.calculateShortestPathFromSource(nodeList.get(0));
        Node node = nodeList.get(2);
        System.out.println("Node to explore: X: " + node.field.getPositionX() + " Y:" +  node.field.getPositionY());
        System.out.println("Terrain: " + node.field.getTerrain());

       // System.out.println(node.adjacentNodes);
        for (Node pathNode: node.getShortestPathFromSource()) {
            System.out.println("Distance in Moves: " + pathNode.getDistanceInMoves());
            System.out.print("[" + pathNode.field.getPositionX() + "," + pathNode.field.getPositionY() + "] ");
            System.out.println();
        }
*/



    }
}
