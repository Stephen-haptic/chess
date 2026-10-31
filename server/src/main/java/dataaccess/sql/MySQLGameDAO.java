package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clear() {
        String sql = "DELETE FROM `game`";

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to clear games", ex);
        }
    }

    @Override
    public void storeGame(GameData game) {
        String sql = """
            INSERT INTO `game` (gameID, whiteUsername, blackUsername, gameName, game)
            VALUES (?, ?, ?, ?, ?)
            """;

        var serializer = new Gson();
        String gameJson = serializer.toJson(game.game());

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.setInt(1, game.gameID());
            statement.setString(2, game.whiteUsername());
            statement.setString(3, game.blackUsername());
            statement.setString(4, game.gameName());
            statement.setString(5, gameJson);

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to store game", ex);
        }
    }

    @Override
    public GameData findGame(int gameID) {
        String sql = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM `game`
                WHERE gameID = ?
                """;

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, gameID);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return readGame(result);
                }
            }
            return null;
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to find game", ex);
        }
    }

    @Override
    public List<GameData> listGames() {
        String sql = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM `game`
                """;

        ArrayList<GameData> gamesList = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql);
             var result = statement.executeQuery()) {
            while (result.next()) {
                gamesList.add(readGame(result));
            }
            return gamesList;
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to find games list", ex);
        }
    }

    @Override
    public void updateGame(GameData game) {
        String sql = """
            UPDATE `game`
            SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?
            WHERE gameID = ?
            """;

        var serializer = new Gson();
        String gameJson = serializer.toJson(game.game());

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, gameJson);
            statement.setInt(5, game.gameID());

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to update game", ex);
        }
    }

    private GameData readGame(ResultSet result) throws SQLException {
        int storedGameID = result.getInt("gameID");
        String storedWhiteUsername = result.getString("whiteUsername");
        String storedBlackUsername = result.getString("blackUsername");
        String storedGameName = result.getString("gameName");
        String storedGame = result.getString("game");

        Gson serializer = new Gson();
        ChessGame deserializedGame = serializer.fromJson(storedGame, ChessGame.class);

        return new GameData(
                storedGameID,
                storedWhiteUsername,
                storedBlackUsername,
                storedGameName,
                deserializedGame
        );
    }
}
