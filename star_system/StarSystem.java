package spacetrader2.star_system;

import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * StarSystem contains an array of planets, and resides at some coordinates
 * @author David Purcell
 */
public class StarSystem {
    private final String name;
    private final Point2D coordinates;
    private Planet[] planets;
    private final double size;
    private final Color color;
    public boolean hasPlayer;
    private Random random = new Random();

    public StarSystem(String name, Point2D coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        hasPlayer = false;
        planets = new Planet[random.nextInt(6) + 4];
        setOrbits();
        size = random.nextInt(10) + 5;
        color = Color.rgb(random.nextInt(5) + 250, random.nextInt(55) + 200, random.nextInt(100));
    }

    public String getName() {
        return name;
    }

    public Planet[] getPlanets() {
        return planets;
    }
    
    public double getSize() {
        return size;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setOrbits() {
        double minDist = 20;
        double remDist = 80;
        for(int i = 0; i < planets.length; i++) {
            double size = 2 * random.nextDouble() + 1;
            double offset = (planets.length - i != 0) ? remDist / (2 * (planets.length - i)) : remDist / 2;
            double distance = minDist + size + offset + (offset / 3) * random.nextGaussian();
            minDist += 2 * offset + size;
            remDist -= 2 * offset + size;
            planets[i] = new Planet(distance);
        }
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
    
    public double getSystemDistance(StarSystem other) {
        double dx = other.getCoordinateX() - coordinates.getX();
        double dy = other.getCoordinateY() - coordinates.getY();
        return Math.sqrt(dx * dx + dy * dy);
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