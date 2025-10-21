package be.kdg.integration2.mvpglobal.view.loading;

import be.kdg.integration2.mvpglobal.view.main.MainPresenter;
import be.kdg.integration2.mvpglobal.view.main.MainView;
import javafx.stage.Stage;

public class LoadingPresenter {
    private final LoadingView view;
    private final Stage stage;

    public LoadingPresenter(LoadingView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        addEventHandlers();
    }

    private void addEventHandlers() {
        MainView mainView = new MainView();
        new MainPresenter(mainView, stage);

        view.getTransition().setOnFinished(event -> {
            stage.getScene().setRoot(mainView);
            stage.setTitle("Blockade");
        });
    }

}
