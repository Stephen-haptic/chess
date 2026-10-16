package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void storeToken(AuthData authData);
    AuthData findToken(String authToken);
    void removeToken(String authToken);
}
