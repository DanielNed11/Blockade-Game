package be.kdg.integration2.mvpglobal;

import be.kdg.integration2.mvpglobal.view.UISettings;
import be.kdg.integration2.mvpglobal.view.loading.LoadingPresenter;
import be.kdg.integration2.mvpglobal.view.loading.LoadingView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoadingView view = new LoadingView();
        new LoadingPresenter(view, primaryStage);

        Scene scene = new Scene(view, UISettings.SCREEN_WIDTH, UISettings.SCREEN_HEIGHT);
        scene.getStylesheets().add("stylesheets/styles.css");

        primaryStage.getIcons().add(new Image("images/BLOCKADE-Photoroom.png"));
        primaryStage.setTitle("Loading...");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}