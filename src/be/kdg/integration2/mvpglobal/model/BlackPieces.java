package be.kdg.integration2.mvpglobal.model;

/**
 * <p>
 * This class initializes a predefined number of black game pieces of all three sizes
 * (small, medium, large) and stores them in a {@link Tile}
 * that does not correspond to a board coordinate (-1, -1).
 * <p>
 */
public class BlackPieces {

    private final Tile blackPieces;

    public BlackPieces() {
        blackPieces = new Tile(-1, -1);
        setUpPieces();
    }

    /**
     * The method loops through a 3x5 grid and assigns pieces based on the row index.
     */
    private void setUpPieces() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0) {
                    blackPieces.addPiece(new Piece(PieceSize.SMALL, ElementColors.BLACK));
                } else if (i == 1) {
                    blackPieces.addPiece(new Piece(PieceSize.MEDIUM, ElementColors.BLACK));
                } else {
                    blackPieces.addPiece(new Piece(PieceSize.LARGE, ElementColors.BLACK));
                }
            }
        }
    }

    public Tile getBlackPieces() {
        return blackPieces;
    }
}
