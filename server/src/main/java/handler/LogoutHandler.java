package handler;

import io.javalin.http.Context;
import service.LogoutService;

public class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    public void logout(Context context) {
        String authToken = context.header("Authorization");
        logoutService.logout(authToken);
        context.status(200);
    }
}
