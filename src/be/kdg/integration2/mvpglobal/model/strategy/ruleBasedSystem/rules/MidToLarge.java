package be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactHandler;
import be.kdg.integration2.mvpglobal.model.strategy.ruleBasedSystem.facts.FactValues;

public class MidToLarge extends Rule {

    @Override
    public boolean conditionRule(FactHandler facts) {
        return facts.factAvailable(FactValues.MOVE_MID_TO_LARGE);
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

                Piece largePiece = currentTile.getTopPiece();

                Piece midPiece = selectedTile.getTopPiece();

                if (largePiece.getSize() == PieceSize.LARGE && largePiece.getColor() != ElementColors.BLACK) {
                    if (largePiece.getColor() == midPiece.getColor()) {
                        if (game.getRules().legalMoves(game.getBoard(), selectedTile.getRow(), selectedTile.getCol(),
                                currentTile.getRow(), currentTile.getCol())) {

                            game.movePiece(selectedTile, currentTile);

                            return true;

                        }
                    }
                }
            }
        }
        return false;
    }



}
