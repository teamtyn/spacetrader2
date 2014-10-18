package spacetrader;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import spacetrader.items.*;
import spacetrader.items.Ship.ShipType;
import spacetrader.player.Player;

/**
 * FXML Controller class for SpaceStation
 * @author Purcell7
 */
public class SpaceStationController implements Initializable, ControlledScreen {
    @FXML private Button buyShip;
    @FXML private VBox shipList;
    @FXML private Label moneyLabel;
    @FXML private Button viewPlayerCardButton;
    @FXML private Button shopForPartsButton;

    @FXML private ProgressBar fuel;
    @FXML private Label fuelLabel;
    @FXML private Label fuelCostLabel;
    @FXML private Button emptyFuel;
    @FXML private Button add1Fuel;
    @FXML private Button add10Fuel;
    @FXML private Button add50Fuel;
    @FXML private Button add100Fuel;

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
    private Player player;
    private ScreensController parentController;

    public void updateFuel() {
        fuel.setProgress(player.getShip().getFuel() / player.getShip().getFuelCapacity());
        fuelLabel.setText(player.getShip().getFuel() + "/" + player.getShip().getFuelCapacity());
        add1Fuel.setDisable(player.getShip().getMissingFuel() < 1);
        add10Fuel.setDisable(player.getShip().getMissingFuel() < 10);
        add50Fuel.setDisable(player.getShip().getMissingFuel() < 50);
        add100Fuel.setDisable(player.getShip().getMissingFuel() < 100);
        moneyLabel.setText(Integer.toString(player.getMoney()));
    }

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
    }

    public void updateShip() {
        if (otherShip.type == myShip.type) {
            shipCost.setText("You already own this type of ship");
            buyShip.setDisable(true);
        } else {
            shipCost.setText(Integer.toString(otherShip.type.getCost()));
            buyShip.setDisable(false);
        }
        myShipStats();
        otherShipStats();
    }

    public void buyShip() {
        player.subtractMoney(otherShip.type.getCost());
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
        if (player.getShip().getFuel() > otherShip.getFuelCapacity()) {
            double excessFuel = player.getShip().getFuel() - otherShip.getFuelCapacity(); 
            System.out.println("Sold excess fuel (" + excessFuel + ") at market value for " + (excessFuel * fuelCost));
            player.addMoney((int)(excessFuel * fuelCost));
        }
        otherShip.addFuel(player.getShip().getFuel());
        player.setShip(otherShip);
        otherShip = new Ship(otherShip.type, null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @Override
    public void lazyInitialize() {
        player = GameModel.getPlayer();
        myShip = player.getShip();
        otherShip = player.getShip();
        fuelCostLabel.setText(Integer.toString(fuelCost));
        updateFuel();
        updateShip();
        for (ShipType type: ShipType.values()) {
            HBox row = new HBox();
            row.getChildren().add(new Label(type.name()));
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                otherShip = new Ship(type, null, null);
                for (Node node: shipList.getChildren()) {
                    node.setStyle("-fx-background-color: #FFFFFF;");
                }
                row.setStyle("-fx-background-color: #EEEEEE;");
                buyShip.setDisable(false);
                updateShip();
            });
            shipList.getChildren().add(row);
        }
    }

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
        if (otherShip.type.getCost() <= player.getMoney()) {
            buyShip();
            updateFuel();
        }
        updateShip();
    }

    @FXML
    private void emptyFuelButtonAction(ActionEvent event) {
        player.getShip().emptyFuel();
        updateFuel();
    }

    @FXML
    private void add1FuelButtonAction(ActionEvent event) {
        if (player.getMoney() >= fuelCost) {
            player.getShip().addFuel(1);
            player.subtractMoney(fuelCost);
            updateFuel();
        }
    }

    @FXML
    private void add10FuelButtonAction(ActionEvent event) {
        if (player.getMoney() >= fuelCost * 10) {
            player.getShip().addFuel(10);
            player.subtractMoney(fuelCost * 10);
            updateFuel();
        }
    }

    @FXML
    private void add50FuelButtonAction(ActionEvent event) {
        if (player.getMoney() >= fuelCost * 50) {
            player.getShip().addFuel(50);
            player.subtractMoney(fuelCost * 50);
            updateFuel();
        }
    }

    @FXML
    private void add100FuelButtonAction(ActionEvent event) {
        if (player.getMoney() >= fuelCost * 100) {
            player.getShip().addFuel(100);
            player.subtractMoney(fuelCost * 100);
            updateFuel();
        }
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }   
}