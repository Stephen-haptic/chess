package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTests {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private LoginService loginService;

    @BeforeEach
    void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        loginService = new LoginService(userDAO, authDAO);

        userDAO.storeUser(new UserData(
                "testUser",
                "Epicpassword",
                "legit@totallyreal.com"
        ));
    }

    @Test
    @DisplayName("Successful Login")
    public void loginSuccess() {
        AuthData result = loginService.login("testUser", "Epicpassword");

        assertNotNull(result);
        assertEquals("testUser", result.username());
        assertNotNull(result.authToken());
        assertFalse(result.authToken().isBlank());

        AuthData storedAuth = authDAO.findToken(result.authToken());

        assertNotNull(storedAuth);
        assertEquals("testUser", storedAuth.username());
        assertEquals(result.authToken(), storedAuth.authToken());
    }

    @Test
    @DisplayName("Unknown User")
    public void loginUnknownUsername() {
        assertThrows(UnauthorizedException.class, () ->
                loginService.login("unknownUser", "Epicpassword"));
    }

    @Test
    @DisplayName("Wrong Password")
    public void loginIncorrectPassword() {
        assertThrows(UnauthorizedException.class, () ->
                loginService.login("testUser", "EeehErrr"));
    }

    @Test
    @DisplayName("Null or Blank Input")
    public void loginInvalidInput() {
        assertThrows(BadRequestException.class, () ->
                loginService.login(null, "EpicPassword"));

        assertThrows(BadRequestException.class, () ->
                loginService.login("testUser", null));

        assertThrows(BadRequestException.class, () ->
                loginService.login("", "EpicPassword"));

        assertThrows(BadRequestException.class, () ->
                loginService.login("testUser", ""));
    }
}
