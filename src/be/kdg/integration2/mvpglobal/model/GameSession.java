package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.strategy.RuleBasedStrategy;

import java.sql.SQLException;
import java.util.*;

/**
 * Represents the central model for managing the state and rules of an ongoing game session.
 * <p>
 * This class manages player turns, game board state, rule validation, dice rolling, undo logic,
 * AI decision-making, and win condition checking. It separates core game logic from any
 * UI or presentation layer, conforming to the MVP pattern.
 */
public class GameSession {

    private final ComputerPlayer computer;
    private final HumanPlayer player;
    private Board board;
    private final GameRules rules;
    private Player currentPlayer;
    private boolean gameOver = false;
    private Player winner;
    private Piece selectedPiece;
    private int selectedRow;
    private int selectedCol;
    private Tile selectedTile;
    private final boolean isConnected;
    private int moveNumber;
    private int gameID;
    private long previousMoveTime = 0L;

    private Dice dice;
    private Dice dice1;

    public Dice getDie1() {
        return dice;
    }

    public Dice getDie2() {
        return dice1;
    }

    /**
     * Constructs a new game session, initializes the board, rules, players,
     * and determines which player goes first. Also rolls the dice for the starting player.
     *
     * @param colorOne  the first color controlled by the player
     * @param colorTwo  the second color controlled by the player
     */
    public GameSession(HumanPlayer player, ElementColors colorOne, ElementColors colorTwo, boolean isConnected) {
        this.board = new Board();
        this.rules = new GameRules();
        this.player = player;
        this.player.setControlledColorOne(colorOne);
        this.player.setControlledColorTwo(colorTwo);
        if (colorOne == ElementColors.RED && colorTwo == ElementColors.YELLOW) {
            this.computer = new ComputerPlayer();
            this.computer.setControlledColorOne(ElementColors.GREEN);
            this.computer.setControlledColorTwo(ElementColors.BLUE);
        } else {
            this.computer = new ComputerPlayer();
            this.computer.setControlledColorOne(ElementColors.YELLOW);
            this.computer.setControlledColorTwo(ElementColors.RED);
        }
        boolean isPlayerHot = player.getControlledColorOne() == ElementColors.RED ||
                player.getControlledColorTwo() == ElementColors.RED;
        this.currentPlayer = isPlayerHot ? player : computer;
        this.isConnected = isConnected;
        if (isConnected) {
            try {
                gameID = DBFunctions.beginGame(player.getPlayerID());
            } catch (SQLException e) {
                throw new RuntimeException("Error while creating game", e);
            }
        }
        rollDice();
    }

    /**
     * Executes a piece movement from one tile to another if the move is legal.
     * Updates movement points, records move history for undo, and checks for win condition.
     *
     * @param fromTile the tile from which to move
     * @param toTile   the destination tile
     * @return true if move was successful
     * @throws IllegalArgumentException if the move is invalid
     */
    public boolean movePiece(Tile fromTile, Tile toTile) {
        int fromRow = fromTile.getRow();
        int fromCol = fromTile.getCol();
        int toRow = toTile.getRow();
        int toCol = toTile.getCol();

        if (!rules.legalMoves(board, fromRow, fromCol, toRow, toCol)) {
            throw new IllegalArgumentException("Sorry this move is not possible");
        }

        Piece piece = board.getTile(fromRow, fromCol).getTopPiece();
        rules.getMoves().push(new GameState(board, rules.getPoints(), rules.getBlackPieces()));
        rules.getPoints().usePoint(piece.getColor(), rules.getTakingPoints());

        setProperHeight(piece, board.getTile(toRow, toCol));
        board.getTile(fromRow, fromCol).removePiece();
        board.getTile(toRow, toCol).addPiece(piece);

        makeBlockades(board);

        moveNumber++;

        if (isConnected) {
            long now = System.currentTimeMillis();
            int duration;
            if (moveNumber == 1) {
                duration = 0;
            } else {
                duration = (int) (now - previousMoveTime) / 1000;
            }
            previousMoveTime = now;

            try {
                DBFunctions.saveMove(
                        gameID,
                        currentPlayer instanceof HumanPlayer ? "Player" : "Computer",
                        fromRow,
                        fromCol,
                        toRow,
                        toCol,
                        moveNumber,
                        duration
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        selectedTile = null;
        checkWin();

        return true;
    }

    /**
     * Ends the current turn by resetting movement points, clearing move history,
     * swapping players, and rolling dice for the next turn.
     */
    public void endTurn() {
        rules.getPoints().reset();
        rules.clearMoves();
        swapTurn();
        rollDice();
    }

    /**
     * Rolls two dice for the current player and assigns movement points
     * based on the result. If both values are 3 or less, their sum is added
     * to both controlled colors.
     */
    private void rollDice() {
        ElementColors color1 = currentPlayer.getControlledColorOne();
        ElementColors color2 = currentPlayer.getControlledColorTwo();

        dice = new Dice(color1);
        dice1 = new Dice(color2);

        dice.rollDie();
        dice1.rollDie();

        int value1 = dice.getValue();
        int value2 = dice1.getValue();

        if (value1 <= 3 && value2 <= 3) {
            rules.getPoints().addPoints(color1, value1 + value2);
            rules.getPoints().addPoints(color2, value1 + value2);
        } else {
            rules.getPoints().addPoints(color1, value1);
            rules.getPoints().addPoints(color2, value2);
        }
    }

    private void swapTurn() {
        currentPlayer = currentPlayer == player ? computer : player;
    }

    /**
     * Handles the AI-controlled player's entire turn using a rule-based strategy.
     * AI selects and executes as many valid moves as possible, then ends its turn.
     */
    public void handleAiTurn() {
        RuleBasedStrategy strategy = new RuleBasedStrategy();

        while (rules.getPoints().getPoints().values().stream().anyMatch(p -> p > 0)) {
            strategy.makeAIMove(this);

            if (getAllValidMoves().isEmpty()) {
                break;
            }
        }
        endTurn();
    }

    /**
     * Reverts the board to the previous game state if any moves were made during the turn.
     *
     * @throws NoSuchElementException if no moves have been made yet
     */
    public void undoMove() {
        if (rules.getMoves().isEmpty()) {
            throw new NoSuchElementException("There are no previous moves");
        }

        GameState move = rules.getMoves().pop();
        this.board = move.board();
        this.rules.setMovementPoints(move.movementPoints());
        this.rules.setBlackPieces(move.blackPieces());
    }

    /**
     * Checks if a medium piece can reach a large piece of the same color,
     * across the board. Used to determine if a legal merge is possible.
     *
     * @return true if a valid target large piece exists
     */
    public boolean checkForOpenLargeAndMidFromSameColor() {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Tile currentTile = board.getTile(row, col);
                if (checkForOpenLargeWithReachableMid(currentTile)) return true;
            }
        }
        return false;
    }

    public boolean checkForOpenLargeWithReachableMid(Tile tile) {

        for (int row = 0; row < getBoard().getSize(); row++) {
            for (int col = 0; col < getBoard().getSize(); col++) {

                Tile currentTile = getBoard().getTile(row, col);

                Piece largePiece = currentTile.getTopPiece();

                Piece midPiece = tile.getTopPiece();

                if (largePiece.getSize() == PieceSize.LARGE && largePiece.getColor() != ElementColors.BLACK) {
                    if (largePiece.getColor() == midPiece.getColor()) {
                        if (getRules().legalMoves(getBoard(), tile.getRow(), tile.getCol(),
                                currentTile.getRow(), currentTile.getCol())) {
                            if (tile.getTopPiece().getSize() == PieceSize.MEDIUM) {

                                return true;
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks whether a small piece can legally move to a tile that contains a
     * large and medium piece of the same color, enabling a blockade.
     *
     * @return true if such a move is possible
     */
    public boolean checkForSmallAndStackedLargeMediumSameColor() {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Tile currentTile = board.getTile(row, col);
                if (canBlockade(currentTile)) return true;
            }
        }
        return false;
    }

    private void makeBlockades(Board board) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getTile(row, col).getPieces().size() == 3) {
                    Piece bottom = board.getTile(row, col).getPieces().get(0);
                    Piece middle = board.getTile(row, col).getPieces().get(1);
                    Piece top = board.getTile(row, col).getPieces().get(2);

                    if (bottom.getColor() == middle.getColor() && top.getColor() == middle.getColor()
                            && middle.getSize() == PieceSize.MEDIUM && top.getSize() == PieceSize.SMALL) {
                        Piece piece = rules.getBlackPieces().getBlackPieces().removePiece();
                        setProperHeight(piece, board.getTile(row, col));
                        board.getTile(row, col).addPiece(piece);
                    }
                }
            }
        }
    }

    public boolean canBlockade(Tile tile) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Tile currentTile = board.getTile(row, col);
                if (currentTile.getPieces().size() == 2) {
                    Piece large = currentTile.getPieces().get(0);
                    Piece medium = currentTile.getPieces().get(1);
                    Piece possiblySmall = tile.getTopPiece();

                    if (large.getColor() == medium.getColor() && possiblySmall.getColor() == medium.getColor()
                            && large.getSize() == PieceSize.LARGE
                            && medium.getSize() == PieceSize.MEDIUM
                            && possiblySmall.getSize() == PieceSize.SMALL) {

                        if (rules.legalMoves(board, tile.getRow(), tile.getCol(), currentTile.getRow(), currentTile.getCol())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sets the height for rendering the given piece based on the stack it's being placed on.
     * This logic is UI-supportive but remains in the model for maintaining Z-offset correctness.
     */
    private static void setProperHeight(Piece piece, Tile toTile) {
        if (!toTile.getPieces().isEmpty()) {
            double newHeight = toTile.getTopPiece().getTranslateZ();
            piece.setTranslateZ(-35 + newHeight);
        } else {
            piece.setTranslateZ(0);
        }
    }

    /**
     * Checks if any player has formed 5 tree formations with black caps.
     * If so, sets the winner and ends the game.
     */
    private void checkWin() {
        Map<ElementColors, Integer> treeCounts = new HashMap<>();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Tile tile = board.getTile(row, col);
                List<Piece> stack = tile.getPieces();

                if (stack.size() >= 2) {
                    Piece top = stack.getLast();
                    Piece belowTop = stack.get(stack.size() - 2);

                    if (top.getColor() == ElementColors.BLACK) {
                        ElementColors baseColor = belowTop.getColor();
                        treeCounts.put(baseColor, treeCounts.getOrDefault(baseColor, 0) + 1);
                    }
                }
            }
        }

        for (Map.Entry<ElementColors, Integer> entry : treeCounts.entrySet()) {
            if (entry.getValue() == 5) {
                ElementColors color = entry.getKey();
                winner = (player.getControlledColorOne() == color || player.getControlledColorTwo() == color)
                        ? player : computer;

                if (isConnected) {
                    try {
                        String winnerName = winner == player ? player.getName() : computer.getName();
                        DBFunctions.saveEndGame(gameID, winnerName, moveNumber);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error ending game: " + e.getMessage());
                    }
                }
                gameOver = true;
            }
        }
    }

    public List<List<Integer>> getAllValidMoves() {
        List<List<Integer>> validMoves = new ArrayList<>();

        Player currentPlayer = getCurrentPlayer();
        ElementColors color1 = currentPlayer.getControlledColorOne();
        ElementColors color2 = currentPlayer.getControlledColorTwo();

        Board board = getBoard();
        GameRules rules = getRules();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Tile fromTile = board.getTile(row, col);
                Piece topPiece = fromTile.getTopPiece();

                if (topPiece == null) continue;

                if (topPiece.getColor() == color1 || topPiece.getColor() == color2) {
                    for (int row2 = 0; row2 < board.getSize(); row2++) {
                        for (int col2 = 0; col2 < board.getSize(); col2++) {

                            if (rules.legalMoves(board, row, col, row2, col2)) {
                                validMoves.add(List.of(row, col, row2, col2));
                            }
                        }
                    }
                }
            }
        }

        return validMoves;
    }


    public Tile getSelectedTile() { return selectedTile; }
    public void setSelectedTile(Tile selectedTile) { this.selectedTile = selectedTile; }
    public Board getBoard() { return board; }
    public GameRules getRules() { return rules; }
    public HumanPlayer getPlayer() { return player; }
    public ComputerPlayer getComputer() { return computer; }
    public ElementColors getAIColorOne() { return computer.getControlledColorOne(); }
    public ElementColors getAIColorTwo() { return computer.getControlledColorTwo(); }
    public Player getCurrentPlayer() { return currentPlayer; }
    public void setSelectedPiece(Piece selectedPiece) { this.selectedPiece = selectedPiece; }
    public int getSelectedRow() { return selectedRow; }
    public void setSelectedRow(int selectedRow) { this.selectedRow = selectedRow; }
    public int getSelectedCol() { return selectedCol; }
    public void setSelectedCol(int selectedCol) { this.selectedCol = selectedCol; }
    public boolean isSelectedPieceNull() { return selectedPiece == null; }
    public Player getWinner() { return winner; }
    public boolean isGameOver() { return gameOver; }
    public int getGameID() { return gameID; }
    public boolean isConnected() { return isConnected; }
}
