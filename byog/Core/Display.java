package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import byog.Core.Serialization.Serializer;
import byog.Core.Entities.Hero;

import java.awt.Font;
import java.awt.Color;
import java.io.Serializable;

public class Display implements Serializable {
    private Game game;

    public Display(Game game) {
        this.game = game;
    }

    /**
     * This method asks the user to start a new game and then input their seed.
     * The seed that gets inputted is then used to generate a board, which gets
     * returned and then rendered.
     *
     * @return The board that will later get rendered to the screen.
     */
    public void displayPrompts() {
        displayStart();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == 'n') {
                    Long seed = requestSeed();
                    TETile[][] newBoard = Drawer.blankBoard();
                    Drawer.draw(newBoard, seed);
                    game.setWorld(new World(newBoard));
                    World world = game.getWorld();
                    world.spawnPlayer();
                    //world.spawnMonster();
                    return;
                } else if (c == 'l') {
                    Serializer savedGame = new Serializer();
                    Game loadGame = savedGame.deserialize(
                            "byog/Core/Serialization/SaveFiles/save-game.txt");
                    if (loadGame == null) {
                        System.exit(0);
                    }
                    game.setWorld(loadGame.getWorld());
                    game.setTer(loadGame.getTer());
                    return;
                } else if (c == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Displays the opening screen of the game, in which the game
     * requests that the player choose to start a new game, load a
     * pre-exisiting game, or quit the game.
     */
    private void displayStart() {
        StdDraw.clear(Color.black);
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.8, "Grigometh Hunter");
        font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.45, "New game (N)");
        StdDraw.text(0.5, 0.4, "Load game (L)");
        StdDraw.text(0.5, 0.35, "Quit (Q)");
    }

    /**
     * Requests that the user input their seed and then end
     * their seed with an s.
     */
    private long requestSeed() {
        displaySeedPrompt();
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c != 's') {
                    seed += Character.toString(c);
                    displaySeedPrompt();
                    StdDraw.text(0.5, 0.5, seed);
                } else {
                    break;
                }
            }
        }
        return Long.parseLong(seed);
    }

    private void displaySeedPrompt() {
        StdDraw.clear(Color.black);
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.8, "Please input seed");
        StdDraw.text(0.5, 0.6, "Once you have entered your seed, type"
                + " 'S' to begin the game");
    }

    public void displayHUD(TETile[][] map) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        int maxHeight = map[0].length;
        if (mouseY >= maxHeight) {
            mouseY = maxHeight - 1;
        }
        TETile hudTile = map[mouseX][mouseY];
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(7, 31, "Tile: " + hudTile.description());
        World world = game.getWorld();
        Hero player = world.getHero();
        int health = player.getHealth();
        StdDraw.text(15, 31, "HP: " + Integer.toString(health));
        int points = player.getPoints();
        StdDraw.text(26, 31, "Points: " + Integer.toString(points));
    }
}
