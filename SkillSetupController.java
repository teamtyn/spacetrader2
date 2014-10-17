package spacetrader;

import spacetrader.player.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;

/**
 * FXML Controller class
 * @author Clayton
 * @version 1.0
 */
public class SkillSetupController implements Initializable, ControlledScreen {
    @FXML private Button minus0;
    @FXML private Button minus1;
    @FXML private Button minus2;
    @FXML private Button minus3;
    @FXML private Button minus4;
    @FXML private Button plus0;
    @FXML private Button plus1;
    @FXML private Button plus2;
    @FXML private Button plus3;
    @FXML private Button plus4;
    @FXML private Label points0;
    @FXML private Label points1;
    @FXML private Label points2;
    @FXML private Label points3;
    @FXML private Label points4;
    @FXML private ProgressBar bar0;
    @FXML private ProgressBar bar1;
    @FXML private ProgressBar bar2;
    @FXML private ProgressBar bar3;
    @FXML private ProgressBar bar4;
    @FXML private Label skill0;
    @FXML private Label skill1;
    @FXML private Label skill2;
    @FXML private Label skill3;
    @FXML private Label skill4;
    @FXML private Label totalLabel;
    @FXML private ProgressBar totalBar;
    @FXML private TextField nameField;

    private final Map<String, Integer> plusButtonMap = new HashMap<>(5);
    private final Map<String, Integer> minusButtonMap = new HashMap<>(5);  
    public static Player player;
    private Label[] labelArray;
    private Label[] pointLabelArray;
    private Button[] minusButtonArray;
    private Button[] plusButtonArray;
    private ProgressBar[] barsArray;
    private Integer[] skillPointArray;
    private final int avgValue;
    private final int barMax;
    private final int maxPts;
    private int totalPts;
    private int len;
    private ScreensController parentController;

    public SkillSetupController() {
        player = new Player();
        avgValue = 10;
        barMax = 30;
        maxPts = 75;
        totalPts = 50;
    }

    // Initializes the controller class
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        plusButtonArray = new Button[] {plus0, plus1, plus2, plus3, plus4};
        minusButtonArray = new Button[] {minus0, minus1, minus2, minus3, minus4};
        pointLabelArray = new Label[] {points0, points1, points2, points3, points4};
        barsArray = new ProgressBar[] {bar0, bar1, bar2, bar3, bar4};
        labelArray = new Label[] {skill0, skill1, skill2, skill3, skill4};
        // DO NOT MOVE LEN DECLARATION
        len = labelArray.length;
        setUp();
    }
    
    @Override
    public void lazyInitialize() {}

    // Synchronizes Player's skills with the GUI for skill selection
    private void updatePlayerSkills() {
        for (int i = 0; i < len; i++) {
           player.getSkills().get(i).setValue(skillPointArray[i]);
       } 
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }

    // Initializes the players' skills and the GUI arrays
    private void setUp() {
        player.setSkillList(createSkillList());
        setUpControls();
    }

    // Functions when any plus button is pressed
    private void addToSkill(int index) {
        int skillPoints = skillPointArray[index];
        if ((skillPoints < barMax) && (totalPts < maxPts)) {
            skillPoints++;
            totalPts++;
        }
        skillPointArray[index] = skillPoints;
        updateProgressBar(index, skillPoints);
        updatePointLabel(index, skillPoints);
    }

    // Functions when any minus button is pressed
    private void subtractFromSkill(int index) {
        int skillPoints = skillPointArray[index];
        if(skillPoints > 0) {
            skillPoints--;
            totalPts--;
        }
        skillPointArray[index] = skillPoints;
        updateProgressBar(index, skillPoints);
        updatePointLabel(index, skillPoints);
    }

    // Keeps totalBar and totalLabel up to date
    private void updateTotalDisplays() {
        totalBar.setProgress((float)(maxPts - totalPts) / maxPts);
        totalLabel.setText(Integer.toString(maxPts - totalPts));
    }

    // Initializes the skills as an ArrayList
    private List<Skill> createSkillList() {
        Skill intelligence = new Skill("Intelligence");
        Skill looks = new Skill("Looks");
        Skill bloodPressure = new Skill("Blood Pressure");
        Skill intuition = new Skill ("Intuition");
        Skill luck = new Skill("Luck");
        List<Skill> skills = new ArrayList<>(asList(intelligence, looks, intuition, bloodPressure, luck));
        return skills;
    }

    /**
     * Initializes labelArray, skillPointArray, plusButtonMap, minusButtonMap,
     *   barsArray, totalBar, totalLabel, and pointLabelArray
     */
    private void setUpControls() {
        for(int i = 0; i < len; i++) {
            labelArray[i].setText(player.getSkills().get(i).getType());
        }
        skillPointArray = new Integer[len];
        for(int i = 0; i < len; i++) {
            skillPointArray[i] = avgValue;
        }
        for(int i = 0; i < len; i++) {
            plusButtonMap.put(plusButtonArray[i].getId(), i);
            minusButtonMap.put(minusButtonArray[i].getId(), i);
            updateProgressBar(i, avgValue);
            updatePointLabel(i, avgValue);
        }
    }

    // Helper method which updates a selected point label
    private void updatePointLabel(int index, int points) {
        pointLabelArray[index].setText(Integer.toString(points));
    }

    // Helper method which updates a selected progress 
    private void updateProgressBar(int index, int points) {
        barsArray[index].setProgress((float)points / barMax);
        updateTotalDisplays();
    }

    // All button handlers from here on out
    @FXML
    private void cancelButtonAction(ActionEvent event) {
        parentController.setScreen("Menu");
    }
    @FXML
    private void resetButtonAction(ActionEvent event) {
        nameField.setText("");
        setUpControls();
    }
    @FXML
    private void doneButtonAction(ActionEvent event) {
        String name = nameField.getText();
        if (name != null && !name.trim().equals("")) {
            player.setName(nameField.getText());
        }
        updatePlayerSkills();
        StringBuilder closingMessage = new StringBuilder();
        closingMessage.append("~~~PLAYER INFORMATION~~~\nNAME: ")
                        .append(player.getName()).append("\nSKILLS: \n");
        for (Skill skill : player.getSkills()) {
            closingMessage.append(skill.getType()).append(" - ")
                            .append(skill.getValue()).append("\n");
        }
        System.out.println(closingMessage.toString());
        GameModel.setPlayer(player);
        parentController.setScreen("StarMap");
    }
    @FXML
    private void plusButtonAction(ActionEvent event) {
        Node n = (Node)event.getSource();
        addToSkill(plusButtonMap.get(n.getId()));
    }
    @FXML
    private void minusButtonAction(ActionEvent event) {
        Node n = (Node)event.getSource();
        subtractFromSkill(minusButtonMap.get(n.getId()));
    }
}