package client.main;

import controller.ClientController;
import model.data.*;
import model.generator.GameMapGenerator;
import model.state.FortState;
import move.Graph;
import move.Node;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import view.GameStateView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

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
