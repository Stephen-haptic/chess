package dataaccess;

import model.GameData;

import java.util.Map;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void storeGame(GameData game) {
        games.put(game.gameID(), game);
    }

    @Override
    public GameData findGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData game) {

    }
}
