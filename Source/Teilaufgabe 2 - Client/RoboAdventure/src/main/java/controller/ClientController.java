package controller;

import converter.ClientConverter;
import converter.IClientConverter;
import converter.IServerConverter;
import converter.ServerConverter;
import model.data.ClientData;
import model.data.GameID;
import model.generator.BalancedTerrainDistributionLogic;
import model.generator.BusinessLogicInterface;
import model.generator.GameMapGenerator;
import model.state.ClientState;
import model.validator.BusinessRules;
import model.validator.MapValidator;
import model.validator.ServerBusinessRules;
import network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.GameView;

import java.net.URL;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final GameFacade gameFacade;

    public ClientController(URL serverBaseURL, GameID gameID, ClientData clientData) {
        int ALLOWED_GAME_ACTIONS = 160;

        IClientConverter clientConverter = new ClientConverter();
        IServerConverter serverConverter = new ServerConverter();

        NetworkCommunication networkCommunication =
                new NetworkCommunication(
                        serverBaseURL,
                        gameID,
                        clientData,
                        clientConverter,
                        serverConverter);

        Game game = new Game();
        GameView gameView = new GameView();

        BusinessLogicInterface mapBusinessLogic = new BalancedTerrainDistributionLogic();
        GameMapGenerator gameMapGenerator = new GameMapGenerator(mapBusinessLogic);

        BusinessRules mapValidatorBusinessRules = new ServerBusinessRules();
        MapValidator mapValidator = new MapValidator(mapValidatorBusinessRules);

        this.gameFacade =
                new GameFacade(
                        ALLOWED_GAME_ACTIONS,
                        networkCommunication,
                        game,
                        gameView,
                        gameMapGenerator,
                        mapValidator);
    }

    public void playFacade() {
        gameFacade.startGame();
        gameFacade.playGame();
        handleGameEnd();
    }

    private void handleGameEnd() {
        ClientState clientState = gameFacade.getGameState();
        switch (clientState) {
            case Won:
                System.out.println("Congratulations! You've won the game.");
                break;
            case Lost:
                System.out.println("Game over. Better luck next time!");
                break;
            default:
                System.out.println("The game ended unexpectedly.");
                break;
        }
    }

}
