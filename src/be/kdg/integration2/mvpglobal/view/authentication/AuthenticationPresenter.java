package be.kdg.integration2.mvpglobal.view.authentication;

import be.kdg.integration2.mvpglobal.model.HumanPlayer;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import be.kdg.integration2.mvpglobal.view.chooseType.ChoseTypePresenter;
import be.kdg.integration2.mvpglobal.view.chooseType.ChoseTypeView;
import javafx.scene.control.Alert;
import be.kdg.integration2.mvpglobal.model.DBFunctions;
import be.kdg.integration2.mvpglobal.view.start.StartPresenter;
import be.kdg.integration2.mvpglobal.view.register.RegisterView;
import javafx.stage.Stage;

public abstract class AuthenticationPresenter<V extends AuthenticationView> {
    private final Stage stage;
    private HumanPlayer player;
    private final V view;
    private final StartPresenter startPresenter;

    public AuthenticationPresenter(V view, StartPresenter startPresenter, Stage stage) {
        this.stage = stage;
        this.view = view;
        this.startPresenter = startPresenter;
        addEventHandlers();

    }

    private void addEventHandlers() {
        view.getSubmitButton().setOnAction(event -> {
            String playerName = view.getUsernameField().getText();
            String playerEmail = view.getEmailField().getText();

            if (playerName.trim().isEmpty() || playerEmail.trim().isEmpty()) {
                CustomAlerts.emptyFieldAlert();
                return;
            }

            try {
                //if we get registerView as a view we are going to continue with register
                if (view instanceof RegisterView) {
                    player = DBFunctions.register(playerName, playerEmail);
                    CustomAlerts.alert("Register",
                            String.format("Welcome, %s!", player.getName()),
                            "Registered and logged in successfully",
                            Alert.AlertType.INFORMATION);


                    ChoseTypeView choseTypeView = new ChoseTypeView();
                    new ChoseTypePresenter(choseTypeView, startPresenter, stage, player, true);

                    view.setCenter(choseTypeView);
                    // and else: we continue with login
                } else {
                    player = DBFunctions.login(playerName, playerEmail);
                    CustomAlerts.alert("Login",
                            String.format("Hello again, %s!", player.getName()),
                            "Logged in successfully",
                            Alert.AlertType.INFORMATION);

                    ChoseTypeView choseTypeView = new ChoseTypeView();
                    new ChoseTypePresenter(choseTypeView, startPresenter, stage, player, true);

                    view.setCenter(choseTypeView);
                }
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                CustomAlerts.alert("Error", "Operation Failed", errorMessage, Alert.AlertType.ERROR);
            }

            view.getUsernameField().clear();
            view.getEmailField().clear();

        });

        view.getUsernameField().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                view.getUsernameField().setPromptText("");
            } else {
                if (view.getUsernameField().getText().isEmpty()) {
                    view.getUsernameField().setPromptText("Enter name here");
                }
            }
        });

        view.getEmailField().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                view.getEmailField().setPromptText("");
            } else {
                if (view.getEmailField().getText().isEmpty()) {
                    view.getEmailField().setPromptText("Enter email here");
                }
            }
        });

        view.getExitButton().setOnAction(event -> startPresenter.returnToStart());
    }
}