package be.kdg.integration2.mvpglobal.view.MoveDuration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class MoveDurationChartView extends StackPane {
    private final XYChart.Series<Number, Number> playerSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> computerSeries = new XYChart.Series<>();
    private final Button backButton = new Button("Back to Main Screen");

    public MoveDurationChartView() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Move Number");
        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        xAxis.setTickLabelFont(javafx.scene.text.Font.font("Arial", 12));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Duration (seconds)");
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        yAxis.setTickLabelFont(javafx.scene.text.Font.font("Arial", 12));

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Move Duration per Player");
        chart.setLegendVisible(true);
        chart.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
        chart.lookup(".chart-legend").setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        playerSeries.setName("Player");

        computerSeries.setName("Computer");

        chart.getData().addAll(playerSeries, computerSeries);

        Label description = new Label("Outliers are highlighted in red/orange");
        description.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        backButton.setStyle("""
            -fx-background-color: #3c3f41;
            -fx-text-fill: white;
            -fx-padding: 8px 16px;
            -fx-font-size: 14px;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: white;
        """);

        VBox topBox = new VBox(10, description, backButton);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #2b2b2b;");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(topBox);
        root.setCenter(chart);

        root.setStyle("-fx-background-color: #2b2b2b;");
        this.setStyle("-fx-background-color: #2b2b2b;");
        this.getChildren().add(root);
    }

    public void updateChart(Map<String, List<Integer>> durations,
                            List<Integer> playerOutliers,
                            List<Integer> computerOutliers) {
        playerSeries.getData().clear();
        computerSeries.getData().clear();

        List<Integer> playerDurations = durations.get("Player");
        List<Integer> computerDurations = durations.get("Computer");

        for (int i = 0; i < playerDurations.size(); i++) {
            XYChart.Data<Number, Number> data = new XYChart.Data<>(i + 1, playerDurations.get(i));
            playerSeries.getData().add(data);
            final int index = i;
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null && playerOutliers.contains(index)) {
                    newNode.setStyle("-fx-background-color: red, white; -fx-background-insets: 0, 2;");
                }
            });
        }

        for (int i = 0; i < computerDurations.size(); i++) {
            XYChart.Data<Number, Number> data = new XYChart.Data<>(i + 1, computerDurations.get(i));
            computerSeries.getData().add(data);
            final int index = i;
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null && computerOutliers.contains(index)) {
                    newNode.setStyle("-fx-background-color: orange, white; -fx-background-insets: 0, 2;");
                }
            });
        }
    }

    Button getBackButton() {
        return backButton;
    }
}