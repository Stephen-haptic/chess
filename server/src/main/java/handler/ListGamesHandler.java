package handler;

import io.javalin.http.Context;
import model.GameData;
import service.ListGamesService;
import java.util.List;
import java.util.Map;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {
        this.listGamesService = listGamesService;
    }

    public void listGames(Context context) {
        String authToken = context.header("Authorization");
        List<GameData> games = listGamesService.listGames(authToken);

        List<Map<String, Object>> gameResponses = games.stream()
                .map(game -> Map.<String, Object>of(
                        "gameID", game.gameID(),
                        "whiteUsername", game.whiteUsername(),
                        "blackUsername", game.blackUsername(),
                        "gameName", game.gameName()
                )).toList();

        context.status(200);
        context.json(Map.of("games", games));
    }
}
