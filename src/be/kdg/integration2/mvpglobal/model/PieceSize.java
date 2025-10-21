package be.kdg.integration2.mvpglobal.model;

/**
 * Represents the possible sizes of a game piece.
 * <p>
 * Each size is associated with a specific integer value that can be used
 * for layout, rendering, or comparison logic.
 */
public enum PieceSize {
    SMALL(25),
    MEDIUM(40),
    LARGE(50);

    private final int size;

    PieceSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
