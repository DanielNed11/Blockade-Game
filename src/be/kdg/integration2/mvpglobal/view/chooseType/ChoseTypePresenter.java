package be.kdg.integration2.mvpglobal.view.chooseType;

import be.kdg.integration2.mvpglobal.model.ElementColors;
import be.kdg.integration2.mvpglobal.model.GameSession;
import be.kdg.integration2.mvpglobal.model.HumanPlayer;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.game.GamePresenter;
import be.kdg.integration2.mvpglobal.view.game.GameView;
import be.kdg.integration2.mvpglobal.view.start.StartPresenter;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ChoseTypePresenter {

    private final Stage stage;
    private final ChoseTypeView view;
    private final StartPresenter startPresenter;
    private final HumanPlayer player;
    private final boolean isConnected;

    public ChoseTypePresenter(
            ChoseTypeView view, StartPresenter startPresenter, Stage stage, HumanPlayer player, boolean isConnected) {
        this.stage = stage;
        this.view = view;
        this.startPresenter = startPresenter;
        this.player = player;
        this.isConnected = isConnected;

        addEventHandlers();
    }


    private void addEventHandlers() {

        view.getStart().setOnAction(e -> {
            GameSession gameSession = null;
            if (!view.getHot().isSelected() && !view.getCold().isSelected()) {
                CustomAlerts.alert(
                        "Select colors",
                        "You have not selected a color.",
                        "Please select colors to continue.",
                        Alert.AlertType.INFORMATION
                );

            } else {
                try {
                    if (view.getHot().isSelected()) {
                        gameSession = new GameSession(player, ElementColors.RED, ElementColors.YELLOW, isConnected);
                    } else if (view.getCold().isSelected()) {
                        gameSession = new GameSession(player, ElementColors.GREEN, ElementColors.BLUE, isConnected);
                    }

                    if (gameSession != null) {
                        GameView gameView = new GameView();
                        new GamePresenter(gameView, stage, gameSession);
                        stage.getScene().setRoot(gameView);
                    }
                } catch (Exception ex) {
                    CustomAlerts.alert(
                            "Error",
                            ex.getMessage(),
                            ex.getMessage(),
                            Alert.AlertType.ERROR
                    );
                }
            }

        });

        view.getExit().setOnAction(e -> startPresenter.returnToStart());


    }
}
