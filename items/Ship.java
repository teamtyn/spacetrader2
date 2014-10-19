package spacetrader.items;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 * Ship class, what the player travels around and transports cargo in
 * @author David Purcell
 */
public class Ship implements Serializable {
    public ShipType type;
    private final Gadget[] gadgets;
    private final Shield[] shields;
    private final Weapon[] weapons;
    private final CargoBay cargoBay;
    private EscapePod escapePod;
    private Insurance insurance;
    private int hull;
    private Color color;
    private double fuel;

    /**
     * ShipType contains TYPE(hullStrength, gadgetSlots, shieldSlots, weaponSlots, cargoBaySlots, fuelCapacity)
     */
    public enum ShipType {
        // TODO: Make numbers more better?
        //Name      hull  fuelC G  S  W  C   fuelE cost  color
        Flea        (10,  30,   0, 0, 0, 10, 30,   100,  Color.BLUE), 
        Gnat        (100, 100,  1, 0, 1, 15, 10,   200,  Color.RED), 
        Firefly     (100, 200,  1, 0, 1, 20, 20,   500,  Color.GREEN), 
        Mosquito    (300, 100,  1, 1, 2, 15, 10,   750,  Color.ORANGE), 
        Bumblebee   (100, 200,  1, 1, 2, 20, 15,   750,  Color.YELLOW),
        Beetle      (100, 1000, 1, 1, 0, 50, 5,    1000, Color.PURPLE),
        Hornet      (400, 100,  1, 2, 3, 20, 15,   1000, Color.BROWN), 
        Grasshopper (100, 200,  3, 2, 2, 30, 15,   1000, Color.GREY), 
        Termite     (500, 1000, 2, 3, 1, 60, 5,    5000, Color.WHITE), 
        Wasp        (500, 300,  2, 2, 4, 35, 20,   5000, Color.ALICEBLUE);

        private int hullStrength;
        private final double fuelCapacity;
        private int gadgetSlots;
        private int shieldSlots;
        private int weaponSlots;
        private int cargoBaySlots;
        private final double fuelEfficiency;
        private final int cost;
        private final Color color;

        ShipType(int hullStrength, double fuelCapacity, int gadgetSlots, int shieldSlots, int weaponSlots, int cargoBaySlots, double fuelEfficiency, int cost, Color color) {
            this.hullStrength = hullStrength;
            this.fuelCapacity = fuelCapacity;
            this.gadgetSlots = gadgetSlots;
            this.shieldSlots = shieldSlots;
            this.weaponSlots = weaponSlots;
            this.cargoBaySlots = cargoBaySlots;
            this.fuelEfficiency = fuelEfficiency;
            this.cost = cost;
            this.color = color;
        }
        public Color getColor() {
            return color;
        }
        public int getCost() {
            return cost;
        }
    };

    public Ship(ShipType type, EscapePod escapePod, Insurance insurance) {
        this.type = type;
        gadgets = new Gadget[type.gadgetSlots];
        shields = new Shield[type.shieldSlots];
        weapons = new Weapon[type.weaponSlots];
        cargoBay = new CargoBay(type.cargoBaySlots);
        hull = type.hullStrength;
        fuel = 0;
        this.escapePod = escapePod;
        this.insurance = insurance;
    }

    //Add things to ship

    public boolean addGadget(Gadget newGadget) {
        boolean success = false;
        for (Gadget gadget: gadgets) {
            if (gadget == null && !success) {
                gadget = newGadget;
                success = true;
            }
        }
        return success;
    }
    public boolean addShield(Shield newShield) {
        boolean success = false;
        for (Shield shield: shields) {
            if (shield == null && !success) {
                shield = newShield;
                success = true;
            }
        }
        return success;
    }
    public boolean addWeapon(Weapon newWeapon) {
        boolean success = false;
        for (Weapon weapon: weapons) {
            if (weapon == null && !success) {
                weapon = newWeapon;
                success = true;
            }
        }
        return success;
    }
    public void addEscapePod(EscapePod escapePod) {
        this.escapePod = escapePod;
    }
    public void addInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
    public double addFuel(double newFuel) {
        fuel += newFuel;
        if (fuel > type.fuelCapacity) {
            fuel = type.fuelCapacity;
        }
        return fuel;
    }

    //Remove things from ship

    public Gadget removeGadget(int position) {
        Gadget removed = null;
        if (position < gadgets.length) {
            removed = gadgets[position];
            gadgets[position] = null;
        }
        return removed;
    }   
    public Shield removeShield(int position) {
        Shield removed = null;
        if (position < shields.length) {
            removed = shields[position];
            shields[position] = null;
        }
        return removed;
    }   
    public Weapon removeWeapon(int position) {
        Weapon removed = null;
        if (position < weapons.length) {
            removed = weapons[position];
            weapons[position] = null;
        }
        return removed;
    }   
    public EscapePod removeEscapePod() {
        EscapePod removed = escapePod;
        escapePod = null;
        return removed;
    }
    public Insurance removeInsurance() {
        Insurance removed = insurance;
        insurance = null;
        return removed;
    }

    // Getters

    public Gadget[] getGadgets() {
        return gadgets;
    }
    public int getGadgetSlots() {
        return gadgets.length;
    }
    public Shield[] getShields() {
        return shields;
    }
    public int getShieldSlots() {
        return shields.length;
    }
    public Weapon[] getWeapons() {
        return weapons;
    }
    public int getWeaponSlots() {
        return weapons.length;
    }
    public CargoBay getCargoBay() {
        return cargoBay;
    }
    public int getCargoBaySlots() {
        return cargoBay.getCapacity();
    }
    public EscapePod getEscapePod() {
        return escapePod;
    }
    public Insurance getInsurance() {
        return insurance;
    }
    public int getHull() {
        return hull;
    }
    public double getFuel() {
        return fuel;
    }
    public double getFuelCapacity() {
        return type.fuelCapacity;
    }
    public double getFuelEfficiency() {
        return type.fuelEfficiency;
    }
    public double getMissingFuel() {
        return type.fuelCapacity - fuel;
    }
    public int getRange() {
        return (int)(fuel * type.fuelEfficiency);
    }

    // Other functionality

    public boolean travelDistance(int distance) {
        boolean success = false;
        if (distance <= getRange()) {
            fuel = fuel - distance / type.fuelEfficiency;
            success = true;
        }
        return success;
    }

    public int takeDamage(int damage) {
        // Shields???
        if (hull - damage >= 0) {
            hull -= damage;
        } else {
            hull = 0;
        }
        if (hull == 0) {
            System.out.println("YOU DEAD");
            // Kertsplode
        }
        return hull;
    }

    public int repairHull(int repairs) {
        hull += repairs;
        if (hull >= type.hullStrength) {
            hull = type.hullStrength;
        }
        return hull;
    }

    public int storeTradeGood(String goodName, int quantity) {
        return cargoBay.addTradeGood(goodName, quantity);
    }

    public boolean removeTradeGood(String goodName, int quantity) {
        return cargoBay.removeTradeGood(goodName, quantity);
    }

    // Return the unused space of the cargo bay
    public int getExtraSpace() {
        return cargoBay.getCapacity() - cargoBay.getCurrentSize();
    }

    public void emptyFuel(){
        fuel = 0;
    }
}