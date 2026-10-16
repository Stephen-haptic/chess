package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

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
}
