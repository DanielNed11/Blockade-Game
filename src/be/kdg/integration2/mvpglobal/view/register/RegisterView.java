package be.kdg.integration2.mvpglobal.view.register;

import be.kdg.integration2.mvpglobal.view.authentication.AuthenticationView;

public class RegisterView extends AuthenticationView {

    private static final String SUBMIT_BUTTON_TEXT = "Register";

    public RegisterView() {
        super(SUBMIT_BUTTON_TEXT);
    }
}