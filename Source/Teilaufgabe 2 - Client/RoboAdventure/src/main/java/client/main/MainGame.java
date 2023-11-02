package client.main;

import controller.ClientController;
import model.data.ClientData;
import model.data.Field;
import model.data.GameMap;
import model.data.Terrain;
import model.state.FortState;

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

        ClientController controller2 = new ClientController(serverBaseUrl, gameId, clientData2);

        Thread t1 = new Thread(() -> {
            controller.play();
        });

        Thread t2 = new Thread(() -> {
            controller2.play();
        });

        t1.start();
        t2.start();



        /*ClientController controller = new ClientController(serverBaseUrl, gameId, clientData);
        GameMap map = new GameMap(controller.generateHalfMap());

        int i = 0;
        System.out.println();
        for (Field field : map.getMap()) {

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
                   if (field.getFortState() == FortState.MyFort) {
                       System.out.print(Character.toString(0x1F3F0) + "");
                   } else {
                       System.out.print(Character.toString(0x1F33F) + "");
                   }
           }

           ++i;
         }*/

    }
}
