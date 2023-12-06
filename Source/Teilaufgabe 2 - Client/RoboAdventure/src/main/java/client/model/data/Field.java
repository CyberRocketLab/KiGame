package client.model.data;

import client.converter.ClientConverter;
import client.exceptions.FieldException;
import client.exceptions.NullOrEmptyParameterException;
import client.model.state.FortState;
import client.model.state.PlayerPositionState;
import client.model.state.TreasureState;
import client.exceptions.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.MimeTypeParseException;

public class Field {
    private static final Logger logger = LoggerFactory.getLogger(Field.class);
    private final int positionX;
    private final int positionY;
    private final Terrain terrain;
    private final TreasureState treasureState;
    private PlayerPositionState playerPositionState;
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
            logger.error("Provided Parameter was null");
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

        if (positionX < 0 || positionY < 0)
            notification.addError("Position of Field cannot have negative number");
        if (terrain == null)
            notification.addError("Terrain of Field cannot be NULL");
        if (playerPositionState == null)
            notification.addError("PlayerPositionState of Field cannot be NULL");
        if (treasureState == null)
            notification.addError("TreasureState of Field cannot be NULL");
        if (fortState == null)
            notification.addError("FortState of Field cannot be NULL");


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

    public void setPlayerPositionState(PlayerPositionState playerPositionState) {
        if (playerPositionState == null) {
            logger.error("Provided PlayerPositionState was null");
            throw new NullOrEmptyParameterException();
        }

        this.playerPositionState = playerPositionState;
    }

    public TreasureState getTreasureState() {
        return treasureState;
    }

    public FortState getFortState() {
        return fortState;
    }

    public void setFortState(FortState fortState) {
        if (fortState == null) {
            logger.error("Provided FortState was null");
            throw new NullOrEmptyParameterException();
        }

        this.fortState = fortState;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
