package controller;

import model.data.Field;
import model.data.GameMap;

import java.beans.PropertyChangeSupport;
import java.util.*;

public class GameState {
    private GameMap map;
    private Boolean treasureFound;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public GameState(GameMap map) {
        this.map = map;
    }

    public void updateMap(List<Field> updatedMap) {

    }


}

