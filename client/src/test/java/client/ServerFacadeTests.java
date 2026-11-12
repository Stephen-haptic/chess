package client;

import org.junit.jupiter.api.*;
import server.Server;
import model.AuthData;
import ui.ServerFacade;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacadeTests {
    private static int port;
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"  + port);
    }

    @BeforeEach
    public void clearDatabase() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/db"))
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Successful Register")
    public void registerSuccess() throws IOException, InterruptedException {
        AuthData result = facade.register(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertFalse(result.authToken().isEmpty());
    }

    @Test
    @DisplayName("Register Failed")
    public void registerFailure() throws IOException, InterruptedException {
        facade.register(
                "testUserStrikesBack",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        Assertions.assertThrows(IOException.class, () -> {
            facade.register(
                    "testUserStrikesBack",
                    "Epicpassword2",
                    "legit@totallynotreal.com"
            );
        });
    }

    @Test
    @DisplayName("Successful Login")
    public void loginSuccess() throws IOException, InterruptedException {
        AuthData result = facade.register(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        AuthData returned = facade.login(
                "testUser",
                "Epicpassword"
        );

        Assertions.assertNotNull(returned);
        Assertions.assertEquals("testUser", returned.username());
        Assertions.assertNotNull(returned.authToken());
        Assertions.assertFalse(returned.authToken().isEmpty());
    }

    @Test
    @DisplayName("Login Failed")
    public void loginFailure() throws IOException, InterruptedException {
        facade.register(
                "testUserStrikesBack",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        Assertions.assertThrows(IOException.class, () -> {
            facade.login(
                    "testUserStrikesBack",
                    "Epicpassword2"
            );
        });
    }

    @Test
    @DisplayName("Successful Logout")
    public void logoutSuccess() throws IOException, InterruptedException {
        AuthData authData = facade.register(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );
        Assertions.assertDoesNotThrow(() -> {
            facade.logout(authData.authToken());
        });
    }

    @Test
    @DisplayName("Failed Logout")
    public void logoutFailed() throws IOException, InterruptedException {
        Assertions.assertThrows(IOException.class, () -> {
            facade.logout("invalid");
        });
    }
}
