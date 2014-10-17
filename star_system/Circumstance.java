package spacetrader.star_system;

import java.util.Random;

/**
 * Circumstance has one of a couple predefined types
 *   It also has a current level or severity as well as a maximum level
 * @author Ryan Burns
 */
public class Circumstance {
    private final Random random = new Random();
    private final Type type;
    private int curLevel;
    private final int maxLevel;
    private boolean ascending;
    public enum Type {NONE, DROUGHT, COLD, CROPFAIL, WAR, BOREDOM, PLAGUE, LACKOFWORKERS};

    public Circumstance() {
        type = Type.values()[random.nextInt(Type.values().length)];
        curLevel = 0;
        maxLevel = random.nextInt(20) + 10;
        ascending = true;
    }

    public Type getType() {
        return type;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getOrdinality() {
        return type.ordinal();
    }

    public void tickCurLevel() {
        if (ascending) {
            curLevel++;
        } else if (!ascending && curLevel != 0) {
            curLevel--;
        }
        if (curLevel == maxLevel) {
            ascending = false;
        }
    }
}