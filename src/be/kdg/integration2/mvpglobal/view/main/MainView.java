package be.kdg.integration2.mvpglobal.view.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import be.kdg.integration2.mvpglobal.view.paricles.ParticlesCanvas;

public class MainView extends StackPane {

    private Button startButton;
    private Button leaderboardButton;
    private Button infoButton;

    private HBox buttonContainer;
    private StackPane contentPane;

    private VBox imageContainer;

    private ParticlesCanvas particlesCanvas;


    public MainView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        particlesCanvas = new ParticlesCanvas();



        ImageView imageView = new ImageView(new Image("images/BLOCKADE-Photoroom.png"));
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_CENTER);
        imageContainer.setPadding(new Insets(0, 0, 20, 0));
        imageContainer.setTranslateY(10);

        startButton = new Button("Start");
        startButton.getStyleClass().add("button");

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.getStyleClass().add("button");

        infoButton = new Button("Info");
        infoButton.getStyleClass().add("button");

        VBox buttonBox = new VBox(30);
        buttonBox.getChildren().addAll(startButton, leaderboardButton, infoButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(150);

        // First I am putting the buttons in Vbox, so I can get the vertical layout,
        // then I put them in a HBox that will align them once you move them to the left

        buttonContainer = new HBox(buttonBox);
        buttonContainer.setAlignment(Pos.CENTER);

        contentPane = new StackPane();
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setPadding(new Insets(20));

    }

    private void layoutNodes() {
        getChildren().addAll(particlesCanvas, imageContainer, buttonContainer, contentPane);
        setPadding(new Insets(20));

        contentPane.setTranslateY(20);
        contentPane.setVisible(false);
        contentPane.setManaged(false);
    }

    Button getStartButton() {
        return startButton;
    }

    Button getLeaderboardButton() {return leaderboardButton;}

    Button getInfoButton() {
        return infoButton;
    }

    HBox getButtonContainer() {
        return buttonContainer;
    }

    StackPane getContentPane() {
        return contentPane;
    }

    ParticlesCanvas getParticlesCanvas() {
        return particlesCanvas;
    }

}
