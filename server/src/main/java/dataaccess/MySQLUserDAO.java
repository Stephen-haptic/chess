package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    @Override
    public void clear() {
    }

    @Override
    public void storeUser(UserData user) {
        String sql = """
            INSERT INTO `user` (username, password, email)
            VALUES (?, ?, ?)
            """;

        try (var conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());

            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to store user", ex);
        }
    }

    @Override
    public UserData findUser(String username) {
        String sql = """
                SELECT username, password, email
                FROM `user`
                WHERE username = ?
                """;

        try (var conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    String storedUsername = result.getString("username");
                    String storedPassword = result.getString("password");
                    String storedEmail = result.getString("email");

                    return new UserData(
                            storedUsername,
                            storedPassword,
                            storedEmail
                    );
                }
            }

            return null;
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Failed to find user", ex);
        }
    }

    @Override
    public boolean verifyPassword (String username, String password) {
        UserData user = findUser(username);

        if (user == null || password == null) {
            return false;
        }

        return BCrypt.checkpw(password, user.password());
    }
}
