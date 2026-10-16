package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void storeUser(UserData user);
    UserData findUser(String username);
}
