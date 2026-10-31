package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthDAOTests {
    private MySQLAuthDAO authDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();

        authDAO = new MySQLAuthDAO();
        authDAO.clear();
    }

    @Test
    @DisplayName("Successfully Store AuthToken")
    public void storeTokenSuccess() {
        AuthData auth = new AuthData(
                "testToken",
                "testUser"
        );

        authDAO.storeToken(auth);
        AuthData storedAuth = authDAO.findToken("testToken");

        assertNotNull(storedAuth);
        assertEquals(auth.authToken(), storedAuth.authToken());
        assertEquals(auth.username(), storedAuth.username());
    }

    @Test
    @DisplayName("Find Null Token")
    public void findNullToken() {
        AuthData result = authDAO.findToken("unknownToken");

        assertNull(result);
    }

    @Test
    @DisplayName("Successfully Remove Token")
    public void removeToken() {
        AuthData auth = new AuthData(
                "testToken",
                "testUser"
        );
        authDAO.storeToken(auth);

        assertNotNull(authDAO.findToken("testToken"));
        authDAO.removeToken("testToken");
        assertNull(authDAO.findToken("testToken"));
    }

    @Test
    @DisplayName("Attempt to Store Duplicate Token")
    public void storeDuplicateToken() {
        AuthData auth = new AuthData(
                "testToken",
                "testUser"
        );

        AuthData duplicateAuth = new AuthData(
                "testToken",
                "testUser2"
        );

        authDAO.storeToken(auth);

        assertThrows(RuntimeException.class, () ->
                authDAO.storeToken(duplicateAuth));

        AuthData storedAuth = authDAO.findToken("testToken");

        assertEquals("testUser", storedAuth.username());
    }

    @Test
    @DisplayName("Successfully Clear Tokens")
    public void clearTokensSuccess() {
        AuthData auth = new AuthData(
                "testToken",
                "testUser"
        );

        AuthData auth2 = new AuthData(
                "testTokenator",
                "testUser2"
        );

        authDAO.storeToken(auth);
        authDAO.storeToken(auth2);

        assertNotNull(authDAO.findToken("testToken"));
        assertNotNull(authDAO.findToken("testTokenator"));
        authDAO.clear();
        assertNull(authDAO.findToken("testToken"));
        assertNull(authDAO.findToken("testTokenator"));
    }
}
