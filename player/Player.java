package spacetrader.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import spacetrader.items.*;
import spacetrader.star_system.*;
import spacetrader.ui.Point;

public class Player implements Serializable {
    private String name;
    private List<Skill> skills;
    // Used to determine player's location in the universe as a whole
    private Point coord;
    private StarSystem system;
    private Planet planet;
    private Ship ship;
    private int money;

    public Player() {
        name = "NoName";
        skills = new ArrayList<>();
        coord = new Point(0, 0);
        ship = new Ship(Ship.ShipType.Gnat, null, null);
        money = 10000;
    }

    // Setter for money
    public void setMoney(int money) {
        this.money = money;
    }

    // Subtract specified amount of money
    public void subtractMoney(int money) {
        this.money -= money;
    }

    // Subtracts as much money as it can
    public int attemptToSubtractMoney(int money) {
        int removed = money;
        if (money > this.money) {
            removed = this.money;
            this.money = 0;
        } else {
            this.money -= money;
        }
        return removed;
    }

    // Add specified amount of money
    public void addMoney(int money) {
        this.money += money;
    }

    // Setter for name
    public void setName(String newName) {
        this.name = newName;
    }

    // Setter for the list of skills
    public void setSkillList(List<Skill> newSkills) {
         skills = newSkills;
    }

    // Setter for value of a specified skill
    public void setSkill(String type, int value) {
        for (Skill skill: this.skills) {
            if (skill.getType().equals(type)) {
                skill.setValue(value);
            }
        }
    }

    public void setShip(Ship ship){
        this.ship = ship;
    }
    
    // Setter for coordinates
    public void setCoordinates(Point newLoc) {
        coord = newLoc;
    }
    
    public void setSystem(StarSystem system) {
        this.system = system;
    }
    
    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    // Increase the level of a skill by the specified value
    public void increaseSkill(String type, int value) {
        for (Skill skill: this.skills) {
            if (skill.getType().equals(type)) {
                skill.increaseValue(value);
            }
        }
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    // Getter for skills
    public List<Skill> getSkills() {
        return this.skills;
    }

    // Getter for money
    public int getMoney() {
        return money;
    }

    // Getter for ship
    public Ship getShip(){
        return ship;
    }

    // Getter for coordinates
    public Point getCoordinates() {
        return coord;
    }

    public double getX() {
        return coord.getX();
    }

    public double getY() {
        return coord.getY();
    }

    public StarSystem getSystem() {
        return system;
    }

    // Getter for planet
    public Planet getPlanet() {
        return planet;
    }

    // Getter for an individual skill
    public Skill getSkill(String type) {
        for (Skill skill: this.skills) {
            if (skill.getType().equals(type)) {
                return skill;
            }
        }
        return null;
    }
}