package byog.Core.Entities;

import byog.TileEngine.TETile;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected int posX;
    protected int posY;
    protected int health = 100;
    protected char direction;
    protected TETile tile;
    protected boolean dead;

    /**
     * Moves the player to the specified x and y coordinates.
     * @param x the x coordinate of the new location
     * @param y the y coordinate of the new location.
     */
    public void move(int x, int y) {
        posX = x;
        posY = y;
    }

    /**
     * This method deducts health from an entity.
     */
    public void loseHealth() {
        //Damage was formerly 1.
        //this.health -= 1;
        this.health -= 10;
    }

    /**
     * Public method for retrieving the player's x coordinate.
     * @return the player's x coordinate
     */
    public int getPosX() {
        return posX;
    }

    /**
     * This is the public method for retrieving the player's y coordinate.
     * @return the player's y coordinate
     */
    public int getPosY() {
        return posY;
    }

    /**
     * This public method for modifies the player's x direction based on
     * a character that is passed in.
     * @param d the character if the player's movement
     */
    public void setDirection(char d) {
        direction = d;
    }

    /**
     * Public method for retrieving the tile that represents the player.
     * @return the player's tile representation
     */
    public TETile getTile() {
        return tile;
    }

    /**
     * This method returns the direction in which the player is facing.
     * @return the player's direction
     */
    public char getDirection() {
        return direction;
    }

    /**
     * This method returns how much health the player has left.
     * @return the player's health
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @param x
     */
    public void setPosX(int x) {
        posX = x;
    }

    public void setPosY(int y) {
        posY = y;
    }

    public void die() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }
}
