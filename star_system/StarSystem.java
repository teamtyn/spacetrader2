package spacetrader.star_system;

import java.util.Random;
import javafx.geometry.Point2D;

/**
 * StarSystem contains an array of planets, and resides at some coordinates
 * @author David Purcell
 */
public class StarSystem {
    private final String name;
    private final Point2D coordinates;
    private Planet[] planets;
    private Random random = new Random();

    public StarSystem(String name, Point2D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        planets = new Planet[random.nextInt(6) + 4];
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet();
        }
    }

    public String getName() {
        return name;
    }

    public Planet[] getPlanets() {
        return planets;
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
        for(Planet planet : planets) {
            builder.append(planet).append("\n");
        }
        return builder.toString();
    }
}