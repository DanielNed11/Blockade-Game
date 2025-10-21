package be.kdg.integration2.mvpglobal.view.info;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class InfoView extends BorderPane {

    private Label label;
    private Button exitButton;
    private VBox vBox;

    public InfoView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        label = new Label();
        label.getStyleClass().add("info-label");
        label.setText(
                "On your turn, roll the two dice corresponding to your two colors. " +
                "You have a number of movement points equal to the number " +
                "rolled, to be used on the color of pieces corresponding to " +
                "the color of the die. So a 5 rolled on a blue die means 5 points " +
                "of blue movement. Each movement point allows you to move a piece " +
                "of that color one square, following the Rules of Movement.");
        label.setWrapText(true);

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("login-button");

        vBox = new VBox(30, label, exitButton);
    }

    private void layoutNodes() {
        exitButton.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        setCenter(vBox);
    }

    VBox getVBox() {
        return vBox;
    }

    Label getLabel() {
        return label;
    }

    Button getExitButton() {
        return exitButton;
    }
}
