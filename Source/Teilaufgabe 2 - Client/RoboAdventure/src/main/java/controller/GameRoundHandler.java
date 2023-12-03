package controller;

import exceptions.NoFoundException;
import move.Move;
import move.NextFieldFinder;
import move.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.MoveStrategy;
import strategy.VisitNearestGrassToKI;
import strategy.VisitedNearestMountainToKI;

public class GameRoundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameRoundHandler.class);
    MoveStrategy visitAllMountains = new VisitedNearestMountainToKI();
    MoveStrategy visitAllGrass = new VisitNearestGrassToKI();
    Move move;
    NextFieldFinder nextFieldFinder;
    Game game;

    public GameRoundHandler(Game game, Move move, NextFieldFinder nextFieldFinder) {
        this.game = game;
        this.move = move;
        this.nextFieldFinder = nextFieldFinder;
    }

    public void handleNextRound() {
        Node nextFieldToCheck;

        if (game.isTreasureFound() && !game.isCollectedTreasure()) {
            logger.debug("Treasure has been found but not collected, setting next move towards the treasure");
            Node treasureNode = move.findNode(game.getTreasureField());

            logger.debug("Position of Treasure {}{}.", treasureNode.getField().getPositionX(), treasureNode.getField().getPositionY());
            move.setMovesToTargetField(treasureNode);

        } else if (game.isCollectedTreasure() && game.isFortFound()) {
            logger.debug("Treasure is collected and Fort has been found!");
            Node fortNode = move.findNode(game.getFortField());

            logger.debug("Position of Fort {}{}.", fortNode.getField().getPositionX(), fortNode.getField().getPositionY());
            move.setMovesToTargetField(fortNode);

        } else {
            logger.debug("Treasure is collected: {}", game.isCollectedTreasure());

            try {
                nextFieldToCheck = nextFieldFinder.getNextFieldToCheck(visitAllMountains, game.isCollectedTreasure());
                move.setMovesToTargetField(nextFieldToCheck);
            } catch (NoFoundException e) {
                logger.info(e.getMessage());
                nextFieldToCheck = nextFieldFinder.getNextFieldToCheck(visitAllGrass, game.isCollectedTreasure());
                move.setMovesToTargetField(nextFieldToCheck);
            }
        }

    }
}
