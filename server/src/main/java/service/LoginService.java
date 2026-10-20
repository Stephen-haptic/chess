package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import exception.UnauthorizedException;
import exception.BadRequestException;

import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(String username, String password) {
        UserData user = userDAO.findUser(username);
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        else if(user == null || !password.equals(user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), username);
        authDAO.storeToken(authData);

        return authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
