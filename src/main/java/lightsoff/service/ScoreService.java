package lightsoff.service;

import lightsoff.entity.Score;
import lightsoff.consoleui.ConsoleUI;
import lightsoff.core.Field;
import lightsoff.core.GameState;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
