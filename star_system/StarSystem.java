package spacetrader.star_system;

import java.io.Serializable;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import spacetrader.GameModel;

/**
 * StarSystem contains an array of planets, and resides at some coordinates
 * @author David Purcell
 */
public class StarSystem implements Serializable {
    private final String name;
    private final Point2D coordinates;
    private final Planet[] planets;
    private final Color color;
    public boolean hasPlayer;

    public StarSystem(String name, Point2D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        hasPlayer = false;
        planets = new Planet[GameModel.getRandom().nextInt(6) + 4];
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet();
        }
        planets[GameModel.getRandom().nextInt(planets.length-1)].setTechLevel(Planet.TechLevel.HIGHTECH);
        color = Color.rgb(GameModel.getRandom().nextInt(56) + 200, GameModel.getRandom().nextInt(106) + 150, GameModel.getRandom().nextInt(25));
    }

    public String getName() {
        return name;
    }

    public Planet[] getPlanets() {
        return planets;
    }
    
    public Color getColor(){
        return color;
    }

    public Planet destroyPlanet(int i) {
        Planet dead = null;
        if (i < planets.length) {
            dead = planets[i];
            planets[i] = null;
        }
        return dead;
    }

    public Point2D getCoordinates() {
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