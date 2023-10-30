package client.main;

import controller.ClientController;
import controller.GamePlay;
import model.*;

import java.util.*;

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
                "FirstName",
                "SecondName",
                "curanova98");

        ClientController controller = new ClientController(serverBaseUrl, gameId, clientData);
        controller.registerClient();
        sleep(400);

        ClientController controller2 = new ClientController(serverBaseUrl, gameId, clientData2);
        controller2.registerClient();

        sleep(400);

        controller.sendClientMap();

        sleep(400);

        controller2.sendClientMap();



    /*   GamePlay gameMap = new GamePlay();
       gameMap.generateClientHalfMap();


      int i = 0;

      System.out.println(gameMap.getClientHalfMap().size());
     for (Field field : gameMap.getClientHalfMap()) {

           if (i == 10) {
               System.out.println(" ");
               i = 0;
           }

           if (field.getTerrain() == Terrain.WATER) {

               int x = 0x1F30A;
               System.out.print(Character.toString(x) + "");
           }

           if (field.getTerrain() == Terrain.MOUNTAIN) {

               System.out.print(Character.toString(0x26F0) + "");
           }

           if (field.getTerrain() == Terrain.GRASS) {
               if (field instanceof FieldClient) {

                   if (((FieldClient) field).getFortState() == FortState.MyFort) {
                       System.out.print(Character.toString(0x1F3F0) + "");
                   } else {
                       System.out.print(Character.toString(0x1F33F) + "");
                   }
               }

           }

           ++i;
       }
        */
    }
}
