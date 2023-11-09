package controller;

import model.data.Field;
import model.data.FieldCompare;
import model.data.GameMap;
import model.state.TreasureState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class GameState {
    private GameMap map;
    private Boolean treasureFound = false;
    private Field treasureField;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public GameState() {

    }

    public void updateMap(List<Field> updatedMap) {
        Collections.sort(updatedMap, new FieldCompare());
        GameMap beforeChange = this.map;
        this.map = new GameMap(updatedMap);

        Field found = updatedMap.stream()
                .filter(field -> field.getTreasureState() == TreasureState.GoalTreasure)
                        .findFirst().orElse(null);

        if(found != null) {
            treasureFound = true;
            treasureField = found;
        }

        //inform all interested parties about changes
        changes.firePropertyChange("map", beforeChange, map);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        //enables to register new listeners
        changes.addPropertyChangeListener(listener);
    }

    public GameMap getMap() {
        return map;
    }

    public Boolean isTreasureFound() {
        return treasureFound;
    }

    public Field getTreasureField() {
        return treasureField;
    }
}

