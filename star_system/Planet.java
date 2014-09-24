package spacetrader.star_system;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 * Planet is defined by its government, resource level, circumstance, and tech level
 *   It has the physical characteristics of color, orbit distance, and size
 * @author David Purcell
 */
public class Planet {
    private final String name;
    private final int orbitDistance;
    private Government government;
    public enum TechLevel {PREAGRICULTURAL, AGRICULTURAL, 
                           MEDIVAL, RENAISSANCE, 
                           EARLYINDUSTRY, INDUSTRIAL, 
                           POSTINDUSTRIAL, HIGHTECH};
    public enum ResourceLevel {NOSPECIALRESOURCES, MINERALRICH, MINERALPOOR,
                          DESERT, LOTSOFWATER, RICHSOIL,
                          POORSOIL, RICHFAUNA, LIFELESS,
                          WEIRDMUSHROOMS, LOTSOFHERBS,
                          ARTISTIC, WARLIKE};
    public enum Circumstance {NONE, DROUGHT, COLDWAVE, HEATWAVE,
                                CROPFAIL, WAR, BOREDOM, PLAGUE,
                                LACKOFWORKERS, MORALCRISIS};
    private Circumstance circumstance;
    private ResourceLevel resourceLevel;
    private TechLevel techLevel;
    private Random random = new Random();
    private final Color color;
    private final int size;

    public Planet() {
        resourceLevel = ResourceLevel.values()[random.nextInt(ResourceLevel.values().length)];
        techLevel = TechLevel.values()[random.nextInt(TechLevel.values().length)];
        circumstance = Circumstance.values()[random.nextInt(Circumstance.values().length)];
        size = random.nextInt(5) + 1;
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // TODO: Josh make fancier
        orbitDistance = random.nextInt(30) + 20; // Distance between planet and star, TODO: need to ensure orbits are unique
        government = new Government();
        name = PlanetNames.getName(government);
    }

    public Color getColor() {
        return color;
    }

    public int getOrbitDistance(){
        return orbitDistance;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Government getGovernment() {
        return government;
    }

    public ResourceLevel getResourceLevel() {
        return resourceLevel;
    }

    public TechLevel getTechLevel() {
        return techLevel;
    }

    public Circumstance getCircumstance() {
        return circumstance;
    }

    public void setResourceLevel(ResourceLevel newRes) {
        resourceLevel = newRes;
    }

    public void setTechLevel(TechLevel newTech) {
        techLevel = newTech;
    }

    public void setCircumstance(Circumstance circumstance) {
        this.circumstance = circumstance;
    }

    public void revolt() {
        government.revolution();
    }

    public void becomeMonarchy(String name){
        government.toMonarchy(name);
    }

    @Override
    public String toString() {
        String str = "Planet: " + name + "\nResource Level: " + resourceLevel + "\nTech Level: " + techLevel
                        + "\nUnder rule of " + government;
        return str;
    }
}