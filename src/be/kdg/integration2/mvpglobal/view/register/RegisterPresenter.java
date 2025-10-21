package be.kdg.integration2.mvpglobal.view.register;

import be.kdg.integration2.mvpglobal.view.start.StartPresenter;
import be.kdg.integration2.mvpglobal.view.authentication.AuthenticationPresenter;
import javafx.stage.Stage;

public class RegisterPresenter extends AuthenticationPresenter<RegisterView> {

    public RegisterPresenter(RegisterView view, StartPresenter startPresenter, Stage stage) {
        super(view, startPresenter, stage);
    }


}
