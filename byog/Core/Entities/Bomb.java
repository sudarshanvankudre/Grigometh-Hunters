package byog.Core.Entities;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Bomb {
    private int posX;
    private int posY;
    TETile tile;

    public Bomb(int x, int y, String direction) {
        posX = x;
        posY = y;
        tile = Tileset.BOMB;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public TETile getTile() {
        return tile;
    }
}
