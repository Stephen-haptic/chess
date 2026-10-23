package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTests {
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;
    private JoinGameService joinGameService;

    private ChessGame chessGame;
    private GameData game;

    @BeforeEach
    void setup() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        joinGameService = new JoinGameService(authDAO, gameDAO);
        authDAO.storeToken(new AuthData("testToken", "testUser"));

        chessGame = new ChessGame();
        game = new GameData(1, null, null, "Test Game", chessGame);
        gameDAO.storeGame(game);
    }

    @Test
    @DisplayName("Successfully Join As White")
    public void joinGameWhiteSuccess() {
        joinGameService.joinGame("testToken", "WHITE", 1);
        GameData result = gameDAO.findGame(1);

        assertNotNull(result);
        assertEquals("testUser", result.whiteUsername());
        assertNull(result.blackUsername());
        assertEquals("Test Game", result.gameName());
        assertSame(chessGame, result.game());
    }

    @Test
    @DisplayName("Successfully Join As Black")
    public void joinGameBlackSuccess() {
        joinGameService.joinGame("testToken", "BLACK", 1);
        GameData result = gameDAO.findGame(1);

        assertNotNull(result);
        assertEquals("testUser", result.blackUsername());
        assertNull(result.whiteUsername());
        assertEquals("Test Game", result.gameName());
        assertSame(chessGame, result.game());
    }

    @Test
    @DisplayName("Invalid AuthToken")
    public void joinGameInvalidToken() {
        assertThrows(UnauthorizedException.class, () ->
                joinGameService.joinGame("invalidToken", "WHITE", 1));
    }

    @Test
    @DisplayName("Missing AuthToken")
    public void joinGameMissingToken() {
        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame("", "WHITE", 1));

        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame(null, "WHITE", 1));
    }

    @Test
    @DisplayName("Game Does Not Exist")
    public void joinGameInvalidGameID() {
        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame("testToken", "WHITE", 999));
    }

    @Test
    @DisplayName("White Spot Taken")
    public void joinGameWhiteTaken() {
        GameData occupiedGame = new GameData(
                1, "existingWhite", null, "Test Game", chessGame);
        gameDAO.updateGame(occupiedGame);

        assertThrows(AlreadyTakenException.class, () ->
                joinGameService.joinGame("testToken", "WHITE", 1));

        GameData result = gameDAO.findGame(1);
        assertEquals("existingWhite", result.whiteUsername());
        assertNull(result.blackUsername());
    }

    @Test
    @DisplayName("Black Spot Taken")
    public void joinGameBlackTaken() {
        GameData occupiedGame = new GameData(
                1, null, "existingBlack", "Test Game", chessGame);
        gameDAO.updateGame(occupiedGame);

        assertThrows(AlreadyTakenException.class, () ->
                joinGameService.joinGame("testToken", "BLACK", 1));

        GameData result = gameDAO.findGame(1);
        assertEquals("existingBlack", result.blackUsername());
        assertNull(result.whiteUsername());
    }

    @Test
    @DisplayName("Invalid Player Color")
    public void joinGameInvalidColor() {
        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame("testToken", "GREEN", 1));

        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame("testToken", null, 1));

        assertThrows(BadRequestException.class, () ->
                joinGameService.joinGame("testToken", "", 1));
    }
}
