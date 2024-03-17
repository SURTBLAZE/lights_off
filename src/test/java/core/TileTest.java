package core;

import lightsoff.core.Tile;
import org.junit.Test;

import static org.junit.Assert.*;
public class TileTest {

    @Test
    public void switchTile(){
        var tile = new Tile(false);
        tile.switchTile();
        assertTrue(tile.getState());
    }
}
