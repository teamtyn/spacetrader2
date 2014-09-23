package spacetrader.star_system;

import java.util.Random;

/**
 * Government is defined by its type, leader, and level of anger
 * @author David Purcell
 */
public class Government {
    public enum GovernmentType {ANARCHY, CAPITALIST, COMMUNIST,
                        CONFEDERACY, CORPORATE, CYBERNETIC,
                        DEMOCRACY, DICTATORSHIP, FASCIST,
                        FEUDAL, MILITARY, MONARCHY,
                        PACIFIST, SOCIALIST, STATEOFSATORI,
                        TECHNOCRACY, THEOCRACY};
    private GovernmentType type;
    private String leader;
    private int anger;
    private Random random = new Random();

    public Government() {
        type = GovernmentType.values()[random.nextInt(GovernmentType.values().length)];
        leader = "THE PEOPLE";
        anger = 0;
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
        leader = null;
        anger = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        String str = "Government Type: " + type + "\nLeader: " + leader
                        + "\nAnger Level: " + anger;
        return str;
    }

    public void toMonarchy(String name){
        type = GovernmentType.MONARCHY;
        leader = name;
        anger = 0;
    }
}
