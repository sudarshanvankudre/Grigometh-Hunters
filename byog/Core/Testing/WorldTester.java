package byog.Core.Testing;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.TileEngine.TERenderer;
import byog.Core.Game;

import java.util.Random;

/**
 * This class is used for testing the world generation algorithm. To display the world
 * that the algorithm generates, run the main method, which in turn calls the draw method.
 * The draw method is the method that executes the algorithm by drawing upon a series of
 * helper functions to create walls, hallways, and floors.
 * <p>
 * February 20, 2018: Uploaded rough draft of world generation algorithm. May still contain bugs.
 * Frequently generates maps that do not span the entire width.
 * However, does support intersections and rooms of varying shapes. Everything
 * is random.
 */
public class WorldTester {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    /**
     * This method draws the rooms and hallways on the board.
     *
     * @param board: the board on which everything is drawn.
     */
    public static void draw(TETile[][] board, long seed) {
        Random randomNums = new Random(seed);
        for (int i = 0; i < WIDTH / 10 - 1; i++) {
            int x = i * 10;
            drawSection(x, board, randomNums);
        }
    }

    /**
     * Draws a section of the maps that is 10 units wide.
     *
     * @param x
     * @param board
     * @param random
     */
    public static void drawSection(int x, TETile[][] board, Random random) {
        int y = 0; //increment y
        int firstLength = random.nextInt(11);
        addHallway(firstLength, x, y, board, "right");
        y += 1;
        // Decide what the max height of the section is.
        int maxHeight = random.nextInt(20) + (HEIGHT - 20);
        //Iteratively draw the room and hallways in each strip.
        while (y <= maxHeight) {
            int minHeight = 4;
            int minWidth = 4;
            int roomHeight = random.nextInt(6) + minHeight;
            int roomWidth = random.nextInt(6) + minWidth;
            addRoom(roomHeight, roomWidth, x, y, board);
            // choose random spot on top of room to draw a hallway
            x = x + random.nextInt(roomWidth - 3);
            y += roomHeight - 3;
            int hallLength = random.nextInt(10) + 4;
            addHallway(hallLength, x, y, board, "up");
            int newY = y + hallLength - 3;
            //choose a random spot on the room to draw a right facing hallway
            y += random.nextInt(hallLength - 3);
            int rightHallLength = random.nextInt(20) + 5;
            addHallway(rightHallLength, x, y, board, "right");
            y = newY;
        }
    }

    /**
     * Draws hallways by treating hallways as rooms with the dimensions 1 x N or N x 1.
     * This method continues to add floor tiles and wall tiles until the hallway intersects
     * with another hallway or a room.
     *
     * @param board
     */
    private static void addHallway(int length, int x, int y, TETile[][] board, String direction) {
        if (direction.equals("up")) {
            addRoom(length, 3, x, y, board);
        } else if (direction.equals("left")) {
            addRoom(3, length, x - (length - 2), y, board);
        } else if (direction.equals("right")) {
            addRoom(3, length, x, y, board);
        } else if (direction.equals("down")) {
            addRoom(length, 3, x, y - (length - 2), board);
        } else {
            return;
        }
    }

    /**
     * Creates a rectangular room. Constructs a border of walls and then fills the
     * walls with floor tiles.
     *
     * @param height: how tall room is on screen
     * @param width:  width of the room
     * @param x:      x coordinate of bottom left corner
     * @param y:      y coordinate of bottom left corner
     * @param board:  the board on which everything is drawn.
     */
    private static void addRoom(int height, int width, int x, int y, TETile[][] board) {
        makeWall(height, width, x, y, board);
        makeFloor(height - 2, width - 2, x + 1, y + 1, board);
    }

    /**
     * Draws a rectangle of floor tiles to represent the floor of a room.
     *
     * @param height: how many rows tall the floor should be
     * @param width:  how many columns wide the floor should be
     * @param x:      the x coordinate of the bottom left corner
     * @param y:      the y coordinate of the bottom left corner
     * @param board:  the board on which everything is drawn.
     */
    private static void makeFloor(int height, int width, int x, int y, TETile[][] board) {
        int xPos = x;
        int yPos = y;
        for (int i = 0; i < height; i++) {
            makeRow(xPos, yPos, board, width, Tileset.FLOOR);
            yPos += 1;
        }
    }

    /**
     * Draws a wall with the given dimensions.
     *
     * @param length
     * @param width
     * @param x
     * @param y
     * @param board
     */
    private static void makeWall(int length, int width, int x, int y, TETile[][] board) {
        makeRow(x, y, board, width, Tileset.WALL); //Bottom
        makeColumn(x, y + 1, board, length - 2, Tileset.WALL); //Left
        makeColumn(x + width - 1, y + 1, board, length - 2, Tileset.WALL); //Right
        makeRow(x, y + length - 1, board, width, Tileset.WALL); //Top
    }

    /**
     * Creates a row of a rectangle at coordinates (x, y). DOES NOT replace tiles
     * that were previously floor tiles; this prevents overlapping rooms and walls.
     *
     * @param x:        the x coordinate of the rectangle
     * @param y:        the y coordinate of the rectangle
     * @param board:    the board on which the rectangle will be drawn.
     * @param rowWidth: the width of the row to be generated
     * @return
     */
    private static void makeRow(int x, int y, TETile[][] board, int rowWidth, TETile tile) {
        if (x <= 0) {
            rowWidth += x;
            x = 0;
        } else if (x >= WIDTH) {
            x = WIDTH - 1;
        }
        if (y <= 0) {
            y = 0;
        } else if (y >= HEIGHT) {
            y = HEIGHT - 1;
        }

        int start = x;
        int end = x + rowWidth - 1;
        if (end >= WIDTH) {
            end = WIDTH - 1;
        }
        for (int i = start; i <= end; i++) {
            if (i == WIDTH - 1 || y == HEIGHT - 1) {
                board[i][y] = Tileset.WALL;
            } else if (i == 0 || y == 0) {
                board[i][y] = Tileset.WALL;
            } else if (board[i][y] != Tileset.FLOOR) {
                board[i][y] = tile;
            }
        }
    }

    /**
     * Creates a column made up of a particular tile. DOES NOT replace tiles that were
     * previously floor tiles in order to prevent overlap.
     *
     * @param x
     * @param y
     * @param board
     * @param length
     * @param tile
     */
    private static void makeColumn(int x, int y, TETile[][] board, int length, TETile tile) {
        if (x <= 0) {
            x = 0;
        } else if (x >= WIDTH) {
            x = WIDTH - 1;
        }
        if (y <= 0) {
            length += y;
            y = 0;
        } else if (y >= HEIGHT) {
            y = HEIGHT - 1;
        }

        int start = y;
        int end = y + length - 1;
        if (end >= HEIGHT) {
            end = HEIGHT - 1;
        }
        for (int i = start; i <= end; i++) {
            if (i == HEIGHT - 1 || x == WIDTH - 1) {
                board[x][i] = Tileset.WALL;
            } else if (i == 0 || x == 0) {
                board[x][i] = Tileset.WALL;
            } else if (board[x][i] != Tileset.FLOOR) {
                board[x][i] = tile;
            }
        }
    }

    /**
     * This main method is used to run the world generation algorithm.
     * It also contains code that mirrors code
     *
     * @param args
     */
    public static void main(String[] args) {
        /* Testing new Drawer class */
        /*TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        //initialize board
        TETile[][] board = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = Tileset.NOTHING;
            }
        }
        String x = "Oh no";
        Drawer.draw(board, 143);
        ter.renderFrame(board);/*


        /* TESTING GAME.JAVA */
        /* THE CODE BELOW TESTS PLAYWITHINPUTSTRING */
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        Game game = new Game();
        TETile[][] board = game.playWithInputString("lssssssssss");
        ter.renderFrame(board);
    }
}







