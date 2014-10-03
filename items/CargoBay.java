package spacetrader.items;

import java.util.ArrayList;
import spacetrader.market.TradeGood;

/**
 *
 * @author David Purcell
 */
public class CargoBay {
    private final int capacity;
    // How many are in there
    private int currentSize;
    // ArrayList storing instances of all possible trade goods to be stored in this cargo hold
    private ArrayList<TradeGood> contents;

    public CargoBay(int capacity) {
        this.capacity = capacity;
        this.currentSize = 0;
        this.contents = new ArrayList<>();
    }

    // Is the cargo hold big enough to accept new stuffs?
    /**
     * 
     * @param quantity
     * @return 
     */
    public boolean canAddQuantity(int quantity) {
        System.out.println("Can Add quantity: " + quantity + " " + ((currentSize + quantity) <= capacity));
        return (currentSize + quantity) <= capacity;
    }

    // If can add then if there is no copy of this type of good, add new good, else add on to quantity of other good
    /**
     * 
     * @param good 
     */
    public void addTradeGood(TradeGood good) {
        System.out.println("LETS ADD THINGS");
        boolean added = false;
        if (canAddQuantity(good.getQuantity())) {
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).type == good.type) {
                    contents.set(i, new TradeGood(contents.get(i).type, contents.get(i).getQuantity() + good.getQuantity()));
                    currentSize += good.getQuantity();
                    System.out.println(currentSize);
                    added = true;
                }
            }
            if (!added) {
                contents.add(good);
                currentSize += good.getQuantity();
                System.out.println(currentSize);
            }
        }
    }

    // If that kind of good exists in the hold, remove X of them, where X is as many goods as possible up to specified quantity
    /**
     * 
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Contents: \n");
        for (TradeGood content: contents) {
            str.append(content.toString()).append("\n");
        }
        return str.toString();
    }
}