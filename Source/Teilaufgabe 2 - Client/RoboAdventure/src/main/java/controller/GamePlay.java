package controller;

import model.Field;
import model.GameMap;
import java.util.*;

public class GamePlay {
    private GameMap gameMap;
    private final GameMap clientHalfMap = new GameMap();
    private Boolean treasureFound;

    public GamePlay() {
    }

    public void generateClientHalfMap() {
        clientHalfMap.generateMap();

        while (!clientHalfMap.validateMap()) {
            clientHalfMap.generateMap();
        }

    }

    public List<Field> getClientHalfMap() {
        return clientHalfMap.getMap();
    }
}

