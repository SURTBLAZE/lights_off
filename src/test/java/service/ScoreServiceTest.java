package service;

import lightsoff.entity.Score;
import lightsoff.service.ScoreService;
import lightsoff.service.ScoreServiceJDBC;
import lightsoff.service.ScoreServiceJPA;
import lightsoff.service.ScoreServiceRestClient;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

public class ScoreServiceTest {
    private ScoreService scoreService = new ScoreServiceJPA();

    @Test
    public void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("Lights off").size());
    }

    @Test
    public void addScore() {
        var date = new Date();
        var score = new Score("Lights off", "Valeri", 19, new Timestamp(date.getTime()));
        scoreService.addScore(score);
        var scores = scoreService.getTopScores("Lights off");
        assertEquals(1, scores.size());
        assertEquals("Valeri", scores.get(0).getPlayer());
        assertEquals("Lights off", scores.get(0).getGame());
        assertEquals(19, scores.get(0).getPoints());
        assertEquals(new Timestamp(date.getTime()), scores.get(0).getPlayedOn());
    }

    @Test
    public void getTopScores() {
        var date = new Date();
        scoreService.reset();
        scoreService.addScore(new Score("Lights off", "Jaroslav", 17, new Timestamp(date.getTime())));
        scoreService.addScore(new Score("Lights off", "Valeri", 19, new Timestamp(date.getTime())));
        scoreService.addScore(new Score("Lights off", "Alla", 18, new Timestamp(date.getTime())));

        var scores = scoreService.getTopScores("Lights off");
        assertEquals(3, scores.size());
        assertEquals("Valeri", scores.get(0).getPlayer());
        assertEquals(19, scores.get(0).getPoints());
        assertEquals("Alla", scores.get(1).getPlayer());
        assertEquals(18, scores.get(1).getPoints());
        assertEquals("Jaroslav", scores.get(2).getPlayer());
        assertEquals(17, scores.get(2).getPoints());
    }
}
