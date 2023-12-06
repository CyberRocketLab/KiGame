package client.controller;

import client.model.generator.BalancedTerrainDistributionLogic;
import client.model.generator.GameMapGenerator;
import client.model.state.ClientState;
import client.model.state.GameState;
import client.model.validator.MapValidator;
import client.model.validator.ServerBusinessRules;
import client.network.NetworkCommunication;
import client.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class GameFacadeTest {
    private GameFacade gameFacade;
    private NetworkCommunication networkCommunication;
    private Game game;
    private GameView gameView;
    private GameMapGenerator mapGenerator;
    private MapValidator mapValidator;
    private TurnManager turnManager;

    @BeforeEach
    public void init() {
        networkCommunication = Mockito.mock(NetworkCommunication.class);
        game = Mockito.mock(Game.class);
        gameView = Mockito.mock(GameView.class);
        mapGenerator = new GameMapGenerator(new BalancedTerrainDistributionLogic());
        mapValidator = new MapValidator(new ServerBusinessRules());
        turnManager = Mockito.mock(TurnManager.class);

        GameState gameState = Mockito.mock(GameState.class);
        when(gameState.getClientState()).thenReturn(ClientState.MustAct);
        when(networkCommunication.getGameState()).thenReturn(gameState);

        gameFacade = new GameFacade(160, networkCommunication, game, gameView, mapGenerator, mapValidator, turnManager);
    }

    @Test
    public void givenValidParameters_whenStartingGame_thenWorks() {
        gameFacade.startGame();
        verify(networkCommunication, times(1)).registerClient();
        verify(networkCommunication, times(1)).sendClientMap(anyList());
        verify(networkCommunication, times(1)).getGameState();
        verify(game, times(1)).updateGameState(any());
        verify(turnManager).waitForMyTurn();
    }

    @Test
    public void initIsValid_playingGame_shouldWorkd() {

    }


}
