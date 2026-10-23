package service;

import dataaccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import exception.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTests {
    private MemoryAuthDAO authDAO;
    private LogoutService logoutService;
    private AuthData testAuth;

    @BeforeEach
    void setup () {
        authDAO = new MemoryAuthDAO();
        logoutService = new LogoutService(authDAO);
        testAuth = new AuthData("testToken", "testUser");
        authDAO.storeToken(testAuth);
    }

    @Test
    @DisplayName("Successful Logout")
    public void logoutSuccess() {
        logoutService.logout("testToken");
        assertNull(authDAO.findToken("testToken"));
    }

    @Test
    @DisplayName("Unknown Token")
    public void logoutUnknownToken() {
        assertThrows(UnauthorizedException.class, () ->
                logoutService.logout("unknownToken"));

        assertNotNull(authDAO.findToken("testToken"));
    }

    @Test
    @DisplayName("Null or Blank Token")
    public void logoutNullOrBlankToken() {
        assertThrows(UnauthorizedException.class, () ->
                logoutService.logout(null));

        assertThrows(UnauthorizedException.class, () ->
                logoutService.logout(""));
    }
}
