package byog.Core;

import byog.Core.Entities.Hero;
import byog.Core.Serialization.Serializer;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.Entities.Monster;
import byog.Core.Entities.Bomb;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class World implements Serializable {
    private TETile[][] map;
    public static final int HEIGHT = Game.HEIGHT;
    public static final int WIDTH = Game.WIDTH;
    private ArrayList<Monster> monsters = new ArrayList<>(10);
    private Hero hero;
    private Random randomLoc = new Random(612);

    public World(TETile[][] world) {
        map = world;
    }

    /**
     * This method chooses a floor tile to spawn the player. This tile
     * is chosen by starting at the coordinates (5, 10) on the board, and then
     * moving right until a floor tile is found. The floor tile
     * that gets chosen becomes the player's starting point.
     */
    public void spawnPlayer() {
        if (hero == null) {
            int startX = 5;
            int startY = 10;
            while (!map[startX][startY].equals(Tileset.FLOOR)) {
                startX++;
            }
            hero = new Hero(startX, startY);
            map[startX][startY] = hero.getTile();
        }
    }

    /**
     * This method chooses a random floor tile to spawn monsters. The floor tile
     * that gets chosen becomes the monster's starting point.
     */
    public void spawnMonster() {
        if (monsters.size() < 5) {
            int startX = 15;
            int startY = 15;
            int heroX = hero.getPosX();
            int heroY = hero.getPosY();
            while (!map[startX][startY].equals(Tileset.FLOOR) || (startX == heroX && startY == heroY)) {
                startX = randomLoc.nextInt(WIDTH);
                startY = randomLoc.nextInt(HEIGHT);
            }
            Monster monster = new Monster(startX, startY);
            monsters.add(monster);
            map[startX][startY] = monster.getTile();
        }
    }

    /**
     * This method runs the game loop.
     * @param ter the tile renderer that displays the maps
     * @param display the display object used for the HUD
     * @param game the game instance that contains the hero and monsters
     */
    public void launchWorld(TERenderer ter, Display display, Game game) {
        boolean wantQuit = false;
        for (int i = 0; i < 5; i++) {
            spawnMonster();
        }
        long moveOrNot = 0L;
        while (true) {
            //spawnMonster();
            if (winCondition()) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(40, 10, "YOU WIN");
                StdDraw.show();
                Serializer saveGame = new Serializer();
                saveGame.serialize(game, "byog/Core/Serialization/SaveFiles/save-game.txt");
                break;
            }
            if (loseCondition()) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(40, 10, "YOU GOT EATEN BY A GRIGOMETH");
                StdDraw.show();
                Serializer saveGame = new Serializer();
                saveGame.serialize(game, "byog/Core/Serialization/SaveFiles/save-game.txt");
                break;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == ':') {
                    wantQuit = true;
                } else if (c == 'q' && wantQuit) {
                    Serializer saveGame = new Serializer();
                    saveGame.serialize(game, "byog/Core/Serialization/SaveFiles/save-game.txt");
                    System.exit(0);
                } else {
                    wantQuit = false;
                    runTick(c);
                }
            }
            //Monster's movement
            for (int i = 0; i < monsters.size(); i++) {
                Monster m = monsters.get(i);
                if (m != null && !m.isDead()) {
                    if (moveOrNot % 20 == 0) {
                        runMonsterTick(m);
                        hurtPlayer(m);
                    }
                    if (monsterOnHero(m)) {
                        monsters.remove(m);
                        spawnMonster();
                    }
                } else {
                    monsters.remove(m);
                    hero.gainPoints();
                    spawnMonster();
                }
            }
            moveOrNot++;
            //End Monster's movement
            ter.renderFrame(map);
            display.displayHUD(map);
            StdDraw.show();
            StdDraw.pause(1000 / 60);
        }
    }

    /**
     * Method that makes this world run through one tic. Stops running if winCondition is met.
     * @param c the character input that the player enters.
     */
    public void runTick(char c) {
        if (!winCondition()) {
            runHeroTick(c);
        }
    }

    /** Performs the actions of the player, including moving and
     *  placing bombs.
     *  @param c the player's input
     */
    private void runHeroTick(char c) {
        int heroX = hero.getPosX();
        int heroY = hero.getPosY();
        int oldX = heroX;
        int oldY = heroY;
        if (c == 'a' && isValidMove(oldX - 1, oldY)) {
            hero.move(oldX - 1, oldY);
            heroX = hero.getPosX();
            hero.setDirection('<');
            map[oldX][oldY] = Tileset.FLOOR;
        } else if (c == 'w' && isValidMove(oldX, oldY + 1)) {
            hero.move(oldX, oldY + 1);
            heroY = hero.getPosY();
            hero.setDirection('^');
            map[oldX][oldY] = Tileset.FLOOR;
        } else if (c == 'd' && isValidMove(oldX + 1, oldY)) {
            hero.move(oldX + 1, oldY);
            heroX = hero.getPosX();
            hero.setDirection('>');
            map[oldX][oldY] = Tileset.FLOOR;
        } else if (c == 's' && isValidMove(oldX, oldY - 1)) {
            hero.move(oldX, oldY - 1);
            heroY = hero.getPosY();
            hero.setDirection('V');
            map[oldX][oldY] = Tileset.FLOOR;
        } else if (c == ' ') {
            Bomb projectile = hero.shoot();
            int projX = projectile.getPosX();
            int projY = projectile.getPosY();
            if (map[projX][projY].equals(Tileset.FLOOR)) {//!map[projX][projY].equals(Tileset.MONSTER) || !map[projX][projY].equals(Tileset.WALL)) {
                map[projX][projY] = projectile.getTile();
            }
        }
        for (Monster m : monsters) {
            hurtPlayer(m);
        }
        map[heroX][heroY] = hero.getTile();
    }

    /**
     * This method moves the monster while also setting its direction. It then
     * changes tiles to reflect the monster's movement.
     *
     * @param m the monster which is being moved and which is performing actions
     *          in a particular tick
     */
    private void runMonsterTick(Monster m) {
        int oldX = m.getPosX();
        int oldY = m.getPosY();
        m.move(map);
        m.setDirection();
        map[m.getPosX()][m.getPosY()] = m.getTile();
        if (monsterOnHero(m)) {
            monsters.remove(m);
            map[oldX][oldY] = Tileset.FLOOR;
            map[m.getPosX()][m.getPosY()] = hero.getTile();
        } else {
            map[oldX][oldY] = Tileset.FLOOR;
        }
    }

    /**
     * This method checks if a monster is on top of a hero, i.e.
     * the monster and hero are occupying the same tile.
     * @param m the monster which is being checked to see if it is
     *          on top of the player
     * @return true if m is on top of the player.
     */
    private boolean monsterOnHero(Monster m) {
        return m.getPosX() == hero.getPosX() && m.getPosY() == hero.getPosY();
    }

    /**
     * Checks to see if the monster m is occupying the same location as the
     * player, and then deducts health from the player if this is the case.
     * @param m the monster which is being checked to see if it damages the
     *          player.
     */
    private void hurtPlayer(Monster m) {
        if (monsterOnHero(m)) {
            hero.loseHealth();
        }
    }

    /**
     * Returns true if the player's move won't cause them to hit the wall or go
     * off the map.
     *
     * @param x the x coordinate the player wants to move to
     * @param y the y coordinate the player wants to move to
     * @return true if player won't hit a wall and is in bounds
     */
    public boolean isValidMove(int x, int y) {
        boolean willHitWall = map[x][y].equals(Tileset.WALL);
        boolean inXBounds = (x >= 0 && x <= WIDTH - 1);
        boolean inYBounds = (y >= 0 && y <= HEIGHT - 1);
        return !willHitWall && inXBounds && inYBounds;
    }

    /**
     * This method returns the map of the world; this map represents the most recent
     * state of the world.
     *
     * @return the map of a particular world instance
     */
    public TETile[][] getMap() {
        return map;
    }

    /**
     * A method that returns the hero of a world.
     * @return the world instance's hero.
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Method that returns true if win condition has been met, false otherwise.
     */
    public boolean winCondition() {
        return hero.getPoints() >= 50;
    }

    /**
     * Method that returns true if the player has lost, false otherwise.
     * @return true when player's health gets down to 0 or lower.
     */
    public boolean loseCondition() {
        return hero.getHealth() <= 0;
    }
}
