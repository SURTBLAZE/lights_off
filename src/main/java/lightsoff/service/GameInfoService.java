package lightsoff.service;

import lightsoff.entity.GameInfo;

public interface GameInfoService {
    void addGameInfo(GameInfo gameInfo) throws GameInfoException;
    String getGameInfo(String game) throws GameInfoException;
    void reset() throws GameInfoException;
}
