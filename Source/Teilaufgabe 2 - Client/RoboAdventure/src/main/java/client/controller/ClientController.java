package client.controller;

import client.exceptions.NullOrEmptyParameterException;
import client.model.state.ClientState;
import client.converter.ClientConverter;
import client.converter.IClientConverter;
import client.converter.IServerConverter;
import client.converter.ServerConverter;
import client.model.data.ClientData;
import client.model.data.GameID;
import client.model.generator.BalancedTerrainDistributionLogic;
import client.model.generator.BusinessLogicInterface;
import client.model.generator.GameMapGenerator;
import client.model.validator.BusinessRules;
import client.model.validator.MapValidator;
import client.model.validator.ServerBusinessRules;
import client.network.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.view.GameView;

import java.net.URL;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final GameFacade gameFacade;

    public ClientController(URL serverBaseURL, GameID gameID, ClientData clientData) {
        if (serverBaseURL == null || gameID == null || clientData == null) {
            logger.error("Provided Parameters: serverBaseURL:{}, gameID:{} , clientData:{}", serverBaseURL, gameID, clientData);
            throw new NullOrEmptyParameterException();
        }

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

        TurnManager turnManager = new TurnManager(networkCommunication);

        this.gameFacade =
                new GameFacade(
                        ALLOWED_GAME_ACTIONS,
                        networkCommunication,
                        game,
                        gameView,
                        gameMapGenerator,
                        mapValidator,
                        turnManager);
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
