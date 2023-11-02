package model.data;

import model.state.ClientState;

public class GameState {
    ClientState clientState;

    public GameState(ClientState clientState) {
        this.clientState = clientState;
    }
}
