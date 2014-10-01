package spacetrader.items;

import java.util.ArrayList;
import java.util.List;
import spacetrader.market.TradeGood;

/**
 *
 * @author David Purcell
 */
class CargoBay {
    //How many things we can put in there
    private int capacity;
    //How many are in there
    private int currentSize;
    //ArrayList storing instances of all possible trade goods to be stored in this cargo hold.
    private ArrayList<TradeGood> contents;
    
    public CargoBay(int capacity){
        this.capacity = capacity;
        this.currentSize = 0;
    }
    
    //Is the cargo hold big enough to accept new stuffs?
    public boolean canAddQuantity(int quantity){
        return (currentSize + quantity) <= capacity;
    }
    
    //If can add then if there is no copy of this type of good, add new good, else add onto quantity of other good.
    public void addTradeGood(TradeGood good){
        boolean added = false;
        if(canAddQuantity(good.getQuantity())){
            for(TradeGood content : contents){
                if(content.type == good.type){
                    content = new TradeGood(content.type, content.getQuantity() + good.getQuantity());
                    currentSize += good.getQuantity();
                    added = true;
                }
            }
            if(!added){
                contents.add(good);
            }
        }
    }
    
    //If that kind of good exists in the hold, remove X of them, where X is as many goods as possible up to specified quantity.
    public TradeGood removeTradeGood(TradeGood good, int quantity){
        TradeGood removedGood = null;
        for(TradeGood content : contents){
            if(content.type == good.type){
                if(content.getQuantity() > quantity){
                    content = new TradeGood(content.type, content.getQuantity() - quantity);   
                } else if(content.getQuantity() == quantity){
                    removedGood = content;
                    content = null;
                } else {
                    System.out.println("Not enough, but here is what you have");
                    removedGood = content;
                    content = null;
                }
            }
        }
        if(removedGood != null){
            currentSize -= removedGood.getQuantity();
        }
        return removedGood;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("Conents: \n");
        for(TradeGood content : contents){
            str.append(content.toString()).append("\n");
        }
        return str.toString();
    }
}
