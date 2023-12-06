package client.converter;

import client.controller.TurnManager;
import client.exceptions.NullOrEmptyParameterException;
import client.model.data.Field;
import client.model.data.Terrain;
import client.model.state.ClientState;
import client.model.state.FortState;
import client.model.state.PlayerPositionState;
import client.model.state.TreasureState;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ClientConverter implements IClientConverter {
    private static final Logger logger = LoggerFactory.getLogger(ClientConverter.class);

    @Override
    public List<Field> getConvertedMap(FullMap serverFullMap) {
        if (serverFullMap == null) {
            logger.error("Provided FullMap was null");
            throw new NullOrEmptyParameterException();
        }

        List<Field> convertedMap = new ArrayList<>();

        serverFullMap.stream().forEach(
                node ->
                        convertedMap.add(
                                new Field(
                                        node.getX(),
                                        node.getY(),
                                        getConvertedTerrain(node.getTerrain()),
                                        getConvertedPlayerPositionState(node.getPlayerPositionState()),
                                        getConvertedTreasureState(node.getTreasureState()),
                                        getConvertedFortState(node.getFortState())
                                )
                        )
        );

        return convertedMap;
    }

    @Override
    public ClientState getConvertedClientState(EPlayerGameState ePlayerGameState) {
        if (ePlayerGameState == null) {
            logger.error("Provided EPlayerGameState was null");
            throw new NullOrEmptyParameterException();
        }

        ClientState clientState = null;

        switch (ePlayerGameState) {
            case Won -> clientState = ClientState.Won;
            case Lost -> clientState = ClientState.Lost;
            case MustAct -> clientState = ClientState.MustAct;
            case MustWait -> clientState = ClientState.MustWait;
        }
        ;
        return clientState;
    }


    private Terrain getConvertedTerrain(ETerrain eTerrain) {
        if (eTerrain == null) {
            logger.error("Provided ETerrain was null");
            throw new NullOrEmptyParameterException();
        }

        Terrain terrain = null;

        switch (eTerrain) {
            case Grass -> terrain = Terrain.GRASS;
            case Water -> terrain = Terrain.WATER;
            case Mountain -> terrain = Terrain.MOUNTAIN;
        }

        return terrain;
    }


    private PlayerPositionState getConvertedPlayerPositionState(EPlayerPositionState ePlayerPositionState) {
        if (ePlayerPositionState == null) {
            logger.error("Provided EPlayerPositionState was null");
            throw new NullOrEmptyParameterException();
        }

        PlayerPositionState playerPositionState = null;

        switch (ePlayerPositionState) {
            case NoPlayerPresent -> playerPositionState = PlayerPositionState.NOBODY;
            case MyPlayerPosition -> playerPositionState = PlayerPositionState.ME;
            case EnemyPlayerPosition -> playerPositionState = PlayerPositionState.ENEMY;
            case BothPlayerPosition -> playerPositionState = PlayerPositionState.BOTH;
        }

        return playerPositionState;
    }


    private TreasureState getConvertedTreasureState(ETreasureState eTreasureStatetreasureState) {
        if (eTreasureStatetreasureState == null) {
            logger.error("Provided ETreasureState was null");
            throw new NullOrEmptyParameterException();
        }

        TreasureState treasureState = null;

        switch (eTreasureStatetreasureState) {
            case MyTreasureIsPresent -> treasureState = TreasureState.GoalTreasure;
            case NoOrUnknownTreasureState -> treasureState = TreasureState.UnknownTreasure;
        }

        return treasureState;
    }


    private FortState getConvertedFortState(EFortState eFortStatefortState) {
        if (eFortStatefortState == null) {
            logger.error("Provided EFortState was null");
            throw new NullOrEmptyParameterException();
        }

        FortState fortState = null;

        switch (eFortStatefortState) {
            case MyFortPresent -> fortState = FortState.MyFort;
            case EnemyFortPresent -> fortState = FortState.EnemyFort;
            case NoOrUnknownFortState -> fortState = FortState.UnknownFort;
        }

        return fortState;
    }
}
