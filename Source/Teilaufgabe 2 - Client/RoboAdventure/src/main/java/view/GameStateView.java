package view;

import model.data.Field;
import model.data.FieldCompare;
import model.data.GameMap;
import model.state.FortState;
import model.state.PlayerPositionState;
import model.state.TreasureState;

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
        int i = 0;

        for (Field field : newMap.getMap()) {

            if (i == (newMap.getEdgeOfX() + 1)) {
                System.out.println();
                i = 0;
            }

            switch (field.getTerrain()) {
                case WATER:
                   System.out.print(Character.toString(0x1F30A) + " ");
                   //  System.out.print(" ~ ");
                    break;
                case MOUNTAIN:
                    if(field.getPlayerPositionState() == PlayerPositionState.ME ) {
                        System.out.print(Character.toString(0x1F604) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.ENEMY) {
                        System.out.print(Character.toString(0x1F4A9) + " ");
                    }else {
                        System.out.print(Character.toString(0x26F0) + " ");
                    }
                    break;

                case GRASS:
                    if (field.getFortState() == FortState.MyFort || field.getFortState() == FortState.EnemyFort) {
                        System.out.print(Character.toString(0x1F3F0) + " ");
                        //  System.out.print(" |=| ");
                    } else if(field.getTreasureState() == TreasureState.GoalTreasure) {
                        System.out.print(Character.toString(0x1F4B0) + " ");
                    } else if(field.getPlayerPositionState() == PlayerPositionState.ME) {
                        System.out.print(Character.toString(0x1F604) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.ENEMY) {
                        System.out.print(Character.toString(0x1F4A9) + " ");
                    } else {
                        System.out.print(Character.toString(0x1F33F) + " ");
                        //   System.out.print(" [] ");
                    }
                    break;
            }

            ++i;
        }
        System.out.println();
        System.out.println();
    }
}
