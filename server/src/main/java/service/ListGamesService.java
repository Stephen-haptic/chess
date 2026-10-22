package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import exception.BadRequestException;
import exception.UnauthorizedException;

import java.util.List;

public class ListGamesService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public List<GameData> listGames(String authToken) {
        AuthData authData = authDAO.findToken(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }
}
