package spacetrader.items;

import java.io.Serializable;
import java.util.HashMap;
import spacetrader.market.TradeGood;

/**
 * A CargoBay is defined by its capacity, current size, and contents (which are TradeGoods)
 * @author David Purcell
 */
public class CargoBay implements Serializable {
    private int capacity;
    private int currentSize;
    private final HashMap<String, Integer> goods;

    public CargoBay(int capacity) {
        this.capacity = capacity;
        goods = new HashMap<>(TradeGood.GoodType.values().length);
        setUpMap();
    }

    public final void setUpMap() {
        goods.put("Water", 0);
        goods.put("Furs", 0);
        goods.put("Food", 0);
        goods.put("Ore", 0);
        goods.put("Games", 0);
        goods.put("Firearms", 0);
        goods.put("Medicine", 0);
        goods.put("Machines", 0);
        goods.put("Narcotics", 0);
        goods.put("Robots", 0);
    }

    /**
     * Add as many goods as possible up to specified quantity
     * @param goodName The good to be stored in the cargo bay
     * @param quantity The quantity to be ideally stored in the cargo bay
     * @return The number of goods added
     */
    public int addTradeGood(String goodName, int quantity) {
        if ((quantity + currentSize) > capacity) {
            quantity = capacity - currentSize;
        }
        if (quantity > 0) {
            currentSize += quantity;
            int oldNum = goods.get(goodName);
            goods.replace(goodName, oldNum + quantity);
        }
        return quantity;
    }

    /**
     * Remove as many goods as possible up to specified quantity
     * @param goodName The good to be removed from the cargo bay
     * @param quantity The quantity to be ideally removed from the cargo bay
     * @return The number of goods removed
     */
    public int removeTradeGood(String goodName, int quantity) {
        if (quantity > goods.get(goodName)) {
            quantity = goods.get(goodName);
        }
        currentSize -= quantity;
        goods.replace(goodName, goods.get(goodName) - quantity);
        return quantity;
    }

    public HashMap<String, Integer> getGoods() {
        return goods;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    /**
     * Only to be used as a convenience when player upgrades their cargo bay
     * @param capacity The new capacity of the cargo bay
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Cargo bay contents: \n");
        for (String goodName: goods.keySet()) {
            str.append(goodName);
            str.append(": ");
            str.append(goods.get(goodName));
            str.append("\n");
        }
        return str.toString();
    }
}