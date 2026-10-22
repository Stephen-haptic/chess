package handler;

import io.javalin.http.Context;
import model.JoinGameRequest;
import service.JoinGameService;

public class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    public void joinGame(Context context) {
        String authToken = context.header("Authorization");
        JoinGameRequest request = context.bodyAsClass(JoinGameRequest.class);
        joinGameService.joinGame(authToken, request.playerColor(), request.gameID());
        context.status(200);
    }
}
