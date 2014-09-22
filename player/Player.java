package spacetrader.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Skill> skills;

    // Empty constructor
    public Player() {
        name = "NoName";
        skills = new ArrayList<>();
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