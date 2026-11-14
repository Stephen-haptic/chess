package client;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class ServerFacade {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private <T> T makeRequest(
            String method,
            String endpoint,
            Object requestBody,
            String authToken,
            Class<T> responseType
    ) throws IOException, InterruptedException {
        String url = serverUrl + endpoint;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));

        HttpRequest.BodyPublisher publisher;
        if (requestBody != null) {
            String json = gson.toJson(requestBody);
            publisher = BodyPublishers.ofString(json);
        } else {
            publisher = BodyPublishers.noBody();
        }
        builder.method(method, publisher);

        if (requestBody != null) {
            builder.header("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.header("Authorization", authToken);
        }

        HttpRequest request = builder.build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 199 && response.statusCode() < 300) {
            if (responseType == null) {
                return null;
            }
            return gson.fromJson(response.body(), responseType);
        }
        else {
            throw new IOException("Request Failed");
        }
    }

    //Register
    public AuthData register(String username, String password, String email)
            throws IOException, InterruptedException {

        UserData user = new UserData(username, password, email);

        return makeRequest("POST", "/user", user, null, AuthData.class);
    }

    //Login
    public AuthData login(String username, String password)
            throws IOException, InterruptedException {

        LoginRequest loginRequest = new LoginRequest(username, password);

        return makeRequest("POST", "/session", loginRequest, null, AuthData.class);
    }

    //Logout
    public void logout(String authToken)
            throws IOException, InterruptedException {
        makeRequest("DELETE", "/session", null, authToken, null);
    }

    //List Games
    public ListGamesResult listGames (String authToken)
            throws IOException, InterruptedException {
        return makeRequest("GET", "/game", null, authToken, ListGamesResult.class);
    }

    //Create Game
    public CreateGameResult createGame (String gameName, String authToken)
            throws IOException, InterruptedException {
        CreateGameRequest gameRequest = new CreateGameRequest(gameName);

        return makeRequest("POST", "/game", gameRequest, authToken, CreateGameResult.class);
    }

    //Join Game
    public void joinGame(String playerColor, int gameID, String authToken)
            throws IOException, InterruptedException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);

        makeRequest("PUT", "/game", joinGameRequest, authToken, null);
    }
}
