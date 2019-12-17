package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.RoomsAndHallways.Room;
import byog.Core.RoomsAndHallways.Hallway;

import java.util.Random;

// DO LATER: For ambition, try implementing minimum spanning tree
// algorithm to make maps arguably better looking.
public class Drawer {
    public static final int WIDTH = Game.WIDTH;
    public static final int HEIGHT = Game.HEIGHT - 2;

    /**
     * This method creates a blank board filled with NOTHING
     * tiles.
     */
    public static TETile[][] blankBoard() {
        TETile[][] blankBoard = new TETile[WIDTH][HEIGHT + 2];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT + 2; y++) {
                blankBoard[x][y] = Tileset.NOTHING;
            }
        }
        return blankBoard;
    }

    /**
     * This is the method that draws the map on the board using
     * drawSection. It fills in each section with rooms and halls,
     * and then moves along the board from left to right, filling in
     * each section along the way.
     *
     * @param board the board on which everything is drawn
     * @param seed  the seed for the map
     */
    public static void draw(TETile[][] board, long seed) {
        Random randomNums = new Random(seed);
        for (int i = 0; i < WIDTH / 10 - 1; i++) {
            int x = i * 10;
            x = randomNums.nextInt(10) + x;
            drawSection(x, board, randomNums);
        }
    }

    /**
     * Draws a section of the maps that is 10 units wide. This
     * method adds rooms and randomly generated hallways. These hallways
     * can either be facing up or right; right facing hallways conenct
     * the sections together.
     *
     * @param x      the x coordinate at which this method starts
     * @param board  the board on which these sections are drawn
     * @param random a random number generator
     */
    private static void drawSection(int x, TETile[][] board, Random random) {
        int y = random.nextInt(5);
        // Decide what the max height of the section is.
        int maxHeight = random.nextInt(6) + (HEIGHT - 10);
        //Iteratively draw the room and hallways in each strip.
        while (y <= maxHeight) {
            /* Start by creating room's dimension */
            int minHeight = 4;
            int minWidth = 4;
            int roomHeight = random.nextInt(6) + minHeight;
            int roomWidth = random.nextInt(6) + minWidth;
            /*Then create a new Room instance */
            Room room = new Room(x, y, roomHeight, roomWidth);
            addRoom(room, board);
            /* Choose a random spot on top of the room to draw a hallway
             * that goes upwards */
            x = x + random.nextInt(roomWidth - 3);
            y += roomHeight - 2;
            /* Choose a random length for the hallway. */
            int hallLength = random.nextInt(10) + 5;
            /* Add the new upwards hallway*/
            Hallway upHall = new Hallway(x, y, hallLength, "up");
            addRoom(upHall, board);
            /* Adjust the y coordinate for the next location to start drawing a
             * hallway. */
            int newY = y + hallLength - 3;
            /* Choose a random spot on the hall to draw a new hallway. */
            y += random.nextInt(hallLength - 4) + 2;
            hallLength = random.nextInt(10) + 25;
            /* Finally, add the hallway to go to the right. */
            if (y < HEIGHT - 3 && x < WIDTH - 10) {
                Hallway rightHall = new Hallway(x, y, hallLength, "right");
                addRoom(rightHall, board);
            }
            y = newY;
        }
    }


    private static void addRoom(Room room, TETile[][] board) {
        int x = room.getX();
        int y = room.getY();
        //heroStartX = x + 1;
        //heroStartY = y + 1;
        int height = room.getHeight();
        int width = room.getWidth();
        makeWall(height, width, x, y, board);
        makeFloor(height - 2, width - 2, x + 1, y + 1, board);
    }

    /**
     * Draws a rectangle of floor tiles to represent the floor of a room.
     *
     * @param height how many rows tall the floor should be
     * @param width  how many columns wide the floor should be
     * @param x      the x coordinate of the bottom left corner
     * @param y      the y coordinate of the bottom left corner
     * @param board  the board on which everything is drawn.
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
     * @param height the height of the wall measured from bottom to top
     * @param width  the width of the wall measured from left to right
     * @param x      the x coordinate of the bottom left corner
     * @param y      the y coordinate of the bottom left corner
     * @param board  the board on which the walls are drawn
     */
    private static void makeWall(int height, int width, int x, int y, TETile[][] board) {
        makeRow(x, y, board, width, Tileset.WALL); //Bottom
        makeColumn(x, y + 1, board, height - 2, Tileset.WALL); //Left
        makeColumn(x + width - 1, y + 1, board, height - 2, Tileset.WALL); //Right
        makeRow(x, y + height - 1, board, width, Tileset.WALL); //Top
    }

    /**
     * Creates a row of a rectangle at coordinates (x, y). DOES NOT replace tiles
     * that were previously floor tiles; this prevents overlapping rooms and walls.
     *
     * @param x        the x coordinate of the rectangle
     * @param y        the y coordinate of the rectangle
     * @param board    the board on which the rectangle will be drawn.
     * @param rowWidth the width of the row to be generated
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
     * @param x      the x coordinate of the bottom left corner
     * @param y      the y coordinate of the bottom left corner
     * @param board  the board on which the column is drawn
     * @param length how long the column is
     * @param tile   the type of tile that the wall will use
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
}
