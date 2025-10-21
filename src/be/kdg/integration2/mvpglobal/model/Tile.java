package be.kdg.integration2.mvpglobal.model;

import java.util.Stack;

/**
 * Represents a single tile (cell) on the game board.
 * <p>
 * Each tile has a fixed position (row and column) and maintains a stack of {@link Piece} objects
 * to simulate vertical stacking. The top of the stack represents the visible piece.
 * <p>
 * Tiles are used by the {@link Board} to manage piece placement and movement.
 */
public class Tile {

    private final Stack<Piece> pieces;
    private final int row;
    private final int col;

    /**
     * Constructs a {@code Tile} at the specified board coordinates.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     */
    public Tile(int row, int col) {
        pieces = new Stack<>();
        this.row = row;
        this.col = col;
    }

    /**
     * Adds a {@link Piece} to this tile by pushing it onto the internal stack.
     *
     * @param piece the piece to add
     */
    public void addPiece(Piece piece) {
        pieces.push(piece);
    }

    /**
     * Removes and returns the top piece from the stack, if present.
     *
     * @return the removed piece, or {@code null} if the tile is empty
     */
    public Piece removePiece() {
        if (!pieces.isEmpty()) {
            return pieces.pop();
        }
        return null;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /**
     * Retrieves the top piece on the tile without removing it.
     *
     * @return the top piece, or {@code null} if the tile is empty
     */
    public Piece getTopPiece() {
        if (pieces.isEmpty()) {
            return null;
        }
        return pieces.peek();
    }

    public Stack<Piece> getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "col=" + col +
                ", pieces=" + getTopPiece() +
                ", row=" + row +
                '}';
    }
}
