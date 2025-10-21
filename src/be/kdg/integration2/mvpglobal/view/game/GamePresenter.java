package be.kdg.integration2.mvpglobal.view.game;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.endGame.EndGamePresenter;
import be.kdg.integration2.mvpglobal.view.endGame.EndGameView;
import be.kdg.integration2.mvpglobal.view.loading.LoadingPresenter;
import be.kdg.integration2.mvpglobal.view.loading.LoadingView;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePresenter {

    private final Stage stage;
    private final GameView view;
    private final GameSession model;

    public GamePresenter(GameView view, Stage stage, GameSession model) {
        this.model = model;
        this.view = view;
        this.stage = stage;

        rotateAnimation(view);
        updateView();

        if (model.getCurrentPlayer() == model.getComputer()) {
            model.handleAiTurn();
            updateView();
        }
    }

    private void addEventHandlers() {
        for (int row = 0; row < model.getBoard().getSize(); row++) {
            for (int col = 0; col < model.getBoard().getSize(); col++) {
                StackPane tileView = (StackPane) view.getBoard2D().getChildren().get(row * model.getBoard().getSize() + col);

                final int finalRow = row;
                final int finalCol = col;

                tileView.setOnMouseClicked(event -> {
                    if (model.isSelectedPieceNull()) {
                        model.setSelectedPiece(model.getBoard().getTile(finalRow, finalCol).getTopPiece());
                        model.setSelectedRow(finalRow);
                        model.setSelectedCol(finalCol);

                        model.setSelectedTile(model.getBoard().getTile(finalRow, finalCol));

                        if (!showLegalMovesTiles(model.getSelectedRow(), model.getSelectedCol()))
                            model.setSelectedPiece(null);
                    } else {
                        try {
                            if (model.getSelectedRow() == finalRow && model.getSelectedCol() == finalCol) {
                                model.setSelectedPiece(null);
                                hideLegalMoves();
                                return;
                            }
                            boolean moved = model.movePiece
                                    (model.getSelectedTile(), model.getBoard().getTile(finalRow, finalCol));

                            model.setSelectedPiece(null);
                            if (moved) {
                                if (model.getRules().getPoints().getPoints().values().stream().noneMatch(p -> p > 0)) {
                                    model.endTurn();
                                    model.handleAiTurn();
                                    updateView();
                                }
                                hideLegalMoves();
                                updateView();
                            }
                        } catch (Exception e) {
                            model.setSelectedPiece(null);
                            hideLegalMoves();
                            CustomAlerts.alert(
                                    "Couldn't make a move.",
                                    "Move not possible",
                                    e.getMessage(),
                                    Alert.AlertType.INFORMATION
                            );
                        }
                    }


                });
            }
        }

        view.getExitButton().setOnAction(event -> {
            LoadingView loadingView = new LoadingView();
            new LoadingPresenter(loadingView, stage);

            stage.getScene().setRoot(loadingView);
        });

        view.getUndoButton().setOnAction(event -> {
            try {
                model.undoMove();
            } catch (Exception e) {
                CustomAlerts.alert(
                        "Undo error",
                        "You have not made a move this turn.",
                        e.getMessage(),
                        Alert.AlertType.INFORMATION
                );
            }
            updateView();
        });


        view.getEndTurnButton().setOnAction(event -> {
            if (model.getCurrentPlayer() == model.getPlayer()) {
                model.endTurn();
                model.handleAiTurn();
                updateView();

            } else {
                CustomAlerts.alert(
                        "Not your turn.",
                        "Wait for your turn.",
                        "Please wait till the opponent's turn is over",
                        Alert.AlertType.INFORMATION
                );
            }
        });

    }


    private void updateView() {
        //redraw boards and black pieces

        view.setUpBlackPieces(model.getRules().getBlackPieces(), view.getBlackPiecesView());
        view.setUp2DBoard(model.getBoard(), view.getBoard2D());
        view.setUp3DBoard(model.getBoard(), view.getBoard3D());
        //redraw dice
        view.getDie1().getChildren().clear();
        view.getDie2().getChildren().clear();

        ElementColors color1 = model.getCurrentPlayer().getControlledColorOne();
        ElementColors color2 = model.getCurrentPlayer().getControlledColorTwo();

        view.getDie1().getChildren().add(view.initializeDice(color1, model.getDie1().getValue()));
        view.getDie2().getChildren().add(view.initializeDice(color2, model.getDie2().getValue()));

        int movementPoints1 = model.getRules().getPoints().getPoints(color1);
        int movementPoints2 = model.getRules().getPoints().getPoints(color2);

        view.getMovementPoints1().setText(String.format("%s: %d", color1.name(), movementPoints1));
        view.getMovementPoints2().setText(String.format("%s: %d", color2.name(), movementPoints2));

        view.getName().setText(model.getCurrentPlayer().getName());

        if (model.isGameOver()) {
            String endMessage = model.getWinner() == model.getPlayer() ? "You Won!" : "You Lost!";

            CustomAlerts.alert(
                    "Game Over",
                    "Game Over",
                    endMessage,
                    Alert.AlertType.INFORMATION
            );
            if (model.isConnected()) {
                EndGameView endGameView = new EndGameView();
                new EndGamePresenter(endGameView, stage, model);
                stage.getScene().setRoot(endGameView);
            } else {
                LoadingView loadingView = new LoadingView();
                new LoadingPresenter(loadingView, stage);
                stage.getScene().setRoot(loadingView);
            }

        }

        addEventHandlers();
    }

    private void hideLegalMoves() {
        int boardSize = model.getBoard().getSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                int index = row * boardSize + col;

                StackPane tileView = (StackPane) view.getBoard2D().getChildren().get(index);

                tileView.setStyle("");
            }
        }
    }

    private boolean showLegalMovesTiles(int fromRow, int fromCol) {
        int boardSize = model.getBoard().getSize();
        int legalMovesCount = 0;

        // Creating effect for legal Tiles
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                int index = row * boardSize + col;

                StackPane tileView = (StackPane) view.getBoard2D().getChildren().get(index);

                // if legal apply effect
                if (model.getRules().legalMoves(model.getBoard(), fromRow, fromCol, row, col)) {
                    tileView.setStyle(" -fx-background-color: transparent;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,255,0,0.8), 15, 0.3, 0, 0);" +
                            "-fx-cursor: hand;");

                    legalMovesCount++;
                }
            }
        }
        return legalMovesCount > 0;
    }

    private static void rotateAnimation(GameView view) {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            Timeline timeline = new Timeline();

            for (int i = 0; i <= 135; i++) {
                int value = i;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 5),
                        e -> view.getRotationSlider().setValue(value)
                ));
            }

            timeline.setCycleCount(1);
            timeline.play();

        });
        delay.play();
    }
}