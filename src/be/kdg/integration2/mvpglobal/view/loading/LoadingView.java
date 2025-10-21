package be.kdg.integration2.mvpglobal.view.loading;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LoadingView extends BorderPane {

    private Label timeDisplay;
    private ProgressBar timeProgress;
    private LoadingTransition trans;

    public LoadingView() {
        initialiseNodes();
        layoutNodes();
        animate();
    }

    private void initialiseNodes() {
        this.timeDisplay = new Label("Loading: 0%");
        this.timeProgress = new ProgressBar();
        timeProgress.setPrefWidth(350);
    }

    private void layoutNodes() {
        int imageSize = 200;

        ImageView logoImage = new ImageView(new Image("images/BLOCKADE-Photoroom.png"));
        logoImage.setPreserveRatio(true);
        logoImage.setFitHeight(imageSize);

        VBox centerBox = new VBox(20, logoImage, timeDisplay, timeProgress);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPrefWidth(500);
        centerBox.setTranslateY(-30);

        this.setCenter(centerBox);
    }

    Label getTimeDisplay() {
        return timeDisplay;
    }

    ProgressBar getTimeProgress() {
        return timeProgress;
    }

    LoadingTransition getTransition() {
        return trans;
    }

    private void animate() {
        trans = new LoadingTransition(this, 3);
        trans.play();
    }
}
