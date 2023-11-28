package model.data;

import exceptions.FieldException;
import exceptions.Notification;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

public class Field {
    private final int positionX;
    private final int positionY;
    private final Terrain terrain;
    private PlayerPositionState playerPositionState;
    private final TreasureState treasureState;
    private FortState fortState;
    private boolean visited = false;

    public Field(int positionX,
                 int positionY,
                 Terrain terrain,
                 PlayerPositionState playerPositionState,
                 TreasureState treasureState,
                 FortState fortState) {

        Notification notification = new Notification();

        validate(positionX, positionY, terrain, playerPositionState, treasureState, fortState, notification);

        if (notification.hasErrors()) {
            throw new FieldException(notification.getErrors());
        }

        this.positionX = positionX;
        this.positionY = positionY;
        this.terrain = terrain;
        this.playerPositionState = playerPositionState;
        this.treasureState = treasureState;
        this.fortState = fortState;
    }

    public Field(Field field) {
        this.positionX = field.positionX;
        this.positionY = field.positionY;
        this.terrain = field.terrain;
        this.playerPositionState = field.playerPositionState;
        this.treasureState = field.treasureState;
        this.fortState = field.fortState;
        this.visited = field.visited;
    }

    private void validate(int positionX,
                          int positionY,
                          Terrain terrain,
                          PlayerPositionState playerPositionState,
                          TreasureState treasureState,
                          FortState fortState, Notification notification) {

        if(positionX < 0 || positionY < 0) {
            notification.addError("Position of Field cannot have negative number");
        }
        if(terrain == null) {
            notification.addError("Terrain of Field cannot be NULL");
        }
        if(playerPositionState == null) {
            notification.addError("PlayerPositionState of Field cannot be NULL");
        }
        if(treasureState == null) {
            notification.addError("TreasureState of Field cannot be NULL");
        }
        if(fortState == null) {
            notification.addError("FortState of Field cannot be NULL");
        }

    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public PlayerPositionState getPlayerPositionState() {
        return playerPositionState;
    }

    public TreasureState getTreasureState() {
        return treasureState;
    }

    public FortState getFortState() {
        return fortState;
    }

    public void setPlayerPositionState(PlayerPositionState playerPositionState) {
        this.playerPositionState = playerPositionState;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setFortState(FortState fortState) {
        this.fortState = fortState;
    }
}
