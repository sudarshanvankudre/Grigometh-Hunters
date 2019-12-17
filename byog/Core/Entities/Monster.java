package byog.Core.Entities;

import byog.Core.Game;
import byog.Core.Location;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Monster extends Entity implements Serializable {
    private int oldX;
    private int oldY;
    private Location[] possibleDests;
    private Random randomGen = new Random(413);

    /**
     * Public constructor for monster instances.
     * @param x the monster's first x coordinate
     * @param y the monster's first y coordinate
     */
    public Monster(int x, int y) {
        posX = x;
        posY = y;
        oldX = x;
        oldY = y;
        possibleDests = new Location(x, y).destinations();
        this.tile = Tileset.MONSTER;
    }

    /**
     * The monster's setDirection method, which also sets what
     * their tile is based on what direction they are facing.
     */
    /*public void setDirectionTile() {
        if (direction == '^') {
            tile = Tileset.UPMONSTER;
        } else if (direction == '<') {
            tile = Tileset.LEFTMONSTER;
        } else if (direction == 'V') {
            tile = Tileset.DOWNMONSTER;
        } else if (direction == '>') {
            tile = Tileset.RIGHTMONSTER;
        }
    }*/

    /**
     * Method that makes the monster move per tic.
     * @param map the map on which the monster moves
     */
    public void move(TETile[][] map) {
        int randLoc = randomGen.nextInt(4);
        Location possible = possibleDests[randLoc];
        while (!withinBounds(possible)
                || map[possible.getCoordX()][possible.getCoordY()].equals(Tileset.WALL)) {
            randLoc = randomGen.nextInt(4);
            possible = possibleDests[randLoc];
        }
        oldX = posX;
        oldY = posY;
        int possibleX = possible.getCoordX();
        int possibleY = possible.getCoordY();
        if (map[possibleX][possibleY].equals(Tileset.BOMB)) {
            dead = true;
            return;
        }
        posX = possible.getCoordX();
        posY = possible.getCoordY();
        possibleDests = possible.destinations();
    }

    /**
     * Method that randomly chooses between two integers c1 and c2.
     * @param c1 an integer
     * @param c2 another integer that is may be chosen
     */
    private int randomChoice(int c1, int c2) {
        Random gen = new Random(2923239);
        byte rand = (byte) gen.nextInt(1);
        if (rand == 1) {
            return c1;
        }
        return c2;
    }

    /**
     * Returns true if x and y are diagonally situated from the current x and y
     * coordinates of the monster.
     * @param x the x coordinate that is being compared to the monster's x
     *          coordinate
     * @param y the y coordinate that is being compared to the monster's x
     *          coordinate
     */
    private boolean isDiagonal(int x, int y) {
        return !((x == posX + 1 && y == posY + 1)
                   || (x == posX + 1 && y == posY - 1)
                   || (x == posX - 1 && y == posY - 1)
                   || (x == posX - 1 && y == posY + 1));
    }

    /**
     * Method that sets the direction the monster is facing.
     */
    public void setDirection() {
        if (posY > oldY) {
            direction = '^';
        } else if (posY < oldY) {
            direction = 'V';
        } else if (posX > oldX) {
            direction = '>';
        } else if (posX < oldX) {
            direction = '<';
        }
    }

    /**
     * Returns true if coordinates x and y are within the bounds of the underlying
     * array supporting the map.
     * @param x an x coordinate of a location
     * @param y a y coordinate of a location
     * @return true if the x and y coordinates are within the bounds of the map
     */
    private boolean withinBounds(int x, int y) {
        return x >= 0 && x <= Game.WIDTH - 1 && y >= 0 && y <= Game.HEIGHT;
    }

    /**
     * Returns true if Location l is within bounds of the map.
     * @param l the location of a monster
     * @return true if the location l is within bounds
     */
    private boolean withinBounds(Location l) {
        return l.getCoordX() >= 0 && l.getCoordX() <= Game.WIDTH - 1
                && l.getCoordY() >= 0 && l.getCoordY() <= Game.HEIGHT;
    }

    /**
     * This method retrieves the x coordinate that was previously
     * the monster's x coordinate.
     * @return the monster's old x coordinate.
     */
    public int getOldX() {
        return oldX;
    }

    /**
     * This method retrieves the y coordinate that was previously
     * the monster's y coordinate.
     * @return the monster's old y coordinate.
     */
    public int getOldY() {
        return oldY;
    }

}
