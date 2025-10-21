package be.kdg.integration2.mvpglobal.view.authentication;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class AuthenticationView extends BorderPane {

    private TextField usernameField;
    private TextField emailField;

    private Button submitButton;
    private Button exitButton;

    private VBox container;

    protected AuthenticationView(String submitButtonText) {
        initializeNodes(submitButtonText);
        layoutNodes();
    }

    private void initializeNodes(String submitButtonText) {
        String usernamePrompt = "Enter your name";
        usernameField = new TextField();
        usernameField.setPromptText(usernamePrompt);
        usernameField.setMaxWidth(textFieldWidth(usernamePrompt));
        usernameField.getStyleClass().add("text-field");

        String emailPrompt = "Enter your email";
        emailField = new TextField();
        emailField.setPromptText(emailPrompt);
        emailField.setMaxWidth(textFieldWidth(emailPrompt));
        emailField.getStyleClass().add("text-field");

        submitButton = new Button(submitButtonText);
        submitButton.getStyleClass().add("button");

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");

        container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll
                (usernameField, emailField, submitButton, exitButton);
    }

    private void layoutNodes() {
        setCenter(container);
    }

    TextField getUsernameField() {
        return usernameField;
    }

    TextField getEmailField() {
        return emailField;
    }

    Button getSubmitButton() {
        return submitButton;
    }

    Button getExitButton() {
        return exitButton;
    }

    // returns pixels that are later set as the
    // width of a text field
    protected double textFieldWidth(String text){
        Text promptText = new Text(text);
        promptText.setFont(Font.font("Comic Sans MS",18));

        return promptText.getBoundsInLocal().getWidth() + 50;
    }
}
