package be.kdg.integration2.mvpglobal.view.leaderBoard;

import be.kdg.integration2.mvpglobal.model.analytics.LeaderboardEntry;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class LeaderboardView extends StackPane {

    private final VBox leaderboardBox = new VBox(15);
    private final Button exitButton = new Button("Exit");

    public LeaderboardView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        Label titleLabel = new Label("🏆 Leaderboard");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold;");
        leaderboardBox.setAlignment(Pos.TOP_CENTER);
        leaderboardBox.setPadding(new Insets(40, 20, 20, 20));
        leaderboardBox.getChildren().add(titleLabel);

        exitButton.setStyle("""
            -fx-background-color: #3C3F41;
            -fx-text-fill: white;
            -fx-padding: 8px 16px;
            -fx-font-size: 14px;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
        """);
    }

    private void layoutNodes() {
        VBox container = new VBox(30, leaderboardBox, exitButton);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(20));
        VBox.setVgrow(leaderboardBox, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);

        // Update: Set transparent background for ScrollPane to match game background
        scrollPane.setStyle("""
        -fx-background: transparent;
        -fx-background-color: transparent;
        -fx-control-inner-background: transparent;
        -fx-box-border: transparent;
        -fx-viewport-border: transparent;
        -fx-faint-focus-color: transparent;
    """);


        container.setStyle("""
        -fx-background-color: transparent;
    """);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        this.setStyle("-fx-background-color: #2b2b2b;");
        this.getChildren().add(scrollPane);
    }

    void setLeaderboardData(List<LeaderboardEntry> data) {
        leaderboardBox.getChildren().removeIf(node -> node instanceof VBox);

        List<LeaderboardEntry> top5 = data.stream()
                .sorted((a, b) -> Double.compare(b.winLossRatio(), a.winLossRatio()))
                .limit(8)
                .toList();

        for (int i = 0; i < top5.size(); i++) {
            LeaderboardEntry entry = top5.get(i);
            String placeText = switch (i) {
                case 0 -> "🥇 1st Place";
                case 1 -> "🥈 2nd Place";
                case 2 -> "🥉 3rd Place";
                case 3 -> "4th Place";
                case 4 -> "5th Place";
                default -> (i + 1) + "th Place";
            };

            VBox entryBox = new VBox(5);
            entryBox.setStyle("""
                -fx-background-color: #3a3d3f;
                -fx-padding: 12px;
                -fx-background-radius: 12;
            """);
            entryBox.setAlignment(Pos.TOP_LEFT);

            Label place = new Label(placeText);
            place.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

            Label name = new Label("Name: " + entry.playerName());
            Label wins = new Label("Wins: " + entry.totalWins());
            Label losses = new Label("Losses: " + entry.totalLosses());
            Label ratio = new Label(String.format("Win/Loss Ratio: %.2f", entry.winLossRatio()));

            for (Label label : List.of(name, wins, losses, ratio)) {
                label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            }

            entryBox.getChildren().addAll(place, name, wins, losses, ratio);
            leaderboardBox.getChildren().add(entryBox);
        }
    }

    Button getExitButton() {
        return exitButton;
    }
}
