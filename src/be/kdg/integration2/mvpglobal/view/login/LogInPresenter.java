package be.kdg.integration2.mvpglobal.view.login;

import be.kdg.integration2.mvpglobal.view.start.StartPresenter;
import be.kdg.integration2.mvpglobal.view.authentication.AuthenticationPresenter;
import javafx.stage.Stage;

public class LogInPresenter extends AuthenticationPresenter<LogInView> {

    public LogInPresenter(LogInView view, StartPresenter startPresenter, Stage stage) {
        super(view, startPresenter, stage);
    }

}
