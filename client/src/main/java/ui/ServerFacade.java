package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();

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

        return null;
    }

    //Register

    //Login

    //Logout

    //List Games

    //Create Game

    //Join Game
}
