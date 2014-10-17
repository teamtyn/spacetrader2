package spacetrader.star_system;

import java.util.HashMap;
import spacetrader.GameModel;

/**
 * Government is defined by its type, leader, and level of anger
 * @author David Purcell
 */
public class Government {
    public enum Type {ANARCHY, ARISTOCRACY, CAPITALIST, COMMUNIST,
                          CORPORATE, DEMOCRACY, FASCIST, MERITOCRACY,
                          MONARCHY, OLIGARCHY, TECHNOCRACY, THEOCRACY};
    private HashMap<Type, String> leaders;
    private Type type;
    private String leader;
    private int anger;

    public Government() {
        type = Type.values()[GameModel.getRandom().nextInt(Type.values().length)];
        leaders = new HashMap<>();
        //setUpLeaderMap();
        //leader = leaders.get(type);
        leader = "THE PEOPLE";
        anger = 0;
    }

    public void setUpLeaderMap() {
        leaders.put(Type.ANARCHY, "No One");
        leaders.put(Type.ARISTOCRACY, "The Snobs");
        leaders.put(Type.CAPITALIST, "The Money");
        leaders.put(Type.COMMUNIST, "The State");
        leaders.put(Type.CORPORATE, "The Businesses");
        leaders.put(Type.DEMOCRACY, "The People");
        leaders.put(Type.FASCIST, "One Mean Guy");
        leaders.put(Type.MERITOCRACY, "The Qualified");
        leaders.put(Type.MONARCHY, "One Nice Guy");
        leaders.put(Type.OLIGARCHY, "The Few");
        leaders.put(Type.TECHNOCRACY, "The Experts");
        leaders.put(Type.THEOCRACY, "God");
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String newLeader) {
        leader = newLeader;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type newType) {
        type = newType;
    }

    public int getAnger() {
        return anger;
    }

    public void setAnger(int newAnger) {
        anger = newAnger;
    }

    public void revolution() {
        type = Type.ANARCHY;
        leader = null;
        //leader = leaders.get(type);
        anger = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        String str = "Government Type: " + type + "\nLeader: " + leader
                        + "\nAnger Level: " + anger;
        return str;
    }

    public void toMonarchy(String name) {
        type = Type.MONARCHY;
        leader = name;
        anger = 0;
    }
}
