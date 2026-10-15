package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.Map;
import java.util.HashMap;

public class MemoryDataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> authRecords = new HashMap<>();

    public void clearDatabase() {
        users.clear();
        games.clear();
        authRecords.clear();
    }
}
