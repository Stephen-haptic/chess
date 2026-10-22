package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import exception.BadRequestException;
import exception.UnauthorizedException;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameData createGame(String authToken, String gameName) {
        if (authToken == null || authToken.isBlank() || gameName == null || gameName.isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authDAO.findToken(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameID = 1234;
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(gameID, "", "", gameName, game);
        gameDAO.storeGame(gameData);

        return gameData;
    }
}
