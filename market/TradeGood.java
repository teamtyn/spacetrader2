package spacetrader.market;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import spacetrader.star_system.Government;
import spacetrader.star_system.Planet;

/**
 * MTLP = Minimum Tech Level to Produce this resource (You can't buy on planets below this level)
 * MTLU = Minimum Tech Level to Use this resource (You can't sell on planets below this level)
 * TTP = Tech Level which produces the most of this item
 * IPL = Price increase per tech level
 * VAR = variance is the maximum percentage that the price can vary above or below the base
 * IE = Radical price increase event, when this even happens on a planet, the price may increase astronomically
 * CR = When this condition is present, the price of this resource is unusually low
 * ER = When this condition is present, the resource is expensive
 * MTL = Min price offered in space trade with random trader (not on a planet)
 * MTH = Max price offered in space trade with random trader (not on a planet)
 * 
 * TechLevel {PREAGRICULTURAL [0], AGRICULTURAL [1], MEDIEVAL [2], RENAISSANCE [3], 
 *            EARLYINDUSTRIAL [4], INDUSTRIAL [5], POSTINDUSTRIAL [6], HIGHTECH [7]};
 * 
 * ResourceLevel {NOSPECIALRESOURCES [0], MINERALRICH [1], MINERALPOOR [2],
 *                DESERT [3], LOTSOFWATER [4], RICHSOIL [5], POORSOIL [6],
 *                RICHFAUNA [7], LIFELESS [8], WEIRDMUSHROOMS [9],
 *                LOTSOFHERBS [10], ARTISTIC [11], WARLIKE [12]};
 * 
 * Circumstance {NONE [0], DROUGHT [1], COLD [2], CROPFAIL [3], WAR [4],
                 BOREDOM [5], PLAGUE [6], LACKOFWORKERS [7]};
 * 
 * If ie, cr, or er == -1, means never
 * @author Ryan Burns
 */
public class TradeGood {
    public GoodType type;
    private final Planet planet;
    private int price;
    private  int quantity;
    // Price multipliers from government type of the planet
    private final static Map<Government.Type, Double> govPrice;
    static {
        Map<Government.Type, Double> govPrice2 = new HashMap<>();
        govPrice2.put(Government.Type.ANARCHY, 1.5);
        govPrice2.put(Government.Type.ARISTOCRACY, 1.0);
        govPrice2.put(Government.Type.CAPITALIST, 1.0);
        govPrice2.put(Government.Type.COMMUNIST, 1.25);
        govPrice2.put(Government.Type.CORPORATE, 1.0);
        govPrice2.put(Government.Type.DEMOCRACY, 1.0);
        govPrice2.put(Government.Type.FASCIST, 1.0);
        govPrice2.put(Government.Type.MERITOCRACY, .75);
        govPrice2.put(Government.Type.MONARCHY, 1.0);
        govPrice2.put(Government.Type.OLIGARCHY, 1.0);
        govPrice2.put(Government.Type.TECHNOCRACY, .5);
        govPrice2.put(Government.Type.THEOCRACY, 1.0);
        govPrice = Collections.unmodifiableMap(govPrice2);
    }
    // Quantity multipliers from government type of the planet
    private final static Map<Government.Type, Integer> govQuantity;
    static {
        Map<Government.Type, Integer> govQuantity2 = new HashMap<>();
        govQuantity2.put(Government.Type.ANARCHY, 1);
        govQuantity2.put(Government.Type.ARISTOCRACY, 1);
        govQuantity2.put(Government.Type.CAPITALIST, 1);
        govQuantity2.put(Government.Type.COMMUNIST, 1);
        govQuantity2.put(Government.Type.CORPORATE, 1);
        govQuantity2.put(Government.Type.DEMOCRACY, 1);
        govQuantity2.put(Government.Type.FASCIST, 1);
        govQuantity2.put(Government.Type.MERITOCRACY, 1);
        govQuantity2.put(Government.Type.MONARCHY, 1);
        govQuantity2.put(Government.Type.OLIGARCHY, 1);
        govQuantity2.put(Government.Type.TECHNOCRACY, 1);
        govQuantity2.put(Government.Type.THEOCRACY, 1);
        govQuantity = Collections.unmodifiableMap(govQuantity2);
    }

    public enum GoodType {
        Water(0, 0, 2, 30, 3, 4, 1, 4, 3, 30, 500, "Water"),
        Furs(0, 0, 0, 250, 10, 10, 2, 7, 8, 230, 280, "Furs"),
        Food(1, 0, 1, 100, 5, 5, 3, 5, 6, 90, 160, "Food"),
        Ore(2, 2, 3, 350, 20, 10, 4, 1, 2, 350, 420, "Ore"),
        Games(3, 1, 6, 250, -10, 5, 5, 11, -1, 160, 270, "Games"),
        Firearms(3, 1, 5, 1250, -75, 100, 4, 12, -1, 600, 1100, "Firearms"),
        Medicine(4, 1, 6, 650, -20, 10, 6, 10, -1, 400, 700, "Medicine"),
        Machines(4, 3, 5, 900, -30, 5, 7, -1, -1, 600, 800, "Machines"),
        Narcotics(5, 0, 5, 3500, -125, 150, 5, 9, -1, 2000, 3000, "Narcotics"),
        Robots(6, 4, 7, 5000, -150, 100, 7, -1, -1, 3500, 5000, "Robots");

        public int mtlp;
        public int mtlu;
        public int ttp;
        public int basePrice;
        public int ipl;
        public int var;
        public int ie;
        public int cr;
        public int er;
        public int mtl;
        public int mhl;
        public String name;
        private final Random rando = new Random();

        GoodType(int mtlp, int mtlu, int ttp, int basePrice, int ipl, int var, int ie, int cr, int er, int mtl, int mhl, String name) {
            this.mtlp = mtlp;
            this.mtlu = mtlu;
            this.ttp = ttp;
            this.basePrice = basePrice;
            this.ipl = ipl;
            this.var = 1 + (((rando.nextInt((2 * var) + 1)) - var) / 100);
            this.ie = ie;
            this.cr = cr;
            this.er = er;
            this.mtl = mtl;
            this.mhl = mhl;
            this.name = name;
        }
    };

    public TradeGood(GoodType type, Planet planet) {
        this.type = type;
        this.planet = planet;
        this.price = calcPrice();
        this.quantity = calcQuantity();
    }
    
    public TradeGood(GoodType type, int quantity) {
        this.type = type;
        this.planet = null;
        this.price = 0;
        this.quantity = quantity;
    }

    public final int calcPrice() {
        // Price calculation from the wiki
        int thisPrice = (type.basePrice + (type.ipl * (planet.getTechLevelOrdinality() - type.mtlp))) * type.var;
        if (type.cr == planet.getResourceLevelOrdinality()) {
            thisPrice *= .75;
        }
        if (type.er == planet.getResourceLevelOrdinality()) {
            thisPrice *= 1.25;
        }
        if (type.ie == planet.getCircumstanceOrdinality()) {
            thisPrice *= 1.5;
        }
        thisPrice *= govPrice.get(planet.getGovernment().getType());
        return thisPrice;
    }

    public final int calcQuantity() {
        return 10;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
<<<<<<< HEAD
    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
=======
    
    public String toString(){
        return this.type.name + " " + quantity;
>>>>>>> origin/master
    }
}