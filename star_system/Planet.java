package spacetrader.star_system;

import java.util.Random;
import javafx.scene.paint.Color;
import spacetrader.market.MarketSetup;

/**
 * Planet is defined by its government, resource level, circumstance, and tech level
 *   It has the physical characteristics of color, orbit distance, and size
 *   It also knows whether or not the player is currently there
 * @author David Purcell
 */
public class Planet {
    private final String name;
    private final int orbitDistance;
    private final Government government;

    public enum TechLevel {PREAGRICULTURAL, AGRICULTURAL, 
                           MEDIEVAL, RENAISSANCE, 
                           EARLYINDUSTRIAL, INDUSTRIAL, 
                           POSTINDUSTRIAL, HIGHTECH};
    public enum ResourceLevel {NOSPECIALRESOURCES, MINERALRICH, MINERALPOOR,
                               DESERT, LOTSOFWATER, RICHSOIL,
                               POORSOIL, RICHFAUNA, LIFELESS,
                               WEIRDMUSHROOMS, LOTSOFHERBS,
                               ARTISTIC, WARLIKE};
    private final Circumstance circumstance;
    private ResourceLevel resourceLevel;
    private TechLevel techLevel;
    private final Random random = new Random();
    private final MarketSetup market;
    private final Color color;
    private final int size;
    public boolean hasPlayer;

    public Planet() {
        resourceLevel = ResourceLevel.values()[random.nextInt(ResourceLevel.values().length)];
        techLevel = TechLevel.values()[random.nextInt(TechLevel.values().length)];
        circumstance = new Circumstance();
        size = random.nextInt(5) + 1;
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // TODO: Josh make fancier
        orbitDistance = random.nextInt(30) + 20; // Distance between planet and star, TODO: need to ensure orbits are unique
        government = new Government();
        name = PlanetNames.getName(government);
        hasPlayer = false;
        market = new MarketSetup(this);
    }

    public MarketSetup getMarket() {
        return market;
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

    public int getResourceLevelOrdinality() {
        return resourceLevel.ordinal();
    }

    public TechLevel getTechLevel() {
        return techLevel;
    }

    public int getTechLevelOrdinality() {
        return techLevel.ordinal();
    }

    public Circumstance getCircumstance() {
        return circumstance;
    }

    public void setResourceLevel(ResourceLevel resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public void setTechLevel(TechLevel techLevel) {
        this.techLevel = techLevel;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Planet: ").append(name).append("\nResource Level: ")
                .append(resourceLevel).append("\nTech Level: ").append(techLevel)
                        .append("\nUnder rule of ").append(government);
        return str.toString();
    }
}