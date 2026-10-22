package service;

import dataaccess.AuthDAO;
import model.AuthData;
import exception.UnauthorizedException;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) {
        AuthData authData = authDAO.findToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.removeToken(authToken);
    }
}
