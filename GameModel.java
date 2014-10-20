package spacetrader;

import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import spacetrader.player.Player;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemNames;
import spacetrader.ui.Point;

/**
 * Acts as the singleton for the game, notably holding the universal player
 */
public class GameModel implements Serializable {
    private static GameModel state;
    private static final Random random = new Random();

    private int day;
    private Player player;
    private final StarSystem[] systems;

    private GameModel() {
        // Cannot be instantiated outside of this class
        systems = generateSystems();
    }
    
    public static void initialize() {
        state = new GameModel();
    }
    
    public static void load(InputStream in) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectIn = new ObjectInputStream(in)) {
            state = GameModel.class.cast(objectIn.readObject());
        }
    }
    
    public static void save(OutputStream out) throws IOException {
        try (ObjectOutputStream objectOut = new ObjectOutputStream(out)) {
            objectOut.writeObject(state);
        }
    }

    public static void setPlayer(Player player) {
        state.player = player;
    }

    public static Player getPlayer() {
        return state.player;
    }

    public static StarSystem[] getSystems() {
        return state.systems;
    }
    
    public static Random getRandom() {
        return random;
    }

    public static int getDay() {
        return state.day;
    }

    public static void setDay(int day) {
        state.day = day;
    }

    private StarSystem[] generateSystems() {
        StarSystem[] generatedSystems = new StarSystem[random.nextInt(5) + 7];
        List<Point> positions = new ArrayList<>();
        for (int x = 100; x <= 860; x += 190) {
            for (int y = 200; y <= 500; y += 150) {
                positions.add(new Point(x + random.nextInt(100) - 50, y + random.nextInt(100) - 50));
            }
        }
        Collections.shuffle(positions, random);
        for (int i = 0; i < generatedSystems.length; i++) {
            generatedSystems[i] = new StarSystem(StarSystemNames.getName(), positions.remove(0));
        }
        return generatedSystems;
    }
}