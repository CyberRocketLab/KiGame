package client.controller;

import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.data.GameMap;
import client.model.state.*;
import client.model.data.FieldCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private GameMap map;
    private GameState gameState;
    private Boolean treasureFound = false;
    private Boolean fortFound = false;
    private Field treasureField;
    private Field fortField;
    private List<Field> visitedFields = new ArrayList<>();

    public void updateGameState(GameState gameState) {
        logger.info("Updating GameState of Game");
        if (gameState == null) {
            logger.error("Provided GameState was null");
            throw new NullOrEmptyParameterException();
        }

        this.gameState = gameState;
        updateMap(gameState.getUpdatedMap());
    }

    public ClientState getClientState() {
        return gameState.getClientState();
    }

    public boolean isCollectedTreasure() {
        return gameState.isCollectedTreasure();
    }


    public void updateMap(List<Field> updatedMap) {
        logger.info("Updating Map of Game");
        if (updatedMap == null) {
            logger.error("Provided List<Field> was null");
            throw new NullOrEmptyParameterException();
        }

        updatedMap.sort(new FieldCompare());

        GameMap beforeChange = this.map;
        this.map = new GameMap(updatedMap);

        Optional<Field> foundTreasure = updatedMap.stream()
                .filter(field -> field.getTreasureState() == TreasureState.GoalTreasure)
                .findFirst();

        if (foundTreasure.isPresent()) {
            treasureFound = true;
            treasureField = foundTreasure.get();
        }

        Optional<Field> foundFort = updatedMap.stream()
                .filter(field -> field.getFortState() == FortState.EnemyFort)
                .findFirst();

        if (foundFort.isPresent()) {
            fortFound = true;
            fortField = foundFort.get();
        }

        setFieldsToVisible(updatedMap);
        setPlayerPositionAsVisited(updatedMap);

        //inform all interested parties about changes
        changes.firePropertyChange("map", beforeChange, map);
    }

    private void setFieldsToVisible(List<Field> updatedMap) {
        for (Field field : visitedFields) {
            Optional<Field> setField = updatedMap.stream()
                    .filter(f -> f.getPositionX() == field.getPositionX() && f.getPositionY() == field.getPositionY())
                    .findFirst();

            setField.ifPresent(value -> value.setVisited(true));
        }
    }

    private void setPlayerPositionAsVisited(List<Field> updatedMap) {
        Optional<Field> currentPlayerPosition = updatedMap.stream()
                .filter(field -> field.getPlayerPositionState() == PlayerPositionState.ME).findFirst();

        if (currentPlayerPosition.isPresent()) {
            currentPlayerPosition.get().setVisited(true);
            visitedFields.add(currentPlayerPosition.get());
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            logger.error("Provided PropertyChangeListener was null");
            throw new NullOrEmptyParameterException();
        }

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

