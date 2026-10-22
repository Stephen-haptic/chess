package dataaccess;

import model.GameData;

public interface GameDAO {
    void clear();
    void storeGame(GameData game);
    GameData findGame(int gameID);
    void updateGame(GameData game);
}
