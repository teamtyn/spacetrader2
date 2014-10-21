package spacetrader2.market;

import spacetrader2.star_system.Government;
import spacetrader2.star_system.Planet;

/**
 * 
 * @author Ryan Burns
 */
public class MarketSetup {
    // Standard goods list from original game, can be added to later
    private String[] goods = {"Water", "Furs", "Food", "Ore", "Games",
                                "Firearms", "Medicine", "Machines",
                                "Narcotics", "Robots"};
    private Integer[] quantities = new Integer[goods.length];
    private Integer[] prices = new Integer[goods.length];
    private Government gov;
    private Planet.Circumstance circ;
    private Planet.TechLevel tech;
    private Planet.ResourceLevel resource;

    public MarketSetup(Planet planet) {
        gov = planet.getGovernment();
        circ = planet.getCircumstance();
        tech = planet.getTechLevel();
        resource = planet.getResourceLevel();
    }

    public String[] getGoods() {
        return goods;
    }
    
    public Integer[] getQuantities()  {
        return quantities;
    }

    public Integer[] getPrices()  {
        return prices;
    }

}