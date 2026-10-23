package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import service.RegisterService;
import exception.AlreadyTakenException;
import exception.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTests {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private RegisterService registerService;

    @BeforeEach
    void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Successful Register New User")
    public void registerSuccess() {
        String username = "testUser";
        String password = "Epicpassword";
        String email = "legit@totallyreal.com";
        AuthData result = registerService.register(username, password, email);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertNotNull(result.authToken());
        assertFalse(result.authToken().isBlank());

        UserData storedUser = userDAO.findUser(username);
        assertNotNull(storedUser);
        assertEquals(username, storedUser.username());
        assertEquals(password, storedUser.password());
        assertEquals(email, storedUser.email());

        AuthData storedAuth = authDAO.findToken(result.authToken());
        assertNotNull(storedAuth);
        assertEquals(username, storedAuth.username());
        assertEquals(result.authToken(), storedAuth.authToken());
    }

    @Test
    @DisplayName("Required Field Null")
    public void registerNullField() {
        assertThrows(BadRequestException.class, () ->
                registerService.register(null, "Epicpassword", "legit@totallyreal.com"));

        assertThrows(BadRequestException.class, () ->
                registerService.register("testUser", null, "legit@totallyreal.com"));

        assertThrows(BadRequestException.class, () ->
                registerService.register("testUser", "Epicpassword", null));
    }

    @Test
    @DisplayName("Required Field Blank")
    public void registerBlankField() {
        assertThrows(BadRequestException.class, () ->
                registerService.register("", "Epicpassword", "legit@totallyreal.com"));

        assertThrows(BadRequestException.class, () ->
                registerService.register("testUser", "", "legit@totallyreal.com"));

        assertThrows(BadRequestException.class, () ->
                registerService.register("testUser", "Epicpassword", ""));
    }

    @Test
    @DisplayName("Username Already Taken")
    public void registerDuplicateUsername() {
        String username = "testUser";

        registerService.register(username, "Epicpassword", "legit@totallyreal.com");

        assertThrows(AlreadyTakenException.class, () ->
                registerService.register(username, "differentPassword", "fake@notlegit.com"));
    }
}
