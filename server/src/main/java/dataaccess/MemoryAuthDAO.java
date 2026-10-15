package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.HashMap;

public class MemoryAuthDAO implements  AuthDAO{
    private final Map<String, AuthData> authRecords = new HashMap<>();

    public void clear() {
        authRecords.clear();
    }
}
