package byog.Core.Serialization;

import byog.Core.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import byog.Core.World;

public class Serializer {

    /**
     * @source https://www.youtube.com/watch?v=FCRwbIXTFyk
     * Referenced video by Margret Posch on YouTube.
     * @param game
     * @param fileName
     */
    public void serialize(Game game, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(game);
        } catch (IOException ex) {
            //System.out.println("An error occurred while saving: ");
            //System.out.println(ex.getMessage());
        }
    }

    /**
     * Method for saving frames; use this for implementing playWithInputString.
     * @param world
     * @param fileName
     */
    public void serializeWorld(World world, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(world);
        } catch (IOException ex) {
            System.out.println("An error occurred while saving: ");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method for loading frames; use this for implementing playWithInputString.
     * @param world
     * @param fileName
     */
    public World deserializeWorld(World world, String fileName) {
        World loadFrame = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadFrame = (World) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Unable to load game: ");
            System.out.println(ex.getMessage());
        }
        return loadFrame;
    }

    public Game deserialize(String fileName) {
        Game savedGame = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            savedGame = (Game) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
        return savedGame;
    }
}

