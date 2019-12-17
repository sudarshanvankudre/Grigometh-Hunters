package byog.Core.Entities;

import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Hero extends Entity implements Serializable {
    private int points;

    public Hero(int x, int y) {
        posX = x;
        posY = y;
        tile = Tileset.UPPLAYER;
        points = 0;
    }

    public Bomb shoot() {
        if (direction == '^') {
            Bomb bomb = new Bomb(posX, posY + 1, "vertical");
            return bomb;
        } else if (direction == 'V') {
            Bomb bomb = new Bomb(posX, posY - 1, "vertical");
            return bomb;
        } else if (direction == '<') {
            Bomb bomb = new Bomb(posX - 1, posY, "horizontal");
            return bomb;
        } else if (direction == '>') {
            Bomb bomb = new Bomb(posX + 1, posY, "horizontal");
            return bomb;
        } else {
            Bomb bomb = new Bomb(posX, posY + 1, "vertical");
            return bomb;
        }
    }

    public void gainPoints() {
        this.points += 10;
    }

    /**
     * The hero's setDirection method, which also sets what
     * their tile is based on what direction they are facing.
     */
    @Override
    public void setDirection(char d) {
        direction = d;
        if (direction == '^') {
            tile = Tileset.UPPLAYER;
        } else if (direction == '<') {
            tile = Tileset.LEFTPLAYER;
        } else if (direction == 'V') {
            tile = Tileset.DOWNPLAYER;
        } else if (direction == '>') {
            tile = Tileset.RIGHTPLAYER;
        }
    }

    public int getPoints() {
        return points;
    }
}
