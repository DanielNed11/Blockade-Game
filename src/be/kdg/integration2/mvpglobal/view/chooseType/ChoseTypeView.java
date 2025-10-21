package be.kdg.integration2.mvpglobal.view.chooseType;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChoseTypeView extends StackPane {

    private ToggleButton hot;
    private ToggleButton cold;
    private Button start;
    private Button exit;

    public ChoseTypeView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {

        hot = new ToggleButton("Hot");
        hot.getStyleClass().add("hot-button");

        cold = new ToggleButton("Cold");
        cold.getStyleClass().add("cold-button");

        start = new Button("Start");

        exit = new Button("Exit");

        ToggleGroup colors = new ToggleGroup();
        hot.setToggleGroup(colors);
        cold.setToggleGroup(colors);
    }

    private void layoutNodes() {
        Label label = new Label("Choose colors");
        label.getStyleClass().add("choice-label");

        setAlignment(Pos.CENTER);

        HBox colors = new HBox(30, hot, cold);
        colors.setAlignment(Pos.CENTER);

        VBox container = new VBox(40, label, colors, start, exit);
        container.setAlignment(Pos.CENTER);

        getChildren().add(container);
    }

    ToggleButton getHot() {
        return hot;
    }

    ToggleButton getCold() {
        return cold;
    }

    Button getStart() {
        return start;
    }

    Button getExit() {
        return exit;
    }

}
