package spacetrader.items;

/**
 * Ship base class, currently just some potential instance variables
 * @author Ryan Burns
 */
public class Ship {
    private int cargoBaySize;
    private int hullStrength;
    private ShipType type;
    private String gadget;
    private int shieldStrength;
    private boolean escapePod;
    private boolean insurance;
    private int fuelCapacity;
    // Standard ship names from original game, can be added to later
    public enum ShipType {Flea, Gnat, Firefly, Mosquito, Bumblebee,
                            Beetle, Hornet, Grasshopper, Termite, Wasp};


}