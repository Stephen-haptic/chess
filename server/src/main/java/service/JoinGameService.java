package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import exception.BadRequestException;
import exception.UnauthorizedException;
import exception.AlreadyTakenException;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, String playerColor, int gameID) {
        if (authToken == null || authToken.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authDAO.findToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData game = gameDAO.findGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (playerColor.equals("WHITE")) {
            if (!game.whiteUsername().isEmpty()) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData newGame = new GameData(gameID, authData.username(), game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(newGame);
        }
        else if (playerColor.equals("BLACK")) {
            if (!game.blackUsername().isEmpty()) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData newGame = new GameData(gameID, game.whiteUsername(), authData.username(), game.gameName(), game.game());
            gameDAO.updateGame(newGame);
        }
        else {
            throw new BadRequestException("Error: bad request");
        }
    }
}
