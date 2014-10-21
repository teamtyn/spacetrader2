package spacetrader2.player;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import spacetrader2.items.*;
import spacetrader2.star_system.Planet;
import spacetrader2.star_system.StarSystem;


public class Player {
    private String name;
    private List<Skill> skills;
    private List<Planet> knownPlanets;
    // Used to determine player's location in the universe as a whole
    private Point2D playerCoord;
    private StarSystem system;
    private Planet planet;
    private Ship ship;

    public Player() {
        name = "NoName";
        skills = new ArrayList<>();
        knownPlanets = new ArrayList<>();
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
    
    public StarSystem getSystem() {
        return system;
    }
    
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
    
    public boolean knowsPlanet(Planet planet) {
        return knownPlanets.contains(planet);
    }
    
    public void addKnownPlanet(Planet planet) {
        knownPlanets.add(planet);
    }
}