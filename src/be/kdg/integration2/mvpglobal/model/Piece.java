package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a game piece with a specific size and color.
 * Each piece has a vertical offset value ({@code translateZ}) used for 3D stack rendering in the view.
 */
public class Piece {

    private final PieceSize size;
    private final ElementColors color;
    private double translateZ;

    /**
     * Constructs a new {@code Piece} with the given size and color.
     * Automatically sets its type (HOT, COLD, BLACK) based on the color.
     *
     * @param size  the size of the piece (SMALL, MEDIUM, or LARGE)
     * @param color the color of the piece
     */
    public Piece(PieceSize size, ElementColors color) {
        this.size = size;
        this.color = color;
        this.translateZ = 0;
    }


    public PieceSize getSize() {
        return size;
    }

    public ElementColors getColor() {
        return color;
    }

    public double getTranslateZ() {
        return translateZ;
    }

    public Piece setTranslateZ(double translateZ) {
        this.translateZ = translateZ;
        return this;
    }

    @Override
    public String toString() {
        return "Piece [size=" + size + ", color=" + color + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Piece piece = (Piece) obj;
        return size == piece.size && color == piece.color;
    }
}
