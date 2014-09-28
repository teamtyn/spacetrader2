package spacetrader.market;

import java.util.HashMap;
import spacetrader.star_system.Government;

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
    private GoodType type;
    private Government.Type gov;
    // Price multipliers from government type of the planet
    private static HashMap<Government.Type, Integer> govPrice;
    static {
        govPrice.put(Government.Type.ANARCHY, 0);
        govPrice.put(Government.Type.ARISTOCRACY, 0);
        govPrice.put(Government.Type.CAPITALIST, 0);
        govPrice.put(Government.Type.COMMUNIST, 0);
        govPrice.put(Government.Type.CORPORATE, 0);
        govPrice.put(Government.Type.DEMOCRACY, 0);
        govPrice.put(Government.Type.FASCIST, 0);
        govPrice.put(Government.Type.MERITOCRACY, 0);
        govPrice.put(Government.Type.MONARCHY, 0);
        govPrice.put(Government.Type.OLIGARCHY, 0);
        govPrice.put(Government.Type.TECHNOCRACY, 0);
        govPrice.put(Government.Type.THEOCRACY, 0);
    }
    // Quantity multipliers from government type of the planet
    private static HashMap<Government.Type, Integer> govQuantity;
    static {
        govQuantity.put(Government.Type.ANARCHY, 0);
        govQuantity.put(Government.Type.ARISTOCRACY, 0);
        govQuantity.put(Government.Type.CAPITALIST, 0);
        govQuantity.put(Government.Type.COMMUNIST, 0);
        govQuantity.put(Government.Type.CORPORATE, 0);
        govQuantity.put(Government.Type.DEMOCRACY, 0);
        govQuantity.put(Government.Type.FASCIST, 0);
        govQuantity.put(Government.Type.MERITOCRACY, 0);
        govQuantity.put(Government.Type.MONARCHY, 0);
        govQuantity.put(Government.Type.OLIGARCHY, 0);
        govQuantity.put(Government.Type.TECHNOCRACY, 0);
        govQuantity.put(Government.Type.THEOCRACY, 0);
    }

    public enum GoodType {
        Water(0, 0, 2, 30, 3, 4, 1, 4, 3, 30, 500),
        Furs(0, 0, 0, 250, 10, 10, 2, 7, 8, 230, 280),
        Food(1, 0, 1, 100, 5, 5, 3, 5, 6, 90, 160),
        Ore(2, 2, 3, 350, 20, 10, 4, 1, 2, 350, 420),
        Games(3, 1, 6, 250, -10, 5, 5, 11, -1, 160, 270),
        Firearms(3, 1, 5, 1250, -75, 100, 4, 12, -1, 600, 1100),
        Medicine(4, 1, 6, 650, -20, 10, 6, 10, -1, 400, 700),
        Machines(4, 3, 5, 900, -30, 5, 7, -1, -1, 600, 800),
        Narcotics(5, 0, 5, 3500, -125, 150, 5, 9, -1, 2000, 3000),
        Robots(6, 4, 7, 5000, -150, 100, 7, -1, -1, 3500, 5000);

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

        GoodType(int mtlp, int mtlu, int ttp, int basePrice, int ipl, int var, int ie, int cr, int er, int mtl, int mhl) {
            this.mtlp = mtlp;
            this.mtlu = mtlu;
            this.ttp = ttp;
            this.basePrice = basePrice;
            this.ipl = ipl;
            this.var = var;
            this.ie = ie;
            this.cr = cr;
            this.er = er;
            this.mtl = mtl;
            this.mhl = mhl;
        }
    };
    
    public TradeGood(GoodType type, Government.Type gov) {
        this.type = type;
        this.gov = gov;
    }
    
}