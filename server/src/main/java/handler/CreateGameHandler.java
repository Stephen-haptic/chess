package handler;

import io.javalin.http.Context;
import model.GameData;
import service.CreateGameService;

public class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public void createGame(Context context) {
        String authToken = context.header("Authorization");
        GameData gameData = context.bodyAsClass(GameData.class);
        GameData game = createGameService.createGame(authToken, gameData.gameName());
        context.status(200);
        context.json(java.util.Map.of("gameID", game.gameID()));
    }
}
