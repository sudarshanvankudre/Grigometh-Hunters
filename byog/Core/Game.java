package byog.Core;

import byog.Core.Serialization.Serializer;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import java.io.Serializable;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 32;
    private World world;
    private Display display;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     *
     * @source: Referenced this lecture from the University of Pennsylvania to better
     * understand StdDraw and how it intercepts keyboard input:
     * http://www.cis.upenn.edu/~cis110/16su/lectures/10stddraw.pdf
     * @source: Referenced StdDraw's key listening API at this URL:
     * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html#hasNextKeyTyped()
     */
    public void playWithKeyboard() {
        display = new Display(this);
        display.displayPrompts(); //start up the world
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] board = world.getMap(); //not needed?
        ter.renderFrame(board);
        world.launchWorld(ter, display, this);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        char firstChar = Character.toLowerCase(input.charAt(0));
        Serializer loadAndSave = new Serializer();
        if (firstChar == 'n') {
            long seed = 0;
            int index = 1;
            char c = input.charAt(index);
            while (Character.toLowerCase(c) != 's') {
                char digit = (char) Character.getNumericValue(c);
                seed = seed * 10 + digit;
                index++;
                c = input.charAt(index);
            }
            TETile[][] finalWorldFrame = Drawer.blankBoard();
            Drawer.draw(finalWorldFrame, seed);
            boolean wantQuit = false;
            world = new World(finalWorldFrame);
            world.spawnPlayer();
            for (int i = index; i < input.length(); i++) {
                c = Character.toLowerCase(input.charAt(i));
                if (c == ':') {
                    wantQuit = true;
                } else if (c == 'q' && wantQuit) {
                    loadAndSave.serialize(this, "byog/Core/save-file.txt");
                    finalWorldFrame = world.getMap();
                    return finalWorldFrame;
                } else {
                    wantQuit = false;
                    world.runTick(c);
                }
            }
            // End game loop.
            loadAndSave.serialize(this, "byog/Core/save-file.txt");
            finalWorldFrame = world.getMap();
            return finalWorldFrame;
        } else if (firstChar == 'l') {
            Serializer loader = new Serializer();
            Game loadGame = loader.deserialize("byog/Core/save-file.txt");
            if (loadGame == null) {
                return Drawer.blankBoard();
            }
            world = loadGame.getWorld();
            boolean wantQuit = false;
            for (int i = 1; i < input.length(); i++) {
                char c = Character.toLowerCase(input.charAt(i));
                if (c == ':') {
                    wantQuit = true;
                } else if (c == 'q' && wantQuit) {
                    loadAndSave.serialize(this, "save-file.txt");
                } else {
                    wantQuit = false;
                    world.runTick(c);
                }
            }
            loadAndSave.serialize(this, "save-file.txt");
            TETile[][] finalWorldFrame = world.getMap();
            return finalWorldFrame;
        } else {
            System.exit(0);
            return null;
        }
    }

    /**
     * ##############
     * HELPER METHODS
     * ##############
     */


    /* Methods for getting and setting attributes of the game. */

    /**
     * This method returns the tile renderer of a game instance.
     * @return the game instance's tile renderer
     */
    public TERenderer getTer() {
        return ter;
    }

    /**
     * This method sets the game instances tile renderer to
     * a new TERenderer instance
     * @param newTer the new tile renderer for the game
     */
    public void setTer(TERenderer newTer) {
        this.ter = newTer;
    }

    /**
     * This method sets the game instances world to
     * a new World instance
     * @param newWorld the new world that is being given
     *                 to a game
     */
    public void setWorld(World newWorld) {
        this.world = newWorld;
    }

    /**
     * This method returns the world instance of a particular
     * game instance.
     * @return the game instance's world
     */
    public World getWorld() {
        return world;
    }
}

