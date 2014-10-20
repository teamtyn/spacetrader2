package spacetrader.star_system;

import java.io.Serializable;
import javafx.scene.paint.Color;
import spacetrader.GameModel;
import spacetrader.market.MarketPlace;
import spacetrader.ui.SerializableColor;

/**
 * Planet is defined by its government, resource level, circumstance, and tech level
 *   It has the physical characteristics of color, orbit distance, and size
 *   It also knows whether or not the player is currently there
 * @author David Purcell
 */
public class Planet implements Serializable {
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
    private final MarketPlace market;
    private final SerializableColor color;
    private final int size;
    public boolean hasPlayer;

    public Planet() {
        resourceLevel = ResourceLevel.values()[GameModel.getRandom().nextInt(ResourceLevel.values().length)];
        techLevel = TechLevel.values()[GameModel.getRandom().nextInt(TechLevel.values().length)];
        circumstance = new Circumstance();
        size = GameModel.getRandom().nextInt(5) + 1;
        color = new SerializableColor(Color.rgb(GameModel.getRandom().nextInt(256),
                GameModel.getRandom().nextInt(256),
                GameModel.getRandom().nextInt(256))); // TODO: Josh make fancier
        orbitDistance = GameModel.getRandom().nextInt(30) + 20; // Distance between planet and star, TODO: need to ensure orbits are unique
        government = new Government();
        name = PlanetNames.getName(government);
        hasPlayer = false;
        market = new MarketPlace(this);
    }

    public MarketPlace getMarket() {
        return market;
    }
    
    public Color getColor() {
        return color.getColor();
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