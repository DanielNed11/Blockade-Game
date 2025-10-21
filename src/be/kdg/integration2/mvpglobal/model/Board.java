package be.kdg.integration2.mvpglobal.model;

/**
 * 5x5 board, containing all {@link Tile} objects and
 * initializing them with their starting {@link Piece} configurations.
 * <p>
 * This class sets up all initial positions for the four players (RED, BLUE, GREEN, YELLOW),
 * and includes a central CLEAR piece to mark the middle tile.
 */
public class Board {

    private static final int SIZE = 5;
    private final Tile[][] tiles;

    /**
     * Constructs a {@code Board} and initializes all {@code Tile} objects,
     * assigning them their row and column coordinates and populating them
     * with their initial {@link Piece} stacks.
     */
    public Board() {
        tiles = new Tile[SIZE][SIZE];
        initializeBoard();
    }

    /**
     * Initializes each position on the board with a {@code Tile} instance,
     * then populates specific tiles with pre-defined {@code Piece} stacks
     * for each player color and the central tile.
     */
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
        setUpBoard();
    }

    /**
     * Populates the board with its initial configuration of pieces:
     * - Each Piece color (BLUE, RED, YELLOW, GREEN) starts with a fixed set of pieces
     *   in defined positions across the board.
     * - A central CLEAR tile is created to mark the board center.
     * <p>
     */
    private void setUpBoard() {
        // Center tile
        tiles[2][2].addPiece(new Piece(PieceSize.LARGE, ElementColors.CLEAR).setTranslateZ(0));

        // === BLUE PIECES SETUP === //
        tiles[4][4].addPiece(new Piece(PieceSize.LARGE, ElementColors.CLEAR).setTranslateZ(0));
        for (int i = 1; i < 6; i++) {
            tiles[4][4].addPiece(new Piece(PieceSize.MEDIUM, ElementColors.BLUE).setTranslateZ(getStackHeight() * i));
        }
        for (int i = 1; i <= 3; i++) {
            tiles[0][i].addPiece(new Piece(PieceSize.LARGE, ElementColors.BLUE).setTranslateZ(0));
            tiles[0][i].addPiece(new Piece(PieceSize.SMALL, ElementColors.BLUE).setTranslateZ(getStackHeight()));
        }
        for (int i = 1; i <= 2; i++) {
            tiles[i][3].addPiece(new Piece(PieceSize.LARGE, ElementColors.BLUE).setTranslateZ(0));
            tiles[i][3].addPiece(new Piece(PieceSize.SMALL, ElementColors.BLUE).setTranslateZ(getStackHeight()));
        }

        // === RED PIECES SETUP === //
        tiles[0][4].addPiece(new Piece(PieceSize.LARGE, ElementColors.CLEAR).setTranslateZ(0));
        for (int i = 1; i < 6; i++) {
            tiles[0][4].addPiece(new Piece(PieceSize.MEDIUM, ElementColors.RED).setTranslateZ(getStackHeight() * i));
        }
        for (int i = 0; i <= 2; i++) {
            tiles[1][i].addPiece(new Piece(PieceSize.LARGE, ElementColors.RED).setTranslateZ(0));
            tiles[1][i].addPiece(new Piece(PieceSize.SMALL, ElementColors.RED).setTranslateZ(getStackHeight()));
        }
        for (int i = 2; i <= 3; i++) {
            tiles[i][0].addPiece(new Piece(PieceSize.LARGE, ElementColors.RED).setTranslateZ(0));
            tiles[i][0].addPiece(new Piece(PieceSize.SMALL, ElementColors.RED).setTranslateZ(getStackHeight()));
        }

        // === YELLOW PIECES SETUP === //
        tiles[4][0].addPiece(new Piece(PieceSize.LARGE, ElementColors.CLEAR).setTranslateZ(0));
        for (int i = 1; i < 6; i++) {
            tiles[4][0].addPiece(new Piece(PieceSize.MEDIUM, ElementColors.YELLOW).setTranslateZ(getStackHeight() * i));
        }
        for (int i = 1; i <= 3; i++) {
            tiles[i][4].addPiece(new Piece(PieceSize.LARGE, ElementColors.YELLOW).setTranslateZ(0));
            tiles[i][4].addPiece(new Piece(PieceSize.SMALL, ElementColors.YELLOW).setTranslateZ(getStackHeight()));
        }
        for (int i = 2; i <= 3; i++) {
            tiles[3][i].addPiece(new Piece(PieceSize.LARGE, ElementColors.YELLOW).setTranslateZ(0));
            tiles[3][i].addPiece(new Piece(PieceSize.SMALL, ElementColors.YELLOW).setTranslateZ(getStackHeight()));
        }

        // === GREEN PIECES SETUP === //
        tiles[0][0].addPiece(new Piece(PieceSize.LARGE, ElementColors.CLEAR).setTranslateZ(0));
        for (int i = 1; i < 6; i++) {
            tiles[0][0].addPiece(new Piece(PieceSize.MEDIUM, ElementColors.GREEN).setTranslateZ(getStackHeight() * i));
        }
        for (int i = 2; i <= 4; i++) {
            tiles[i][1].addPiece(new Piece(PieceSize.LARGE, ElementColors.GREEN).setTranslateZ(0));
            tiles[i][1].addPiece(new Piece(PieceSize.SMALL, ElementColors.GREEN).setTranslateZ(getStackHeight()));
        }
        for (int i = 2; i <= 3; i++) {
            tiles[4][i].addPiece(new Piece(PieceSize.LARGE, ElementColors.GREEN).setTranslateZ(0));
            tiles[4][i].addPiece(new Piece(PieceSize.SMALL, ElementColors.GREEN).setTranslateZ(getStackHeight()));
        }
    }

    /**
     * Returns the height offset used to simulate 3D stacking of pieces.
     * This value affects the {@code translateZ} used in JavaFX rendering.
     *
     * @return the stacking offset for pieces (typically a negative value)
     */
    public int getStackHeight() {
        return -35;
    }

    public int getSize() {
        return SIZE;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }
}
