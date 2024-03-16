package tests.core;

import lightsoff.core.Field;
import lightsoff.core.TileState;
import org.junit.Test;

import javax.swing.plaf.PanelUI;

import static org.junit.Assert.*;
public class FieldTest {
    @Test
    public void generateFieldBadParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new Field(7, 7, 60));
    }

    @Test
    public void switchTileBlock(){
        var field = new Field(7,7,4);
        assertFalse(field.getField()[0][0].getState());
        field.switchTileBlock(0,0);
        assertTrue(field.getField()[0][0].getState());
    }

    @Test
    public void isSolved(){
        var field = new Field(7,7,4);
        field.generate();
        field.clear();
        assertTrue(field.isSolved());
    }

}
