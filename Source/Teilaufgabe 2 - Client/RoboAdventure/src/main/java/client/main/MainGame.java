package client.main;

import controller.ClientController;
import controller.Game;
import model.data.*;
import model.generator.BalancedTerrainDistributionLogic;
import model.generator.BusinessLogicInterface;
import model.generator.GameMapGenerator;
import model.validator.MapValidator;
import view.GameView;

import java.net.MalformedURLException;
import java.net.URL;
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

    private static boolean validateMap(List<Field> randomMap) {
        MapValidator mapValidator = new MapValidator();
        return mapValidator.validateMap(randomMap);
    }
}
