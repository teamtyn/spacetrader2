package spacetrader.star_system;

import java.io.Serializable;
import javafx.scene.paint.Color;
import spacetrader.GameModel;
import spacetrader.ui.Point;
import spacetrader.ui.SerializableColor;

/**
 * StarSystem contains an array of planets, and resides at some coordinates
 * @author David Purcell
 */
public class StarSystem implements Serializable {
    private final String name;
    private final Point coordinates;
    private final Planet[] planets;
    private final SerializableColor color;
    public boolean hasPlayer;

    public StarSystem(String name, Point coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        hasPlayer = false;
        planets = new Planet[GameModel.getRandom().nextInt(6) + 4];
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet();
        }
        // TODO: REMOVE THIS. Leaving in for now because fixing it before demo 7 isn't worth it. -Ryan
        planets[GameModel.getRandom().nextInt(planets.length-1)].setTechLevel(Planet.TechLevel.HIGHTECH);
        color = new SerializableColor(Color.rgb(
                GameModel.getRandom().nextInt(56) + 200,
                GameModel.getRandom().nextInt(106) + 150,
                GameModel.getRandom().nextInt(25)));
    }

    public String getName() {
        return name;
    }

    public Planet[] getPlanets() {
        return planets;
    }
    
    public Color getColor(){
        return color.getColor();
    }

    public Planet destroyPlanet(int i) {
        Planet dead = null;
        if (i < planets.length) {
            dead = planets[i];
            planets[i] = null;
        }
        return dead;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public double getCoordinateX() {
        return coordinates.getX();
    }

    public double getCoordinateY() {
        return coordinates.getY();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("System: ");
        builder.append(name)
            .append("\nStar System Coordinates:\n")
            .append(coordinates)
            .append("\nPlanets: \n");
        for (Planet planet: planets) {
            builder.append(planet).append("\n");
        }
        return builder.toString();
    }
}