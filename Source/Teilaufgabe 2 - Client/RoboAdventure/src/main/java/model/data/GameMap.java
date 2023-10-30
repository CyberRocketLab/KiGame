package model.data;

import java.util.*;

public class GameMap {
    private List<Field> map = new ArrayList<>();

    public GameMap(List<Field> map) {
        this.map = map;
    }

    public List<Field> getMap() {
        return map;
    }
}
