package spacetrader;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import spacetrader.items.*;
import spacetrader.items.Ship.ShipType;
import spacetrader.player.Player;

/**
 * FXML Controller class for SpaceStation
 * @author Purcell7
 */
public class SpaceStationController implements Initializable, ControlledScreen {
    @FXML private Button buyShip;
    @FXML private TextField confirmationField;
    @FXML private VBox shipList;
    @FXML private Label moneyLabel;
    @FXML private Label shipDialogueField;
    @FXML private Label fuelDialogueField;
    @FXML private Button viewPlayerCardButton;
    @FXML private Button shopForPartsButton;

    @FXML private ProgressBar fuelProgress;
    @FXML private Label fuelLabel;
    @FXML private Label fuelCostLabel;
    @FXML private Button cancelFuelButton;
    @FXML private Button fillFuelButton;
    @FXML private Button confirmFuelButton;

    @FXML private Label otherShipLabel;
    @FXML private Pane otherShipPicturePane;
    @FXML private Label hullStrength;
    @FXML private Label fuelCapacity;
    @FXML private Label gadgetSlots;
    @FXML private Label shieldSlots;
    @FXML private Label weaponSlots;
    @FXML private Label cargoBaySlots;
    @FXML private Label fuelEfficiency;
    @FXML private Label shipCost;

    @FXML private Pane myShipPicturePane;
    @FXML private Label myHullStrength;
    @FXML private Label myFuelCapacity;
    @FXML private Label myGadgetSlots;
    @FXML private Label myShieldSlots;
    @FXML private Label myWeaponSlots;
    @FXML private Label myCargoBaySlots;
    @FXML private Label myFuelEfficiency;
    @FXML private Label myShipValue;

    private Ship myShip;
    private Ship otherShip;
    private final int fuelCost = 10;
    private double tempFuel;
    private Player player;
    private FadeTransition ft;
    private ScreensController parentController;

    /**
     * Determines which fuel buttons should currently be disabled
     * Maintains the progress bar and labels associated with fuel
     */
    public void updateFuel() {
        fuelProgress.setProgress(tempFuel / player.getShip().getFuelCapacity());
        fuelLabel.setText(Math.round(tempFuel) + "/" + Math.round(player.getShip().getFuelCapacity()));
        confirmFuelButton.setDisable(player.getShip().getFuel() == tempFuel);
        cancelFuelButton.setDisable(player.getShip().getFuel() == tempFuel);
        fillFuelButton.setDisable(tempFuel == player.getShip().getFuelCapacity());
        moneyLabel.setText(Integer.toString(player.getMoney()));
    }

    /**
     * Sets up the My Ship panel with the appropriate info
     * Picture is currently a colored rectangle, TODO: Get Josh's 3D ship
     */
    public void myShipStats() {
        myHullStrength.setText(Integer.toString(myShip.getHull()));
        myFuelCapacity.setText(Double.toString(myShip.getFuelCapacity()));
        myGadgetSlots.setText(Integer.toString(myShip.getGadgetSlots()));
        myShieldSlots.setText(Integer.toString(myShip.getShieldSlots()));
        myWeaponSlots.setText(Integer.toString(myShip.getWeaponSlots()));
        myCargoBaySlots.setText(Integer.toString(myShip.getCargoBaySlots()));
        myFuelEfficiency.setText(Double.toString(myShip.getFuelEfficiency()));
        myShipValue.setText(Integer.toString(myShip.type.getCost()));
        myShipPicturePane.getChildren().removeAll();
        Rectangle myShipPicture = new Rectangle(100, 10, 100, 100);
        myShipPicture.setFill(myShip.type.getColor());
        myShipPicturePane.getChildren().add(myShipPicture);
    }

    /**
     * Sets up the Other Ship panel with the appropriate info
     * Picture is currently a colored rectangle, TODO: Get Josh's 3D ship
     */
    public void otherShipStats() {
        otherShipLabel.setText(otherShip.type.name());
        hullStrength.setText(Integer.toString(otherShip.getHull()));
        fuelCapacity.setText(Double.toString(otherShip.getFuelCapacity()));
        gadgetSlots.setText(Integer.toString(otherShip.getGadgetSlots()));
        shieldSlots.setText(Integer.toString(otherShip.getShieldSlots()));
        weaponSlots.setText(Integer.toString(otherShip.getWeaponSlots()));
        cargoBaySlots.setText(Integer.toString(otherShip.getCargoBaySlots()));
        fuelEfficiency.setText(Double.toString(otherShip.getFuelEfficiency()));
        otherShipPicturePane.getChildren().removeAll();
        Rectangle otherShipPicture = new Rectangle(100, 10, 100, 100);
        otherShipPicture.setFill(otherShip.type.getColor());
        otherShipPicturePane.getChildren().add(otherShipPicture);
        shipCost.setText(Integer.toString(otherShip.type.getCost()));
    }

    /**
     * Informs the user if they are not allowed to buy the currently selected ship
     * Calls the methods to keep myShip and otherShip up to date
     */
    public void updateShip() {
        if (otherShip.type == myShip.type) {
            shipDialogueField.setText("You already own this type of ship!");
            buyShip.setDisable(true);
        } else if (otherShip.type.getCost() > player.getMoney()) {
            shipDialogueField.setText("You cannot afford this ship!");
            buyShip.setDisable(true);
        } else {
            shipDialogueField.setText("");
            buyShip.setDisable(false);
        }
        myShipStats();
        otherShipStats();
    }

    /**
     * Handles purchase of a ship
     * Subtracts money from the player, transfers the parts, and then
     *   resets all variables after giving the player their new ship
     */
    public void buyShip() {
        player.subtractMoney(otherShip.type.getCost());
        transferParts();
        player.setShip(otherShip);
        myShip = otherShip;
        otherShip = new Ship(otherShip.type, null, null);
    }

    /**
     * Transfers parts from player's old ship to new ship upon purchase
     * TODO: Account for new ship not being able to fit any of these things, esp. goods
     */
    public void transferParts() {
        otherShip.addEscapePod(player.getShip().getEscapePod());
        otherShip.addInsurance(player.getShip().getInsurance());
        // TODO: 
        for (Gadget gadget: player.getShip().getGadgets()) {
            otherShip.addGadget(gadget);
        }
        for (Shield shield: player.getShip().getShields()) {
            otherShip.addShield(shield);
        }
        for (Weapon weapon: player.getShip().getWeapons()) {
            otherShip.addWeapon(weapon);
        }
        HashMap<String, Integer> goods = player.getShip().getCargoBay().getGoods();
        for (String goodName: goods.keySet()) {
            otherShip.storeTradeGood(goodName, goods.get(goodName));
        }
    }

    // Sets up the fade transition specifications
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ft = new FadeTransition(Duration.millis(1000), fuelDialogueField);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
    }

    /**
     * 
     */
    @Override
    public void lazyInitialize() {
        player = GameModel.getPlayer();
        myShip = player.getShip();
        otherShip = player.getShip();
        fuelCostLabel.setText(Integer.toString(fuelCost));
        tempFuel = myShip.getFuel();
        updateFuel();
        updateShip();
        for (ShipType type: ShipType.values()) {
            int mult = type.ordinal();
            HBox row = new HBox();
            Label label = new Label(type.name());
            label.setPrefSize(200, 25);
            label.setAlignment(Pos.CENTER);
            row.getChildren().add(label);
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                otherShip = new Ship(type, null, null);
                for (Node node: shipList.getChildren()) {
                    node.setStyle("-fx-background-color: #FFFFFF;");
                }
                row.setStyle("-fx-background-color: #EEEEEE;");
                row.setPrefSize(200, 25);
                row.setLayoutY(mult * 25);
                buyShip.setDisable(false);
                updateShip();
            });
            shipList.getChildren().add(row);
        }
    }

    // All button handlers below here

    @FXML
    private void backButtonAction(ActionEvent event) {
        parentController.setScreen("StarMap");
    }

    @FXML
    private void viewPlayerCardButtonAction(ActionEvent event) {
        // TODO
        System.out.println("Viewing player card.");
    }

    @FXML
    private void shopForPartsButtonAction(ActionEvent event) {
        // TODO
        System.out.println("Shopping for parts.");
    }

    @FXML
    private void buyShipButtonAction(ActionEvent event) {
        if (confirmationField.getText().trim().equals(Integer.toString(otherShip.type.getCost()))) {
            buyShip();
            updateFuel();
            updateShip();
        } else {
            shipDialogueField.setText("Please confirm the price of your new ship.");
        }
    }

    @FXML
    private void confirmFuelButtonAction(ActionEvent event) {
        player.getShip().addFuel(tempFuel);
        player.subtractMoney((int)(tempFuel * fuelCost));
        updateFuel();
    }

    @FXML
    private void cancelFuelButtonAction(ActionEvent event) {
        tempFuel = player.getShip().getFuel();
        updateFuel();
    }

    @FXML
    private void fillFuelButtonAction(ActionEvent event) {
        tempFuel = player.getShip().getFuelCapacity() - player.getShip().getFuel();
        updateFuel();
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }   
}