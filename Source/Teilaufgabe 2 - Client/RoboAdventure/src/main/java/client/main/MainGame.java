package client.main;

import controller.ClientController;
import model.data.ClientData;
import model.data.Field;
import model.data.GameMap;
import model.data.Terrain;
import model.generator.GameMapGenerator;
import model.state.FortState;
import view.GameStateView;

import static java.lang.Thread.sleep;

public class MainGame {
    public static void main(String[] args) throws InterruptedException {
        // Getting URL and GameID from user input;
       /* String serverBaseUrl = args[1];
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

        ClientController controller2 = new ClientController(serverBaseUrl, gameId, clientData2);

        Thread t1 = new Thread(() -> {
            controller.play();
        });

        Thread t2 = new Thread(() -> {
            controller2.play();
        });

        t1.start();
        t2.start();*/



        GameMapGenerator mapGenerator = new GameMapGenerator();
        mapGenerator.generateRandomMap(5,10);

        GameMap map = new GameMap(mapGenerator.getMap());

        GameStateView gameStateView = new GameStateView();
        gameStateView.updateMap(map);




    }
}
