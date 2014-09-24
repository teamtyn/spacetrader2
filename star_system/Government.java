package spacetrader.star_system;

import java.util.HashMap;
import java.util.Random;

/**
 * Government is defined by its type, leader, and level of anger
 * @author David Purcell
 */
public class Government {
    public enum GovernmentType {ANARCHY, ARISTOCRACY, CAPITALIST, COMMUNIST,
                                  CORPORATE, DEMOCRACY, FASCIST, MERITOCRACY,
                                  MONARCHY, OLIGARCHY, TECHNOCRACY, THEOCRACY};
    private HashMap<GovernmentType, String> leaders;
    private GovernmentType type;
    private String leader;
    private int anger;
    private Random random = new Random();

    public Government() {
        type = GovernmentType.values()[random.nextInt(GovernmentType.values().length)];
        //leader = leaders.get(type);
        leader = "wtf";
        anger = 0;
        //setUpLeaderMap();
    }

    public void setUpLeaderMap() {
        leaders.put(GovernmentType.ANARCHY, "No One");
        leaders.put(GovernmentType.ARISTOCRACY, "The Snobs");
        leaders.put(GovernmentType.CAPITALIST, "The Money");
        leaders.put(GovernmentType.COMMUNIST, "The State");
        leaders.put(GovernmentType.CORPORATE, "The Businesses");
        leaders.put(GovernmentType.DEMOCRACY, "The People");
        leaders.put(GovernmentType.FASCIST, "One Mean Guy");
        leaders.put(GovernmentType.MERITOCRACY, "The Qualified");
        leaders.put(GovernmentType.MONARCHY, "One Nice Guy");
        leaders.put(GovernmentType.OLIGARCHY, "The Few");
        leaders.put(GovernmentType.TECHNOCRACY, "The Experts");
        leaders.put(GovernmentType.THEOCRACY, "God");
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String newLeader) {
        leader = newLeader;
    }

    public GovernmentType getType() {
        return type;
    }

    public void setType(GovernmentType newType) {
        type = newType;
    }

    public int getAnger() {
        return anger;
    }

    public void setAnger(int newAnger) {
        anger = newAnger;
    }

    public void revolution() {
        type = GovernmentType.ANARCHY;
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
        type = GovernmentType.MONARCHY;
        leader = name;
        anger = 0;
    }
}