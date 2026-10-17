package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;

import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(String username, String password, String email) {
        if (username == null || password == null || email == null) {
            throw new BadRequestException("All fields are required");
        }
        else if (username.isBlank() || password.isBlank() || email.isBlank()) {
            throw new BadRequestException("Field cannot be blank");
        }
        else if (userDAO.findUser(username) != null) {
            throw new AlreadyTakenException("Username already taken");
        }

        UserData user = new UserData(username, password, email);
        userDAO.storeUser(user);

        AuthData authData = new AuthData(generateToken(), username);
        authDAO.storeToken(authData);

        return authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
