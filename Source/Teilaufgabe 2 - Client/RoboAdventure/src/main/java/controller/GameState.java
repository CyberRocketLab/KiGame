package controller;

import model.data.Field;
import model.data.FieldCompare;
import model.data.GameMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class GameState {
    private GameMap map;
    private Boolean treasureFound;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public GameState() {

    }

    public void updateMap(List<Field> updatedMap) {
        Collections.sort(updatedMap, new FieldCompare());
        GameMap beforeChange = this.map;
        this.map = new GameMap(updatedMap);

        //inform all interested parties about changes
        changes.firePropertyChange("map", beforeChange, map);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        //enables to register new listeners
        changes.addPropertyChangeListener(listener);
    }


}

