package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactValues;

public class SelectMid extends Rule {

    @Override
    public boolean conditionRule(FactHandler facts) {
        return facts.factAvailable(FactValues.SELECT_MEDIUM);
    }

    @Override
    public boolean actionRule(FactHandler facts, GameSession game) {
        for (int row = 0; row < game.getBoard().getSize(); row++) {
            for (int col = 0; col < game.getBoard().getSize(); col++) {
                Tile currentTile = game.getBoard().getTile(row, col);

                if (game.checkForOpenLargeWithReachableMid(currentTile)) {
                    game.setSelectedTile(currentTile);
                    return true;
                }

            }
        }
        return false;
    }


}

