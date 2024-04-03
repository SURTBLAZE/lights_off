package lightsoff;

import lightsoff.consoleui.ConsoleUI;
import lightsoff.core.Field;
import lightsoff.core.GameState;
import org.springframework.beans.factory.annotation.Autowired;

public class Game {
    @Autowired
    private ConsoleUI consoleUI;

    @Autowired
    private Field field;

    public void startGame(){
        int level = 1;
        do{
            field = new Field(7,7,level + 1);
            field.generate();
            consoleUI.play(field);
            level++;
        }while(field.getGameState() == GameState.PLAYING);
    }
}
