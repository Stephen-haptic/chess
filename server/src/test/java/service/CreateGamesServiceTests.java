package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGamesServiceTests {
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;
    private CreateGameService createGameService;

    @BeforeEach
    void setup() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        createGameService = new CreateGameService(authDAO, gameDAO);

        authDAO.storeToken(new AuthData("testToken", "testUser"));
    }

    @Test
    @DisplayName("Successfully Create Game")
    public void createGameSuccess() {
        String gameName = "Test Game";

        GameData result = createGameService.createGame("testToken", gameName);

        assertNotNull(result);
        assertNull(result.whiteUsername());
        assertNull(result.blackUsername());
        assertNotNull(result.game());

        GameData storedGame = gameDAO.findGame(result.gameID());

        assertNotNull(storedGame);
        assertEquals(gameName, storedGame.gameName());
        assertEquals(result.gameID(), storedGame.gameID());
    }

    @Test
    @DisplayName("Invalid AuthToken")
    public void createGameInvalidToken() {
        assertThrows(UnauthorizedException.class, () ->
                createGameService.createGame("invalidToken", "Test Game"));
    }

    @Test
    @DisplayName("Missing AuthToken")
    public void createGameMissingToken() {
        assertThrows(BadRequestException.class, () ->
                createGameService.createGame(null, "Test Game"));

        assertThrows(BadRequestException.class, () ->
                createGameService.createGame("", "Test Game"));
    }

    @Test
    @DisplayName("Invalid Game Name")
    public void createGameInvalidName() {
        assertThrows(BadRequestException.class, () ->
                createGameService.createGame("testToken", null));

        assertThrows(BadRequestException.class, () ->
                createGameService.createGame("testToken", ""));
    }
}
