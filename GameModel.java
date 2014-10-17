package spacetrader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import spacetrader.player.Player;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemNames;

/**
 * Acts as the singleton for the game, notably holding the universal player
 */
public class GameModel {
    private static final Random random = new Random();
    private static Player player;
    private static final StarSystem[] systems = generateSystems();

    private GameModel() {
        // Cannot be instantiated outside of this class
    }

    public static void setPlayer(Player player) {
        GameModel.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

    public static Random getRandom() {
        return random;
    }

    public static StarSystem[] getSystems() {
        return systems;
    }

    private static StarSystem[] generateSystems() {
        StarSystem[] generatedSystems = new StarSystem[random.nextInt(5) + 7];
        List<Point2D> positions = new ArrayList<>();
        for (int x = 100; x <= 860; x += 190) {
            for (int y = 200; y <= 500; y += 150) {
                positions.add(new Point2D(x + random.nextInt(100) - 50, y + random.nextInt(100) - 50));
            }
        }
        Collections.shuffle(positions, random);
        for (int i = 0; i < generatedSystems.length; i++) {
            generatedSystems[i] = new StarSystem(StarSystemNames.getName(), positions.remove(0));
        }
        return generatedSystems;
    }
}