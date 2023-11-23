package model.data;

import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

public class Field {
    private final int positionX;
    private final int positionY;
    private Terrain terrain;
    private PlayerPositionState playerPositionState;
    private TreasureState treasureState;
    private FortState fortState;
    private boolean visited = false;

    public Field(int positionX,
                 int positionY,
                 Terrain terrain,
                 PlayerPositionState playerPositionState,
                 TreasureState treasureState,
                 FortState fortState) {

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

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
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
}
