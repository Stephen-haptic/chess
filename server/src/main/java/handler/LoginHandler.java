package handler;

import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.LoginService;

public class LoginHandler {
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public void login(Context context) {
        UserData user = context.bodyAsClass(UserData.class);
        AuthData authData = loginService.login(user.username(), user.password());
        context.status(200);
        context.json(authData);
    }
}
