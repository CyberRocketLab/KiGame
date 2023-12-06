package client.model.generator;

import client.model.data.Terrain;

import java.util.List;

public interface BusinessLogicInterface {
    List<Terrain> getAmountOfTerrains(int maxRows, int maxColumns);
}
