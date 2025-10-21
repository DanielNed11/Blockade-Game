package be.kdg.integration2.mvpglobal.view.info;

import be.kdg.integration2.mvpglobal.view.main.MainPresenter;
import javafx.stage.Stage;

public class InfoPresenter {

    private Stage stage;
    private InfoView view;
    private MainPresenter startPresenter;

    public InfoPresenter(InfoView view, MainPresenter startPresenter, Stage stage) {
        this.stage = stage;
        this.view = view;
        this.startPresenter = startPresenter;

        addEventHandlers();
        updateView();
    }

    private void addEventHandlers() {
        view.getExitButton().setOnAction(event -> startPresenter.closeContentPane());

    }

    private void updateView() {

    }

}
