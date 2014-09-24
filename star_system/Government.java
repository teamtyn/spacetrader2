package spacetrader.star_system;

import java.util.Random;

/**
 * Government is defined by its type, leader, and level of anger
 * @author David Purcell
 */
public class Government {
    public enum Type {ANARCHY, CAPITALIST, COMMUNIST,
                        CONFEDERACY, CORPORATE, CYBERNETIC,
                        DEMOCRACY, DICTATORSHIP, FASCIST,
                        FEUDAL, MILITARY, MONARCHY,
                        PACIFIST, SOCIALIST, STATEOFSATORI,
                        TECHNOCRACY, THEOCRACY};
    private Type type;
    private String leader;
    private int anger;
    private Random random = new Random();

    public Government() {
        type = Type.values()[random.nextInt(Type.values().length)];
        leader = "THE PEOPLE";
        anger = 0;
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
        anger = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        String str = "Government Type: " + type + "\nLeader: " + leader
                        + "\nAnger Level: " + anger;
        return str;
    }

    public void toMonarchy(String name){
        type = Type.MONARCHY;
        leader = name;
        anger = 0;
    }
}
