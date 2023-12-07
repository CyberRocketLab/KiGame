package client.view;

import client.controller.Game;
import client.model.data.Field;
import client.model.data.GameMap;
import client.model.state.FortState;
import client.model.state.PlayerPositionState;
import client.model.state.TreasureState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameView implements PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(GameView.class);

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("map".equals(evt.getPropertyName())) {
            updateMap((GameMap) evt.getNewValue());
        }
    }

    public void updateMap(GameMap newMap) {
        logger.info("Updating GameMap");
        int i = 0;

        for (Field field : newMap.getMap()) {

            if (i == (newMap.getEdgeOfX() + 1)) {
                System.out.println();
                i = 0;
            }

            switch (field.getTerrain()) {
                case WATER:
                    System.out.print(Character.toString(0x1F30A) + " ");
                    break;
                case MOUNTAIN:
                    if (field.getPlayerPositionState() == PlayerPositionState.ME) {
                        System.out.print(Character.toString(0x1F604) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.ENEMY) {
                        System.out.print(Character.toString(0x1F4A9) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.BOTH) {
                        System.out.print(Character.toString(0x1F49A) + " ");
                    } else {
                        System.out.print(Character.toString(0x26F0) + " ");
                    }
                    break;

                case GRASS:
                    if (field.getFortState() == FortState.MyFort || field.getFortState() == FortState.EnemyFort) {
                        System.out.print(Character.toString(0x1F3F0) + " ");
                    } else if (field.getTreasureState() == TreasureState.GoalTreasure) {
                        System.out.print(Character.toString(0x1F4B0) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.ME) {
                        System.out.print(Character.toString(0x1F604) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.ENEMY) {
                        System.out.print(Character.toString(0x1F4A9) + " ");
                    } else if (field.getPlayerPositionState() == PlayerPositionState.BOTH) {
                        System.out.print(Character.toString(0x1F49A) + " ");
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
