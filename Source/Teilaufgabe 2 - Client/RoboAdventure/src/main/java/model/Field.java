package model;

public abstract class Field {
    private int positionX;
    private int positionY;

    private Terrain terrain;
    private PlayerPositionState playerPositionState;

    public Field(int positionX, int positionY, Terrain terrain, PlayerPositionState playerPositionState) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.terrain = terrain;
        this.playerPositionState = playerPositionState;
    }
}
