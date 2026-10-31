package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO{
    @Override
    public void clear() {
        String sql = "DELETE FROM `auth`";

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to clear authTokens", ex);
        }
    }

    @Override
    public void storeToken(AuthData authData) {
        String sql = """
            INSERT INTO `auth` (authToken, username)
            VALUES (?, ?)
            """;

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {

            statement.setString(1, authData.authToken());
            statement.setString(2, authData.username());

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to store authToken", ex);
        }
    }

    @Override
    public AuthData findToken (String authToken) {
        String sql = """
                SELECT authToken, username
                FROM `auth`
                WHERE authToken = ?
                """;

        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, authToken);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String storedToken = result.getString("authToken");
                    String storedUsername = result.getString("username");

                    return new AuthData(
                            storedToken,
                            storedUsername
                    );
                }
            }
            return null;
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to find authToken", ex);
        }
    }

    @Override
    public void removeToken(String authToken) {
        String sql = "DELETE FROM `auth` WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement(sql)) {
            statement.setString(1, authToken);

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to remove authToken", ex);
        }
    }
}
