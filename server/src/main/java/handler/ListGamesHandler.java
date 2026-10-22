package handler;

import io.javalin.http.Context;
import model.GameData;
import service.ListGamesService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {
        this.listGamesService = listGamesService;
    }

    public void listGames(Context context) {
        String authToken = context.header("Authorization");
        List<GameData> games = listGamesService.listGames(authToken);

        List<Map<String, Object>> gameResponses = games.stream()
                        .map(game -> {
                            Map<String, Object> response = new HashMap<>();
                            response.put("gameID", game.gameID());
                            response.put("whiteUsername", game.whiteUsername());
                            response.put("blackUsername", game.blackUsername());
                            response.put("gameName", game.gameName());
                            return response;
                        }).toList();

        context.status(200);
        context.json(Map.of("games", gameResponses));
    }
}
