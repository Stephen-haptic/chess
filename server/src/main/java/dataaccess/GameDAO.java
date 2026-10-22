package dataaccess;

import model.GameData;
import java.util.List;

public interface GameDAO {
    void clear();
    void storeGame(GameData game);
    GameData findGame(int gameID);
    List<GameData> listGames();
    void updateGame(GameData game);
}
