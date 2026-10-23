package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesServiceTests {
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;
    private ListGamesService listGamesService;

    @BeforeEach
    void setup() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        listGamesService = new ListGamesService(authDAO, gameDAO);

        authDAO.storeToken(new AuthData("testToken", "testUser"));
    }

    @Test
    @DisplayName("Successfully List Games")
    public void listGamesSuccess() {
        GameData game1 = new GameData(
                1, null, null, "Game One", new ChessGame());
        GameData game2 = new GameData(
                2, "whitePlayer", null, "Game Two", new ChessGame());

        gameDAO.storeGame(game1);
        gameDAO.storeGame(game2);

        List<GameData> result = listGamesService.listGames("testToken");

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream()
                .anyMatch(game -> game.gameID() == 1));
        assertTrue(result.stream()
                .anyMatch(game -> game.gameID() == 2));

        assertTrue(result.stream()
                .anyMatch(game -> game.gameName().equals("Game One")));
        assertTrue(result.stream()
                .anyMatch(game -> game.gameName().equals("Game Two")));
    }

    @Test
    @DisplayName("Successfully List Empty Games")
    public void listGamesEmpty() {
        List<GameData> result = listGamesService.listGames("testToken");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Invalid Token")
    public void listGamesInvalidToken() {
        assertThrows(UnauthorizedException.class, () ->
                listGamesService.listGames("invalidToken"));
    }

    @Test
    @DisplayName("Missing Token")
    public void listGamesMissingToken() {
        assertThrows(UnauthorizedException.class, () ->
                listGamesService.listGames(""));
        assertThrows(UnauthorizedException.class, () ->
                listGamesService.listGames(null));
    }
}
