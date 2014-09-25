package spacetrader.items;

import javafx.scene.paint.Color;

/**
 * //Ship class, what the player travels around and transports cargo in.
 * @author David Purcell
 */
public class Ship {
    // Standard ship names from original game, can be added to later
    //Ship type contains TYPE(hullStrength, gadgetSlots, shieldSlots, weaponSlots, cargoBaySlots, fuelCapacity)
    private ShipType type;
    private Gadget[] gadgets;
    private Shield[] shields;
    private Weapon[] weapons;
    private CargoBay[] cargoBays;
    private EscapePod escapePod;
    private Insurance insurance;
    private Color color;
    private double fuel;
    
    public enum ShipType {
        //TODO: Make numbers more better?
        Flea (100, 4, 4,4,4,4,4, Color.BLUE), 
        Gnat (100, 100, 4,4,4,4,10, Color.RED), 
        Firefly (100, 4, 4,4,4,4,4, Color.GREEN), 
        Mosquito (100, 4, 4,4,4,4,4, Color.ORANGE), 
        Bumblebee (100, 4, 4,4,4,4,4, Color.YELLOW),
        Beetle (100, 4, 4,4,4,4,4, Color.PURPLE), 
        Hornet (100, 4, 4,4,4,4,4, Color.BROWN), 
        Grasshopper (100, 4, 4,4,4,4,4, Color.GREY), 
        Termite (100, 4, 4,4,4,4,4, Color.WHITE), 
        Wasp (100, 4, 4,4,4,4,4, Color.ALICEBLUE);
        
        private int hullStrength;
        private double fuelCapacity;
        private int gadgetSlots;
        private int shieldSlots;
        private int weaponSlots;
        private int cargoBaySlots;
        private double fuelEfficiency;
        private Color color;
        
        ShipType(int hullStrength, double fuelCapacity, int gadgetSlots, int shieldSlots, int weaponSlots, int cargoBaySlots, double fuelEfficiency, Color color){
            this.hullStrength = hullStrength;
            this.fuelCapacity = fuelCapacity;
            this.gadgetSlots = gadgetSlots;
            this.shieldSlots = shieldSlots;
            this.weaponSlots = weaponSlots;
            this.cargoBaySlots = cargoBaySlots;
            this.fuelEfficiency = fuelEfficiency;
            this.color = color;
        }
    };

    public Ship(ShipType type, EscapePod escapePod, Insurance insurance){
        this.type = type;
        gadgets = new Gadget[type.gadgetSlots];
        shields = new Shield[type.shieldSlots];
        weapons = new Weapon[type.weaponSlots];
        cargoBays = new CargoBay[type.cargoBaySlots];
        fuel = type.fuelCapacity;
        this.escapePod = escapePod;
        this.insurance = insurance;
    }
    
    //Add things to ship
    public boolean addGadget(Gadget newGadget){
        boolean success = false;
        for(Gadget gadget : gadgets){
            if(gadget == null && !success){
                gadget = newGadget;
                success = true;
            }
        }
        return success;
    }
    public boolean addShield(Shield newShield){
        boolean success = false;
        for(Shield shield : shields){
            if(shield == null && !success){
                shield = newShield;
                success = true;
            }
        }
        return success;
    }
    public boolean addWeapon(Weapon newWeapon){
        boolean success = false;
        for(Weapon weapon : weapons){
            if(weapon == null && !success){
                weapon = newWeapon;
                success = true;
            }
        }
        return success;
    }
    public boolean addCargoBay(CargoBay newCargoBay){
        boolean success = false;
        for(CargoBay cargoBay : cargoBays){
            if(cargoBay == null && !success){
                cargoBay = newCargoBay;
                success = true;
            }
        }
        return success;
    }
    public void addEscapePod(EscapePod escapePod){
        this.escapePod = escapePod;
    }
    public void addInsurance(Insurance insurance){
        this.insurance = insurance;
    }
    public double addField(double newFuel){
        fuel += newFuel;
        if(fuel > type.fuelCapacity){
            fuel = type.fuelCapacity;
        }
        return fuel;
    }
 
    //Remove things from ship
    public Gadget removeGadget(int position){
        Gadget removed = null;
        if(position < gadgets.length){
            removed = gadgets[position];
            gadgets[position] = null;
        }
        return removed;
    }   
    public Shield removeShield(int position){
        Shield removed = null;
        if(position < shields.length){
            removed = shields[position];
            shields[position] = null;
        }
        return removed;
    }   
    public Weapon removeWeapon(int position){
        Weapon removed = null;
        if(position < weapons.length){
            removed = weapons[position];
            weapons[position] = null;
        }
        return removed;
    }   
    public CargoBay removeCargoBay(int position){
        CargoBay removed = null;
        if(position < cargoBays.length){
            removed = cargoBays[position];
            cargoBays[position] = null;
        }
        return removed;
    }
    public EscapePod removeEscapePod(){
        EscapePod removed = escapePod;
        escapePod = null;
        return removed;
    }
    public Insurance removeInsurance(){
        Insurance removed = insurance;
        insurance = null;
        return removed;
    }
    
    //Getters
    public Gadget[] getGadgets(){
        return gadgets;
    }
    public Shield[] getShields(){
        return shields;
    }
    public Weapon[] getWeapons(){
        return weapons;
    }
    public CargoBay[] getCargoBays(){
        return cargoBays;
    }
    public EscapePod getEscapePod(){
        return escapePod;
    }
    public Insurance getInsurance(){
        return insurance;
    }
    public double getFuel(){
        return fuel;
    }
    public int getRange(){
        return (int)(fuel * type.fuelEfficiency);
    }
    
    //Other functionality
    public boolean travelDistance(int distance){
        boolean success = false;
        if(distance <= getRange()){
            fuel = Math.round(fuel - distance / type.fuelEfficiency);
            success = true;
        }
        return success;
    }
}