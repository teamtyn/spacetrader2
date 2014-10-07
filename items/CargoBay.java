package spacetrader.items;

import java.util.ArrayList;
import java.util.HashMap;
import spacetrader.market.TradeGood;

/**
 * A CargoBay is defined by its capacity, current size, and contents (which are TradeGoods)
 * @author David Purcell
 */
public class CargoBay {
    private int capacity;
    private int currentSize;
    private HashMap<String, Integer> goods;
    private ArrayList<TradeGood> contents;

    public CargoBay(int capacity) {
        this.capacity = capacity;
        contents = new ArrayList<>();
        goods = new HashMap<>();
    }

    /**
     * Returns whether the cargo bay can fit a given quantity
     * @param quantity The quantity to be potentially added
     * @return Whether or not the cargo bay can handle this new quantity
     */
    public boolean canAddQuantity(int quantity) {
        return (currentSize + quantity) <= capacity;
    }

    /**
     * If can add then if there is no copy of this type of good, add new good, else add on to quantity of other good
     * @param good The good to be stored in the cargo bay
     * @return Returns a message explaining failure if it does fail, can be printed to player
     */
    public String addTradeGood(TradeGood good) {
        boolean added = false;
        String returnMsg = null;
        if (canAddQuantity(1)) {
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).type == good.type) {
                    contents.set(i, new TradeGood(contents.get(i).type, contents.get(i).getQuantity() + good.getQuantity()));
                    currentSize += good.getQuantity();
                    added = true;
                }
            }
            if (!added) {
                contents.add(good);
                currentSize += good.getQuantity();
            }
        } else {
            returnMsg = "Not enough room for that";
        }
        return returnMsg;
    }

    /**
     * If that kind of good exists in the hold, remove X of them, where X is as many goods as possible up to specified quantity
     * @param good
     * @return 
     */
    public TradeGood removeTradeGood(TradeGood good) {
        TradeGood removedGood = null;
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).type == good.type) {
                if (contents.get(i).getQuantity() > good.getQuantity()) {
                    removedGood = good;
                    contents.set(i, new TradeGood(contents.get(i).type, contents.get(i).getQuantity() - good.getQuantity()));   
                } else if (contents.get(i).getQuantity() == good.getQuantity()) {
                    removedGood = contents.get(i);
                    contents.remove(i);
                    i--;
                } else {
                    System.out.println("Not enough, but here is what you have");
                    removedGood = contents.get(i);
                    contents.remove(i);
                    i--;
                }
            }
        }
        if (removedGood != null) {
            currentSize -= removedGood.getQuantity();
            System.out.println(currentSize);
        }
        return removedGood;
    }

    public ArrayList<TradeGood> getContents() {
        return contents;
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
        StringBuilder str = new StringBuilder("Contents: \n");
        for (TradeGood content: contents) {
            str.append(content.toString()).append("\n");
        }
        return str.toString();
    }
}