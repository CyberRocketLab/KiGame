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


    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public PlayerPositionState getPlayerPositionState() {
        return playerPositionState;
    }

    public void setPlayerPositionState(PlayerPositionState playerPositionState) {
        this.playerPositionState = playerPositionState;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

}
