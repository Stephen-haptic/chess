package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.HashMap;

public class MemoryAuthDAO implements  AuthDAO{
    private final Map<String, AuthData> authRecords = new HashMap<>();

    @Override
    public void clear() {
        authRecords.clear();
    }

    @Override
    public void storeToken(AuthData authData) {
        authRecords.put(authData.authToken(), authData);
    }

    @Override
    public AuthData findToken (String authToken) {
        return authRecords.get(authToken);
    }

    @Override
    public void removeToken(String authToken) {
        authRecords.remove(authToken);
    }
}
