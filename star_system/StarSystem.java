package spacetrader.star_system;

import java.util.Random;
import javafx.geometry.Point2D;

/**
 * StarSystem contains an array of planets, and resides at some coordinates
 * @author David Purcell
 */
public class StarSystem {
    private final String name;
    private Point2D coordinates;
    private Planet[] planets;
    private Random random = new Random();
    private String[] names = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    public StarSystem(String name) {
        this.name = name;
        // Ryan - wut
        coordinates = new Point2D(random.nextDouble() * 1300 + 50, random.nextDouble() * 600 + 50); //Range <-50.0, -50.0> - <50.0,50.0>
        planets = new Planet[random.nextInt(6) + 4];
        for (int i = 0; i < planets.length - 1; i++) {
            planets[i] = new Planet(names[i % 10]);
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
        String str = "System: " + name + "\nStar System Coordinates:\n"
                        + coordinates + "\nPlanets: \n";
        for(Planet planet : planets) {
            str = str + planet + "\n";
        }
        return str;
    }
}