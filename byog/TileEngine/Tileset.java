package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER = new TETile('@', Color.white, Color.black, "player");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile UPPLAYER = new TETile('^', Color.WHITE, Color.BLACK,
            "up-facing player");
    public static final TETile RIGHTPLAYER = new TETile('>', Color.WHITE, Color.BLACK,
            "right-facing player");
    public static final TETile DOWNPLAYER = new TETile('v', Color.WHITE, Color.BLACK,
            "down-facing player");
    public static final TETile LEFTPLAYER = new TETile('<', Color.WHITE, Color.BLACK,
            "left-facing player");
    public static final TETile SAHAI = new TETile('A', Color.WHITE, Color.BLACK,
            "Natural mathematical curiosity", "byog/TileEngine/TileImages/AnantSahai.jpg");
    public static final TETile MONSTER = new TETile('X', Color.RED, Color.BLACK,
            "Grigometh");
    public static final TETile BOMB = new TETile('O', Color.YELLOW, Color.BLACK, "Bomb");
}


