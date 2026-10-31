package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserDAOTests {
    private MySQLUserDAO userDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();

        userDAO = new MySQLUserDAO();
        userDAO.clear();
    }

    @Test
    @DisplayName("Successfully Store and Find User")
    public void storeAndFindUserSuccess() {
        UserData user = new UserData(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        userDAO.storeUser(user);

        UserData storedUser = userDAO.findUser("testUser");

        assertNotNull(storedUser);
        assertEquals(user.username(), storedUser.username());
        assertEquals(user.email(), storedUser.email());
        // check if the stored password is a hash
        assertNotEquals(user.password(), storedUser.password());
        assertTrue(userDAO.verifyPassword("testUser", "Epicpassword"));
    }

    @Test
    @DisplayName("Find Null User")
    public void findNullUser() {
        UserData result = userDAO.findUser("unknownUser");

        assertNull(result);
    }

    @Test
    @DisplayName("Incorrect Password")
    public void verifyIncorrectPassword() {
        UserData user = new UserData(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        userDAO.storeUser(user);

        assertFalse(userDAO.verifyPassword("testUser", "Wrongpassword"));
        assertFalse(userDAO.verifyPassword("unknownUser", "Epicpassword"));
        assertFalse(userDAO.verifyPassword("testUser", "null"));
    }

    @Test
    @DisplayName("Reject Duplicate Username")
    public void storeDuplicateUsername() {
        UserData user = new UserData(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        UserData duplicateUser = new UserData(
                "testUser",
                "Epicpasswordz",
                "legitator@totallyreal.com"
        );

        userDAO.storeUser(user);

        assertThrows(RuntimeException.class, () ->
                userDAO.storeUser(duplicateUser));

        UserData storedUser = userDAO.findUser("testUser");

        assertEquals("legit@totallyreal.com", storedUser.email());
    }

    @Test
    @DisplayName("Successfully Clear Users")
    public void clearUsers() {
        UserData user = new UserData(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        );

        UserData user2 = new UserData(
                "testUserer",
                "Epicpasswordz",
                "legitator@totallyreal.com"
        );

        userDAO.storeUser(user);
        userDAO.storeUser(user2);

        assertNotNull(userDAO.findUser("testUser"));
        assertNotNull(userDAO.findUser("testUserer"));
        userDAO.clear();
        assertNull(userDAO.findUser("testUser"));
        assertNull(userDAO.findUser("testUserer"));
    }
}
