package spacetrader.items;

import java.util.HashMap;
import spacetrader.market.TradeGood;

/**
 * A CargoBay is defined by its capacity, current size, and contents (which are TradeGoods)
 * @author David Purcell
 */
public class CargoBay {
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
     * If room to add that quantity, increment the size and the quantity of the specified good
     * @param goodName The good to be stored in the cargo bay
     * @param quantity The quantity of the good to be stored in the cargo bay
     * @return The number of goods successfully added
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
     * Returns whether the cargo bay can remove a given quantity
     * @param quantity The quantity to be potentially removed
     * @return Whether or not the cargo bay can afford to lose this new quantity
     */
    private boolean canRemoveQuantity(int quantity) {
        return (currentSize - quantity) >= 0;
    }

    /**
     * Remove X of them, where X is as many goods as possible up to specified quantity
     * @param goodName The good to be removed from the cargo bay
     * @param quantity The quantity to be removed from the cargo bay
     * @return Whether or not it was able to removed that quantity of the good
     */
    public boolean removeTradeGood(String goodName, int quantity) {
        boolean removed = false;
        if (canRemoveQuantity(quantity)) {
            removed = true;
            currentSize -= quantity;
            int oldNum = goods.get(goodName);
            goods.replace(goodName, oldNum - quantity);
        }
        return removed;
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