package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactValues;

import java.util.List;
import java.util.Random;

public class MakeRandomMove extends Rule {
    @Override
    public boolean conditionRule(FactHandler facts) {
        return facts.factAvailable(FactValues.MAKE_RANDOM_MOVE);
    }

    @Override
    public boolean actionRule(FactHandler facts, GameSession game) {
        List<List<Integer>> candidateMoves = game.getAllValidMoves();

        if (candidateMoves.isEmpty()) return false;

        Random random = new Random();
        List<Integer> move = candidateMoves.get(random.nextInt(candidateMoves.size()));

        int fromRow = move.get(0);
        int fromCol = move.get(1);
        int toRow = move.get(2);
        int toCol = move.get(3);

        Tile fromTile = game.getBoard().getTile(fromRow, fromCol);
        Tile toTile = game.getBoard().getTile(toRow, toCol);

        game.movePiece(fromTile, toTile);
        return true;
    }

}