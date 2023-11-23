package model.data;

import java.util.*;
import java.util.stream.Collectors;

public class GameMap {
    private List<Field> map = new ArrayList<>();
    private int edgeOfX;
    private int edgeOfY;

    public GameMap(List<Field> map) {
        List<Integer> position = map.stream().map(Field::getPositionX).toList();
        edgeOfX = 9;
        edgeOfY = 9;

        if(position.contains(19)) {
            edgeOfX = 19;
            edgeOfY = 4;
        }

        this.map = map;
    }

    public List<Field> getMap() {
        return map;
    }

    public int getEdgeOfX() {
        return edgeOfX;
    }

    public int getEdgeOfY() {
        return edgeOfY;
    }

}
