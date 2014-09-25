package spacetrader.player;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import spacetrader.items.*;
import spacetrader.star_system.*;

public class Player {
    private String name;
    private List<Skill> skills;
    // Used to determine player's location in the universe as a whole
    private Point2D playerCoord;
    private StarSystem system;
    private Planet planet;
    private Ship ship;

    public Player() {
        name = "NoName";
        skills = new ArrayList<>();
        playerCoord = new Point2D(0, 0);
        ship = new Ship(Ship.ShipType.Gnat, null, null);
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

    public void setPlayerShip(Ship ship){
        this.ship = ship;
    }
    
    // Setter for coordinates
    public void setPlayerCoordinates(Point2D newLoc) {
        playerCoord = newLoc;
    }
    
    public void setPlayerSyetem(StarSystem system) {
        this.system = system;
    }
    
    public void setPlayerPlanet(Planet planet) {
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
    
    public Ship getShip(){
        return ship;
    }

    // Getter for coordinates
    public Point2D getPlayerCoordinates() {
        return playerCoord;
    }
    
    public double getPlayerCoordinateX() {
        return playerCoord.getX();
    }
    
    public double getPlayerCoordinateY() {
        return playerCoord.getY();
    }
    
    public StarSystem getPlayerSystem() {
        return system;
    }
    
    public Planet getPlayerPlanet() {
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