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


    public void updateMap(List<Field> updatedMap) {
        updatedMap.sort(new FieldCompare());

        GameMap beforeChange = this.map;
        this.map = new GameMap(updatedMap);

        Optional<Field> foundTreasure = updatedMap.stream()
                .filter(field -> field.getTreasureState() == TreasureState.GoalTreasure)
                .findFirst();

        if(foundTreasure.isPresent()) {
            treasureFound = true;
            treasureField = foundTreasure.get();
        }

        Optional<Field> foundFort = updatedMap.stream()
                .filter(field -> field.getFortState() == FortState.EnemyFort)
                .findFirst();

        if(foundFort.isPresent()) {
            fortFound = true;
            fortField = foundFort.get();
        }

        setFieldsToVisible(updatedMap);
        setPlayerPositionAsVisited(updatedMap);

        //inform all interested parties about changes
        changes.firePropertyChange("map", beforeChange, map);
    }

    public void setFieldsToVisible(List<Field> updatedMap) {
        for(Field field: visitedFields) {
            Optional<Field> setField = updatedMap.stream()
                    .filter(f -> f.getPositionX() == field.getPositionX() && f.getPositionY() == field.getPositionY())
                    .findFirst();

            setField.ifPresent(value -> value.setVisited(true));
        }
    }

    public void setPlayerPositionAsVisited(List<Field> updatedMap) {
        Optional<Field> currentPlayerPosition = updatedMap.stream()
                .filter(field -> field.getPlayerPositionState() == PlayerPositionState.ME).findFirst();

        if(currentPlayerPosition.isPresent()) {
            currentPlayerPosition.get().setVisited(true);
            visitedFields.add(currentPlayerPosition.get());
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public GameMap getMap() {
        return map;
    }

    public List<Field> getListOfFields() {
        return map.getMap();
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

