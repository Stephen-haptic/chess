package dataaccess;

import model.UserData;

import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void storeUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public UserData findUser(String username) {
        return users.get(username);
    }
}
