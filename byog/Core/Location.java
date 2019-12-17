package byog.Core;

import java.io.Serializable;

public class Location implements Serializable {
    private int coordX;
    private int coordY;

    public Location(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public boolean onBorder() {
        boolean onLeftBorder = coordX == 0 && coordY <= Game.HEIGHT;
        boolean onRightBorder = coordX == Game.WIDTH - 1 && coordY <= Game.HEIGHT;
        boolean onBottom = coordY == 0 && coordX <= Game.WIDTH - 1;
        boolean onTop = coordY == Game.HEIGHT - 1 && coordX <= Game.WIDTH - 1;
        return onLeftBorder || onRightBorder || onBottom || onTop;
        /*return coordX == 0 && coordY <= Game.HEIGHT ||
                coordX == Game.WIDTH - 1 && coordY <= Game.HEIGHT ||
                coordY == 0 && coordX <= Game.WIDTH - 1 ||
                coordY == Game.HEIGHT - 1 && coordX <= Game.WIDTH - 1;*/
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public Location[] destinations() {
        Location left = new Location(coordX - 1, coordY);
        Location right = new Location(coordX + 1, coordY);
        Location down = new Location(coordX, coordY - 1);
        Location up = new Location(coordX, coordY + 1);
        Location[] l = new Location[]{left, right, up, down};
        /*new Location(coordX - 1, coordY),
                new Location(coordX + 1, coordY),
                new Location(coordX, coordY + 1),
                new Location(coordX, coordY - 1)};*/
        return l;
    }

    private double square(double x) {
        return x * x;
    }

    /**
     * Returns distance between this Location and another Location.
     */
    public double distance(Location l) {
        return Math.sqrt(square(l.coordX - this.coordX) + square(l.coordY - this.coordY));
    }
}
