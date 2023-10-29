package model;

public class FieldClient extends Field{
    TreasureState treasureState;
    FortState fortState;

    public FieldClient(int positionX,
                       int positionY,
                       Terrain terrain,
                       PlayerPositionState playerPositionState,
                       TreasureState treasureState,
                       FortState fortState) {

        super(positionX, positionY, terrain, playerPositionState);
        this.treasureState = treasureState;
        this.fortState = fortState;
    }

    public TreasureState getTreasureState() {
        return treasureState;
    }

    public void setTreasureState(TreasureState treasureState) {
        this.treasureState = treasureState;
    }

    public FortState getFortState() {
        return fortState;
    }

    public void setFortState(FortState fortState) {
        this.fortState = fortState;
    }
}
