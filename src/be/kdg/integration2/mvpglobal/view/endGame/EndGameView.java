package be.kdg.integration2.mvpglobal.view.endGame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;


public class EndGameView extends StackPane {

    private Label winner;
    private Label totalPlayTime;

    private Label turnsOfPlayer;
    private Label turnsOfComputer;

    private Label avgDurationOfPlayer;
    private Label avgDurationOfComputer;

    private Button next;

    public EndGameView() {

        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {

        winner = new Label();
        totalPlayTime = new Label();
        turnsOfPlayer = new Label();
        turnsOfComputer = new Label();
        avgDurationOfPlayer = new Label();
        avgDurationOfComputer = new Label();
        next = new Button("Next");

    }

    private void layoutNodes() {
        // Style all labels
        Font headerFont = Font.font("Arial", FontWeight.BOLD, 28);
        Font subHeaderFont = Font.font("Arial", FontWeight.BOLD, 20);
        Font valueFont = Font.font("Arial", FontWeight.NORMAL, 16);

        winner.setFont(headerFont);
        totalPlayTime.setFont(subHeaderFont);

        turnsOfPlayer.setFont(valueFont);
        turnsOfPlayer.setAlignment(Pos.CENTER);
        avgDurationOfPlayer.setFont(valueFont);
        avgDurationOfPlayer.setAlignment(Pos.CENTER);
        turnsOfComputer.setFont(valueFont);
        turnsOfComputer.setAlignment(Pos.CENTER);
        avgDurationOfComputer.setFont(valueFont);
        avgDurationOfComputer.setAlignment(Pos.CENTER);

        // Group player stats
        VBox playerStats = new VBox(10,
                new Label("Player Stats:"),
                turnsOfPlayer,
                avgDurationOfPlayer
        );
        playerStats.setAlignment(Pos.CENTER_LEFT);

        VBox computerStats = new VBox(10,
                new Label("Computer Stats:"),
                turnsOfComputer,
                avgDurationOfComputer
        );
        computerStats.setAlignment(Pos.CENTER_RIGHT);

        Label playTimeLabel = new Label("Total Play Time:");
        playTimeLabel.setFont(subHeaderFont);

        VBox topSection = new VBox(10, winner, playTimeLabel, totalPlayTime);
        topSection.setAlignment(Pos.TOP_CENTER);

        // Container for stats
        HBox statsContainer = new HBox(100, playerStats, computerStats);
        statsContainer.setAlignment(Pos.CENTER);
        statsContainer.setPadding(new Insets(20));

        // Overall layout
        VBox mainLayout = new VBox(30, topSection, statsContainer, next);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));

        getChildren().add(mainLayout);

    }


    Label getWinner() {
        return winner;
    }

    Label getTotalPlayTime() {
        return totalPlayTime;
    }

    Label getTurnsOfPlayer() {
        return turnsOfPlayer;
    }

    Label getTurnsOfComputer() {
        return turnsOfComputer;
    }

    Label getAvgDurationOfPlayer() {
        return avgDurationOfPlayer;
    }

    Label getAvgDurationOfComputer() {
        return avgDurationOfComputer;
    }

    Button getNext() {
        return next;
    }
}

