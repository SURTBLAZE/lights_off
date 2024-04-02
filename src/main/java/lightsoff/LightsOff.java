package lightsoff;

import lightsoff.consoleui.ConsoleUI;
import lightsoff.core.Field;
import lightsoff.core.GameState;

public class LightsOff {
    public LightsOff() {
    }

    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        Field field;
        int level = 1;
        do{
            field = new Field(7,7,level + 1);
            field.generate();
            consoleUI.play(field);
            level++;
        }while(field.getGameState() == GameState.PLAYING);
    }
}
