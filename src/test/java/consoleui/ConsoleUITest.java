package tests.consoleui;
import lightsoff.consoleui.ConsoleUI;
import lightsoff.core.Field;
import lightsoff.core.GameState;
import lightsoff.service.ScoreServiceJDBC;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsoleUITest {
    @Test
    public void saveScore(){
        var consoleUI = new ConsoleUI();
        consoleUI.setUsername("Valeri");
        consoleUI.setTotalScore(100);
        var scoreService = new ScoreServiceJDBC();
        scoreService.reset();
        consoleUI.saveScore(scoreService);

        var scores = scoreService.getTopScores("Lights off");
        assertEquals(1,scores.size());
        assertEquals("Valeri",scores.get(0).getPlayer());
        assertEquals(100,scores.get(0).getPoints());
    }

}
