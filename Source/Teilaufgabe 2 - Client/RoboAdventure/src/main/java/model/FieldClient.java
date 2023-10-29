package model;

public class FieldClient extends Field{
    private TreasureState treasureState;
    private FortState fortState;

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

    // Creating Copy Contructor for Deep Copy
    public FieldClient(Field field) {
        super(field.getPositionX(), field.getPositionY(), field.getTerrain(), field.getPlayerPositionState());

        // Default State
        this.treasureState =  TreasureState.UnknownTreasure;
        this.fortState = FortState.NoFort;
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
