package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactValues;

public class MakeBlockade extends Rule {
    @Override
    public boolean conditionRule(FactHandler facts) {
        return facts.factAvailable(FactValues.FORM_BLOCKADE);
    }


    @Override
    public boolean actionRule(FactHandler facts, GameSession game) {

        Tile selectedTile = game.getSelectedTile();
        if (selectedTile == null) {
            return false;
        }

        for (int row = 0; row < game.getBoard().getSize(); row++) {
            for (int col = 0; col < game.getBoard().getSize(); col++) {

                Tile currentTile = game.getBoard().getTile(row, col);

                if (currentTile.getPieces().size() == 2) {
                    Piece large = currentTile.getPieces().get(0);
                    Piece medium = currentTile.getPieces().get(1);
                    Piece possiblySmall = selectedTile.getTopPiece();

                    if (large.getColor() == medium.getColor() &&
                        possiblySmall.getColor() == medium.getColor() &&
                        large.getColor() == possiblySmall.getColor()) {
                        if (large.getSize() == PieceSize.LARGE) {
                            if (medium.getSize() == PieceSize.MEDIUM) {
                                if (possiblySmall.getSize() == PieceSize.SMALL) {

                                        game.movePiece(selectedTile, currentTile);
                                        return true;

                                }
                            }
                        }
                    }
                }
            }
        }

        return false;

    }
}