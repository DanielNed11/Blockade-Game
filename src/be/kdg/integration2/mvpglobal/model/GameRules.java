package be.kdg.integration2.mvpglobal.model;

import java.util.*;

/**
 * Contains the core rule logic for move validation and point calculation in the game.
 * <p>
 * This model class determines legal movements based on the current game state,
 * piece size, movement points, and the color interactions between tiles.
 * <p>
 * It also manages the reserve of black pieces and stores move history using a {@link Stack}.
 * This class plays a central role in enforcing game logic while remaining view-agnostic.
 */
public class GameRules {

    private MovementPoints movementPoints = new MovementPoints();
    private BlackPieces blackPieces = new BlackPieces();
    private Stack<GameState> moves = new Stack<>();
    private int takingPoints = 0;

    /**
     * Checks whether moving a piece from one tile to another is allowed based on current game rules.
     *
     * @param board  the current game board
     * @param fromRow the row index of the source tile
     * @param fromCol the column index of the source tile
     * @param toRow   the row index of the target tile
     * @param toCol   the column index of the target tile
     * @return {@code true} if the move is legal; {@code false} otherwise
     */
    public boolean legalMoves(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow && fromCol == toCol) return false;
        if (board.getTile(fromRow, fromCol).getTopPiece().getSize() == PieceSize.LARGE) return false;

        takingPoints = availableMoves(board.getTile(fromRow, fromCol), board.getTile(toRow, toCol));

        return takingPoints != -1;
    }

    /**
     * Determines whether a move between two tiles is allowed, and if so,
     * calculates the number of movement points required.
     *
     * @param fromTile the tile the piece is moving from
     * @param toTile   the tile the piece is moving to
     * @return number of points needed if the move is valid, otherwise {@code -1}
     */
    private int availableMoves(Tile fromTile, Tile toTile) {
        Piece fromPiece = fromTile.getTopPiece();
        Piece toPiece = toTile.getTopPiece();

        if (fromPiece.getSize() == PieceSize.LARGE) return -1;

        int availablePoints = movementPoints.getPoints(fromPiece.getColor());

        int fromRow = fromTile.getRow();
        int fromCol = fromTile.getCol();
        int toRow = toTile.getRow();
        int toCol = toTile.getCol();

        int rowDistance = Math.abs(fromRow - toRow);
        int colDistance = Math.abs(fromCol - toCol);
        int stepsNeeded = Math.max(rowDistance, colDistance);

        if (stepsNeeded > availablePoints) return -1;

        ElementColors fromColor = fromPiece.getColor();
        ElementColors toColor = toPiece.getColor();

        if (toColor == fromColor && toPiece.getSize().getSize() >= fromPiece.getSize().getSize()) {
            return stepsNeeded;
        }

        if (toColor == ElementColors.CLEAR) {
            return stepsNeeded;
        }

        if (toColor == ElementColors.BLACK) {
            if (toTile.getPieces().size() > 1) {
                Piece belowTop = toTile.getTopPiece();
                if (belowTop.getColor() == fromColor &&
                        fromPiece.getSize().getSize() < toPiece.getSize().getSize()) {
                    return stepsNeeded;
                }
            }
        }

        return -1;
    }

    int getTakingPoints() {
        return takingPoints;
    }

    public BlackPieces getBlackPieces() {
        return blackPieces;
    }

    void setMovementPoints(MovementPoints movementPoints) {
        this.movementPoints = movementPoints;
    }

    void setBlackPieces(BlackPieces blackPieces) {
        this.blackPieces = blackPieces;
    }

    Stack<GameState> getMoves() {
        return moves;
    }

    void clearMoves() {
        moves = new Stack<>();
    }

    public MovementPoints getPoints() {
        return movementPoints;
    }
}
