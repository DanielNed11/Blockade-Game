package be.kdg.integration2.mvpglobal.view.endGame;

import be.kdg.integration2.mvpglobal.model.DBFunctions;
import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.analytics.EndGameEntry;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.MoveDuration.MoveDurationChartPresenter;
import be.kdg.integration2.mvpglobal.view.MoveDuration.MoveDurationChartView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EndGamePresenter {

    private final EndGameView view;
    private final Stage stage;
    private final GameSession model;

    public EndGamePresenter(EndGameView view, Stage stage, GameSession model) {
        this.stage = stage;
        this.view = view;
        this.model = model;

        updateData();
        addEventHandlers();
    }


    private void addEventHandlers() {

        view.getNext().setOnAction(event -> {
            MoveDurationChartView moveDurationChartView = new MoveDurationChartView();
            new MoveDurationChartPresenter(moveDurationChartView, stage, model);

            stage.getScene().setRoot(moveDurationChartView);
        });

    }

    private void updateData() {
        EndGameEntry endGameData = null;

        try {
            endGameData = DBFunctions.getEndGameSummary(model.getGameID());
        } catch (SQLException e) {
            CustomAlerts.alert(
                    "Error",
                    e.getMessage(),
                    e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }

        if (endGameData != null) {
            view.getWinner().setText(endGameData.winner());

            int totalSeconds = endGameData.playTimeSeconds();
            String timeFormatted = totalSeconds < 60
                    ? totalSeconds + " Seconds"
                    : String.format("%d:%02d Minutes", totalSeconds / 60, totalSeconds % 60);
            view.getTotalPlayTime().setText(timeFormatted);

            view.getTurnsOfPlayer().setText(String.valueOf(endGameData.turnForPlayer()));
            view.getAvgDurationOfPlayer().setText(String.format("%.2f", endGameData.avgPlayer()));

            view.getTurnsOfComputer().setText(String.valueOf(endGameData.turnForComputer()));
            view.getAvgDurationOfComputer().setText(String.format("%.2f", endGameData.avgComputer()));
        }
    }

}
