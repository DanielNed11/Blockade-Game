package be.kdg.integration2.mvpglobal.view;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class CustomAlerts {

    // this is a method that creates a custom alerts
    public static void alert(String title, String headerTitle, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerTitle);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("stylesheets/styles.css");
        dialogPane.getStyleClass().add("dialog");

        alert.showAndWait();
    }

    public static void emptyFieldAlert() {
        String title = "Empty field";
        String headerTitle = "All fields are mandatory!";
        String message = "Fill all fields!";
        alert(title, headerTitle, message, Alert.AlertType.ERROR);
    }
}
