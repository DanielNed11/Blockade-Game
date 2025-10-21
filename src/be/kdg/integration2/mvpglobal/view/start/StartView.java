package be.kdg.integration2.mvpglobal.view.start;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StartView extends BorderPane {
    private Button logInButton;
    private Button registerButton;
    private Button guestButton;
    private Button exitButton;
    private VBox buttonContainer;

    public StartView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        logInButton = new Button("Log In");
        logInButton.getStyleClass().add("button");

        registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");

        guestButton = new Button("Guest");
        guestButton.getStyleClass().add("button");

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");

        buttonContainer = new VBox(30, logInButton, registerButton, guestButton, exitButton);
    }

    private void layoutNodes() {
        buttonContainer.setAlignment(Pos.CENTER);
        setCenter(buttonContainer);
    }

    Button getLogInButton() {
        return logInButton;
    }

    Button getRegisterButton() {
        return registerButton;
    }

    Button getGuestButton() {
        return guestButton;
    }

    Button getExitButton() {
        return exitButton;
    }

    VBox getButtonContainer() {
        return buttonContainer;
    }
}