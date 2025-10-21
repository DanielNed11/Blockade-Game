package be.kdg.integration2.mvpglobal.view.MoveDuration;


import be.kdg.integration2.mvpglobal.model.DBFunctions;
import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.loading.LoadingPresenter;
import be.kdg.integration2.mvpglobal.view.loading.LoadingView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public class MoveDurationChartPresenter {
    private final MoveDurationChartView view;
    private final Stage stage;
    private final GameSession model;

    public MoveDurationChartPresenter(MoveDurationChartView view, Stage stage, GameSession model) {
        this.view = view;
        this.stage = stage;
        this.model = model;

        loadChartData();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBackButton().setOnAction(event -> {
            LoadingView loadingView = new LoadingView();
            new LoadingPresenter(loadingView, stage);

            stage.getScene().setRoot(loadingView);

        });

    }

    private void loadChartData() {
        try {
            Map<String, List<Integer>> durations = DBFunctions.getMoveDurationsByPlayer(model.getGameID());

            List<Integer> playerOutliers = findOutliers(durations.get("Player"));
            List<Integer> computerOutliers = findOutliers(durations.get("Computer"));

            view.updateChart(durations, playerOutliers, computerOutliers);

        } catch (SQLException e) {
            CustomAlerts.alert(
                    "Error",
                    e.getMessage(),
                    e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    private List<Integer> findOutliers(List<Integer> durations) {
        if (durations == null || durations.isEmpty()) return Collections.emptyList();

        double avg = durations.stream().mapToInt(i -> i).average().orElse(0);
        double std = Math.sqrt(durations.stream()
                .mapToDouble(i -> Math.pow(i - avg, 2)).average().orElse(0));

        double threshold = avg + 2 * std;

        List<Integer> outliers = new ArrayList<>();
        for (int i = 0; i < durations.size(); i++) {
            if (durations.get(i) > threshold) {
                outliers.add(i);
            }
        }
        return outliers;
    }
}

