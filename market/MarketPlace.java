package spacetrader.market;

import java.util.ArrayList;
import spacetrader.star_system.Planet;

/**
 * MarketSetup has three lists, goods, buyable, and sellable
 *   -goods are all the goods in the game, prices based on the planet passed on
 *   -buyable are all the goods that are allowed to be bought here (via tech level)
 *   -sellable are all the goods that are allowed to be sold here (via tech level)
 * @author Ryan Burns
 */
public class MarketPlace {
    private final ArrayList<TradeGood> goods;
    private final ArrayList<TradeGood> buyable;
    private final ArrayList<TradeGood> sellable;

    /**
     * Initializes the three lists based on the correct planet
     * @param planet The planet that that market is on
     */
    public MarketPlace(Planet planet) {
        goods = new ArrayList<>();
        buyable = new ArrayList<>();
        sellable = new ArrayList<>();
        for (TradeGood.GoodType type: TradeGood.GoodType.values()) {
            TradeGood newGood = new TradeGood(type, planet);
            goods.add(newGood);
            if (type.mtlp <= planet.getTechLevelOrdinality()) {
                buyable.add(newGood);
            }
            if (type.mtlu <= planet.getTechLevelOrdinality()) {
                sellable.add(newGood);
            }
        }
    }

    public ArrayList<TradeGood> getGoods() {
        return goods;
    }

    public ArrayList<TradeGood> getBuyable() {
        return buyable;
    }

    public ArrayList<TradeGood> getSellable() {
        return sellable;
    }

    /**
     * Works as both the increment and decrement functions
     * @param good The good to be changed
     * @param change The change in amount of the good
     */
    public void changeQuantity(TradeGood good, int change) {
        for (TradeGood tg: goods) {
            if (tg.type == good.type) {
                tg.setQuantity(tg.getQuantity() + change);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (TradeGood good: goods) {
            str.append(good.type);
            str.append(": ");
            str.append(good.getPrice());
            str.append("\n");
        }
        return str.toString();
    }
}