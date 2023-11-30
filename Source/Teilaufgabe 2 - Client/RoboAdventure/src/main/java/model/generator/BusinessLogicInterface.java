package model.generator;

import model.data.Terrain;

import java.util.List;

public interface BusinessLogicInterface {
    List<Terrain> getAmountOfTerrains(int maxRows, int maxColumns);
}
