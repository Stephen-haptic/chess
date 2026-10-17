package handler;

import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.RegisterService;

public class RegisterHandler {
    private final RegisterService registerService;

    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    public void register(Context context) {
        UserData user = context.bodyAsClass(UserData.class);
        AuthData authData = registerService.register(user.username(), user.password(), user.email());
        context.status(200);
        context.json(authData);
    }
}
