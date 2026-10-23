package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearServiceTests {

    @Test
    @DisplayName("Clear Empty Database")
    public void clearEmptyDatabase() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        ClearService service = new ClearService(userDAO, gameDAO, authDAO);

        Assertions.assertDoesNotThrow(service::clearApplication);
    }

    @Test
    @DisplayName("Clear Database With Data")
    public void clearDatabaseWithData() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        userDAO.storeUser(new UserData("testUser", "Epicpassword", "legit@totallyreal.com"));
        authDAO.storeToken(new AuthData("testToken", "testUser"));
        gameDAO.storeGame(new GameData(1, null, null, "Test Game", new ChessGame()));

        ClearService service = new ClearService(userDAO, gameDAO, authDAO);
        service.clearApplication();

        Assertions.assertNull(userDAO.findUser("testUser"));
        Assertions.assertNull(authDAO.findToken("testToken"));
        Assertions.assertNull(gameDAO.findGame(1));
    }
}
