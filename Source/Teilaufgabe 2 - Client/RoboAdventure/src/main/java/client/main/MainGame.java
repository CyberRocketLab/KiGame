package client.main;

import client.controller.ClientController;
import client.model.data.ClientData;
import client.model.data.GameID;

import java.net.MalformedURLException;
import java.net.URL;

public class MainGame {
    public static void main(String[] args) {
        try {
            URL serverBaseUrl = new URL(args[1]);
            GameID gameID = new GameID(args[2]);
            ClientData clientData = new ClientData(
                    "Alexandr",
                    "Curanov",
                    "curanova98");

            ClientController controller = new ClientController(serverBaseUrl, gameID, clientData);
            controller.play();

        } catch (MalformedURLException e) {
            System.out.println("The provided server base URL is not valid.");
        }


    }
}
