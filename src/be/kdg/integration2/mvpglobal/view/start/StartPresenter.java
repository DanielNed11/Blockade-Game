package be.kdg.integration2.mvpglobal.view.start;

import be.kdg.integration2.mvpglobal.model.HumanPlayer;
import be.kdg.integration2.mvpglobal.view.chooseType.ChoseTypePresenter;
import be.kdg.integration2.mvpglobal.view.chooseType.ChoseTypeView;
import javafx.scene.layout.VBox;
import be.kdg.integration2.mvpglobal.view.main.MainPresenter;
import be.kdg.integration2.mvpglobal.view.login.LogInPresenter;
import be.kdg.integration2.mvpglobal.view.login.LogInView;
import be.kdg.integration2.mvpglobal.view.register.RegisterPresenter;
import be.kdg.integration2.mvpglobal.view.register.RegisterView;
import javafx.stage.Stage;

public class StartPresenter {

    private final Stage stage;
    private final StartView view;
    private final MainPresenter mainPresenter;
    private final VBox initialContainer;

    public StartPresenter(
            StartView view, MainPresenter mainPresenter, Stage stage) {
        this.stage = stage;
        this.view = view;
        this.mainPresenter = mainPresenter;
        this.initialContainer = view.getButtonContainer();

        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getLogInButton().setOnAction(event -> {
            LogInView logInView = new LogInView();
            new LogInPresenter(logInView, this, stage);

            view.setCenter(logInView);
        });

        view.getRegisterButton().setOnAction(event -> {
            RegisterView registerView = new RegisterView();
            new RegisterPresenter(registerView, this, stage);

            view.setCenter(registerView);
        });

        view.getGuestButton().setOnAction(event -> {
            ChoseTypeView choseTypeView = new ChoseTypeView();
            new ChoseTypePresenter(choseTypeView, this, stage, new HumanPlayer("Guest", -99), false);

            view.setCenter(choseTypeView);
        });

        view.getExitButton().setOnAction(event ->
                mainPresenter.closeContentPane());

    }

    // making a way to return to the previous view
    public void returnToStart() {
        view.setCenter(initialContainer);
    }
}

