package controller;

import model.data.Field;
import model.data.FieldCompare;
import model.data.GameMap;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class GameState {
    private GameMap map;
    private Boolean treasureFound = false;
    private Boolean fortFound = false;
    private Field treasureField;
    private Field fortField;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private List<Field> visitedFields = new ArrayList<>();

    public GameState() {

    }

    public void updateMap(List<Field> updatedMap) {
        Collections.sort(updatedMap, new FieldCompare());

        // TODO: set Field as visited here
        GameMap beforeChange = this.map;
        this.map = new GameMap(updatedMap);

        Field foundTreasure = updatedMap.stream()
                .filter(field -> field.getTreasureState() == TreasureState.GoalTreasure)
                        .findFirst().orElse(null);

        Field foundFort = updatedMap.stream()
                .filter(field -> field.getFortState() == FortState.EnemyFort)
                .findFirst().orElse(null);

        // Setting Current position as visited and adding it to the list
        setFieldsToVisible(updatedMap);

        if(foundTreasure != null) {
            treasureFound = true;
            treasureField = foundTreasure;
        }

        if(foundFort != null) {
            fortFound = true;
            fortField = foundFort;
        }

        //inform all interested parties about changes
        changes.firePropertyChange("map", beforeChange, map);
    }

    public void setFieldsToVisible(List<Field> updatedMap) {

        // Setting all previous discovered Fields as true
        for(Field field: visitedFields) {
            Field setField = updatedMap.stream()
                    .filter(f -> f.getPositionX() == field.getPositionX() && f.getPositionY() == field.getPositionY())
                    .findFirst().orElseThrow();

            setField.setVisited(true);
        }

        // Adding current PlayerPosition to visited Fields
        Field currentPlayerPosition = updatedMap.stream()
                .filter(field -> field.getPlayerPositionState() == PlayerPositionState.ME).findFirst().orElse(null);

        if(currentPlayerPosition != null) {
            currentPlayerPosition.setVisited(true);
            visitedFields.add(currentPlayerPosition);
        }

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

    public Field getFortField() {
        return fortField;
    }

    public Boolean isFortFound() {
        return fortFound;
    }
}

