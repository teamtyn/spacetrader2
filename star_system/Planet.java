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
    private final double orbitDistance;
    private final double orbitSpeed;
    private final double axialTilt;
    private Government government;
    public enum TechLevel {PREAGRICULTURAL, AGRICULTURAL, 
                           MEDIEVAL, RENAISSANCE, 
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
    private final double size;
    public boolean hasPlayer;

    public Planet(double orbitDistance) {
        resourceLevel = ResourceLevel.values()[random.nextInt(ResourceLevel.values().length)];
        techLevel = TechLevel.values()[random.nextInt(TechLevel.values().length)];
        circumstance = Circumstance.values()[random.nextInt(Circumstance.values().length)];
        
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // TODO: Josh make fancier
        this.orbitDistance = orbitDistance;
        size = 2 * random.nextDouble() + 1;
        orbitSpeed = Math.sqrt(1/(20 * orbitDistance));
        axialTilt = 45 * random.nextDouble();
        
        
        government = new Government();
        name = PlanetNames.getName(government);
        hasPlayer = false;
    }

    public Color getColor() {
        return color;
    }

    public double getOrbitSpeed() {
        return  orbitSpeed;
    }
    
    public double getOrbitDistance(){
        return orbitDistance;
    }
    
    public double getAxialTilt() {
        return axialTilt;
    }

    public double getSize() {
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