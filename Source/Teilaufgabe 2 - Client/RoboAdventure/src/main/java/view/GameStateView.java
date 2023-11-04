package view;

import model.data.Field;
import model.data.FieldCompare;
import model.data.GameMap;
import model.state.FortState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameStateView implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("map".equals(evt.getPropertyName())) {
            updateMap((GameMap) evt.getNewValue());
        }
    }

    public void updateMap(GameMap newMap) {

        List<Integer> position = newMap.getMap().stream().map(x -> x.getPositionX()).collect(Collectors.toList());
        int maxX = 9;
        if(position.contains(19)) {
            maxX = 19;
        }

        ++maxX;
        int i = 0;
        for (Field field : newMap.getMap()) {

            if (i == maxX) {
                System.out.println();
                i = 0;
            }

          //  System.out.print("[X=" + field.getPositionX() + ",Y=" + field.getPositionY() + "]");


            switch (field.getTerrain()) {
                case WATER:
                    //System.out.print(Character.toString(0x1F30A) + " ");
                    System.out.print(new String(Character.toChars(0x1F30A)) + " ");
                    break;
                case MOUNTAIN:
                    System.out.print(Character.toString(0x26F0) + " ");
                    break;
                case GRASS:
                    if (field.getFortState() == FortState.MyFort || field.getFortState() == FortState.EnemyFort) {
                        System.out.print(Character.toString(0x1F3F0) + " ");
                    } else {
                        System.out.print(Character.toString(0x1F33F) + " ");
                    }
                    break;
            }

            ++i;
        }
        System.out.println();
        System.out.println();
    }
}
