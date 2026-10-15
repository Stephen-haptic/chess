package dataaccess;

import model.GameData;

import java.util.Map;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }
}
