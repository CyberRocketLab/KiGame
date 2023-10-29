package client.main;

import controller.ClientController;
import model.*;

import java.util.*;

public class MainGame {
    public static void main(String[] args) {

        /*// Getting URL and GameID from user input;
        String serverBaseUrl = args[1];
        String gameId = args[2];

        ClientData clientData = new ClientData(
                "Alexandr",
                "Curanov",
                "curanova98");

        ClientData clientData2 = new ClientData(
                "FirstName",
                "SecondName",
                "curanova98");

        ClientController controller = new ClientController(serverBaseUrl, gameId, clientData);
        controller.registerClient();

        ClientController controller2 = new ClientController(serverBaseUrl, gameId, clientData2);
        controller2.registerClient();*/

       GameMap gameMap = new GameMap();
       gameMap.generateMap();


      int i = 0;

      System.out.println(gameMap.getMap().size());
       for (Field field : gameMap.getMap()) {
           if (i == 10) {
               System.out.println(" ");
               i = 0;
           }

           if (field.getTerrain() == Terrain.WATER) {
               System.out.print("  ~   ");
           }

           if (field.getTerrain() == Terrain.MOUNTAIN) {
               System.out.print("  ^   ");
           }

           if (field.getTerrain() == Terrain.GRASS) {
               System.out.print(" ...  ");
           }

           ++i;
       }



    }
}
