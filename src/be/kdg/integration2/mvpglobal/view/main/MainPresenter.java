package be.kdg.integration2.mvpglobal.view.main;

import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.leaderBoard.LeaderboardPresenter;
import be.kdg.integration2.mvpglobal.view.leaderBoard.LeaderboardView;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import be.kdg.integration2.mvpglobal.view.info.InfoPresenter;
import be.kdg.integration2.mvpglobal.view.info.InfoView;
import be.kdg.integration2.mvpglobal.view.paricles.ParticlesAnimation;
import be.kdg.integration2.mvpglobal.view.start.StartPresenter;
import be.kdg.integration2.mvpglobal.view.start.StartView;

public class MainPresenter {
    private final MainView view;
    private final Stage stage;
    private boolean isContentVisible = false;

    public MainPresenter(MainView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        ParticlesAnimation particlesAnimation = new
                ParticlesAnimation(view.getParticlesCanvas());

        particlesAnimation.startParticles();
        addEventHandlers();
        closeContentPane();
    }

    private void addEventHandlers() {
        view.getStartButton().setOnAction(eventOpen ->
                openContentPane(StartView.class, StartPresenter.class));

        view.getInfoButton().setOnAction(eventOpen ->
                openContentPane(InfoView.class, InfoPresenter.class));

        view.getLeaderboardButton().setOnAction(e ->
                openContentPane(LeaderboardView.class, LeaderboardPresenter.class));
    }

    // a generic method that takes care of changing the view
    private <V extends Parent, P> void openContentPane(Class<V> classView, Class<P> classPresenter) {
        isContentVisible = !isContentVisible;

        setContentVisible();

        if (isContentVisible) {
            view.getButtonContainer().setVisible(false);
            view.getButtonContainer().setManaged(false);

            try {
                V newView = classView.getDeclaredConstructor().newInstance();
                classPresenter
                        .getDeclaredConstructor(classView, this.getClass(), stage.getClass())
                        .newInstance(newView, this, stage);
                view.getContentPane().getChildren().setAll(newView);
            } catch (Exception e) {
                CustomAlerts.alert(
                        "Error",
                        "Error while changing menus.",
                        e.getMessage(),
                        Alert.AlertType.INFORMATION
                );
            }
        } else {
            closeContentPane();
        }
    }

    // "closes contentPane" it sets it invisible and moves the buttons
    // on top of the already invisible contentPane
    public void closeContentPane() {
        isContentVisible = false;
        view.getButtonContainer().setVisible(true);
        view.getButtonContainer().setManaged(true);
        view.getContentPane().getChildren().clear();
        setContentVisible();
    }

    private void setContentVisible() {
        view.getContentPane().setVisible(isContentVisible);
        view.getContentPane().setManaged(isContentVisible);
    }

}
