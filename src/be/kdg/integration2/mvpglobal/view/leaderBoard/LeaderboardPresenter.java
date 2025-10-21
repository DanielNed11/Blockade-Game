package be.kdg.integration2.mvpglobal.view.leaderBoard;

import be.kdg.integration2.mvpglobal.model.DBFunctions;
import be.kdg.integration2.mvpglobal.model.analytics.LeaderboardEntry;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.main.MainPresenter;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardPresenter {

    private final MainPresenter mainPresenter;
    private final LeaderboardView view;

    public LeaderboardPresenter(LeaderboardView view, MainPresenter mainPresenter, Stage stage) {
        this.view = view;
        this.mainPresenter = mainPresenter;

        addEventHandlers();
    }

    private void addEventHandlers() {
        try {
            List<LeaderboardEntry> leaderboard = DBFunctions.leaderboard();
            view.setLeaderboardData(leaderboard);
        } catch (Exception e) {
            CustomAlerts.alert
                    ("Leaderboard", "Error",
                    e.getMessage(), Alert.AlertType.ERROR);
        }

        view.getExitButton().setOnAction(event -> mainPresenter.closeContentPane());
    }

}
