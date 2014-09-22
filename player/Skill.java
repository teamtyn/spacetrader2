package spacetrader.player;

public class Skill {
    private String type;
    private int value;

    // No value constructor
    public Skill(String type) {
        this(type, 0);
    }

    // Full constructor
    public Skill(String type, int value) {
        this.type = type;
        this.value = value;
    }

    // Getter for value
    public int getValue() {
        return this.value;
    }

    // Getter for type
    public String getType() {
        return this.type;
    }

    // Setter for value
    public void setValue(int newValue) {
        this.value = newValue;
    }

    // Increase the value of skill by specified amount
    public void increaseValue(int value) {
        this.value += value;
    }
}