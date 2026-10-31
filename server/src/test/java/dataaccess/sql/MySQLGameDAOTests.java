package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import chess.ChessGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameDAOTests {
    private MySQLGameDAO gameDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();

        gameDAO = new MySQLGameDAO();
        gameDAO.clear();
    }

    @Test
    @DisplayName("Successfully Store Game")
    public void storeGameSuccess() {
        GameData game = new GameData(
                1234,
                null,
                null,
                "Test Game",
                new ChessGame()
        );

        gameDAO.storeGame(game);
        GameData storedGame = gameDAO.findGame(1234);

        assertEquals(game.gameID(), storedGame.gameID());
        assertEquals(game.gameName(), storedGame.gameName());
        assertNotNull(storedGame.game());
        assertEquals(
                game.game().getTeamTurn(),
                storedGame.game().getTeamTurn()
        );
        assertEquals(
                game.game().getBoard(),
                storedGame.game().getBoard()
        );
    }

    @Test
    @DisplayName("Game Not Found")
    public void findGameNotFound() {
        assertNull(gameDAO.findGame(1001));
    }

    @Test
    @DisplayName("Successfully Update Game")
    public void updateGameSuccess() {
        GameData game = new GameData(
                1234,
                null,
                null,
                "Test Game",
                new ChessGame()
        );
        gameDAO.storeGame(game);

        GameData newGame = new GameData(
                1234,
                "Geraldine",
                null,
                "Test Game",
                new ChessGame()
        );
        gameDAO.updateGame(newGame);
        GameData updatedGame = gameDAO.findGame(1234);

        assertNotEquals(game.whiteUsername(), updatedGame.whiteUsername());
        assertNull(updatedGame.blackUsername());
        assertEquals(game.gameID(), updatedGame.gameID());
        assertEquals(game.gameName(), updatedGame.gameName());
    }

    @Test
    @DisplayName("Successfully List Games")
    public void listGamesSuccess() {
        GameData game1 = new GameData(
                1234,
                null,
                null,
                "Test Game",
                new ChessGame()
        );
        GameData game2 = new GameData(
                1001,
                "Geraldine",
                null,
                "Test Gaming",
                new ChessGame()
        );
        GameData game3 = new GameData(
                9876,
                "Monson",
                "Brigham",
                "Prophet Game",
                new ChessGame()
        );
        gameDAO.storeGame(game1);
        gameDAO.storeGame(game2);
        gameDAO.storeGame(game3);

        List<GameData> games = gameDAO.listGames();

        assertNotNull(games);
        assertEquals(3, games.size());
        assertTrue(games.stream().anyMatch(
                game -> game.gameID() == 1234
                && game.gameName().equals("Test Game")
        ));
        assertTrue(games.stream().anyMatch(
                game -> game.gameID() == 1001
                        && game.gameName().equals("Test Gaming")
        ));
        assertTrue(games.stream().anyMatch(
                game -> game.gameID() == 9876
                        && game.gameName().equals("Prophet Game")
                        && game.whiteUsername().equals("Monson")
                        && game.blackUsername().equals("Brigham")
        ));
    }
}