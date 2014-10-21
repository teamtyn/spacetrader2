package spacetrader2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import spacetrader2.player.Player;
import spacetrader2.star_system.StarSystem;
import spacetrader2.star_system.StarSystemNames;

/**
 *
 * @author Administrator
 */
public class GameModel {
    private static GameModel model = new GameModel();
    private Player player;
    private StarSystem[] systems;
    
    
    private GameModel() {
        //Single instance allowed.
    }
    
    public static GameModel getGameModel() {
        return model;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public StarSystem[] getSystems() {
        return systems;
    }
    
    public void generateSystems() {
        Random random = new Random();
        List<Point2D> positions = new ArrayList<>();
        for (int x = 0; x <= 2000; x += 350) {
            for (int y = 0; y <= 2000; y += 350) {
                positions.add(new Point2D(x + random.nextInt(100) - 50, y + random.nextInt(100) - 50));
            }
        }
        Collections.shuffle(positions, random);
        
        systems = new StarSystem[10];
        for (int i = 0; i < systems.length; i++) {
            systems[i] = new StarSystem(StarSystemNames.getName(), positions.remove(0));
        }
        player.setSystem(systems[0]);
        player.setPlanet(player.getSystem().getPlanets()[0]);
        player.addKnownPlanet(player.getPlanet());
    }
    
}
