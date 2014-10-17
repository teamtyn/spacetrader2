package spacetrader;

import java.net.URL;
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
import spacetrader.market.TradeGood;
import spacetrader.player.Player;

/**
 * FXML Controller class for SpaceStation
 * @author Purcell7
 */
public class SpaceStationController implements Initializable, ControlledScreen {
    @FXML private ProgressBar fuel;
    @FXML private Label fuelLabel;
    @FXML private Label fuelCostLabel;
    @FXML private Label moneyLabel;
    @FXML private Button emptyFuel;
    @FXML private Button add1Fuel;
    @FXML private Button add10Fuel;
    @FXML private Button add50Fuel;
    @FXML private Button add100Fuel;
    @FXML private Button buyShip;
    @FXML private Button viewMyShip;
    @FXML private VBox shipList;
    @FXML private Pane viewShipDetails;
    @FXML private Label hullStrength;
    @FXML private Label fuelCapacity;
    @FXML private Label gadgetSlots;
    @FXML private Label shieldSlots;
    @FXML private Label weaponSlots;
    @FXML private Label cargoBaySlots;
    @FXML private Label fuelEfficiency;
    @FXML private Label shipCost;

    private Ship viewShip;
    private final int fuelCost = 10;
    private Player player;
    private ScreensController parentController;

    @FXML
    private void backButtonAction(ActionEvent event) {
        if (ScreensController.isInitialized("StarMap")) {
            ((StarMapController)ScreensController.getController("StarMap")).viewPlanet(player.getPlanet(), player.getSystem());
            parentController.setScreen("StarMap");
        }
    }

    public void updateFuel() {
        fuel.setProgress(player.getShip().getFuel() / player.getShip().getFuelCapacity());
        fuelLabel.setText(player.getShip().getFuel() + "/" + player.getShip().getFuelCapacity());
        add1Fuel.setDisable(player.getShip().getMissingFuel() < 1);
        add10Fuel.setDisable(player.getShip().getMissingFuel() < 10);
        add50Fuel.setDisable(player.getShip().getMissingFuel() < 50);
        add100Fuel.setDisable(player.getShip().getMissingFuel() < 100);
        moneyLabel.setText(Integer.toString(player.getMoney()));
    }

    public void updateShip() {
        if (viewShip.type == player.getShip().type) {
            shipCost.setText("You already own this type of ship");
        } else {
            shipCost.setText(Integer.toString(viewShip.type.getCost()));
        }
        buyShip.setDisable(viewShip.type == player.getShip().type);
        viewShipDetails.getChildren().removeAll();
        Rectangle ship = new Rectangle(100, 10, 100, 100);
        ship.setFill(viewShip.type.getColor());
        viewShipDetails.getChildren().add(ship);
        hullStrength.setText(Integer.toString(viewShip.getHull()));
        fuelCapacity.setText(Double.toString(viewShip.getFuelCapacity()));
        gadgetSlots.setText(Integer.toString(viewShip.getGadgetSlots()));
        shieldSlots.setText(Integer.toString(viewShip.getShieldSlots()));
        weaponSlots.setText(Integer.toString(viewShip.getWeaponSlots()));
        cargoBaySlots.setText(Integer.toString(viewShip.getCargoBaySlots()));
        fuelEfficiency.setText(Double.toString(viewShip.getFuelEfficiency()));
    }

    public void buyShip() {
        player.subtractMoney(viewShip.type.getCost());
        viewShip.addEscapePod(player.getShip().getEscapePod());
        viewShip.addInsurance(player.getShip().getInsurance());
        // TODO: 
        for (Gadget gadget: player.getShip().getGadgets()) {
            viewShip.addGadget(gadget);
        }
        for (Shield shield: player.getShip().getShields()) {
            viewShip.addShield(shield);
        }
        for (Weapon weapon: player.getShip().getWeapons()) {
            viewShip.addWeapon(weapon);
        }
        for (TradeGood good: player.getShip().getCargo()) {
            viewShip.storeTradeGood(good.type.name, good.getQuantity());
        }
        if (player.getShip().getFuel() > viewShip.getFuelCapacity()) {
            double excessFuel = player.getShip().getFuel() - viewShip.getFuelCapacity(); 
            System.out.println("Sold excess fuel (" + excessFuel + ") at market value for " + (excessFuel * fuelCost));
            player.addMoney((int)(excessFuel * fuelCost));
        }
        viewShip.addFuel(player.getShip().getFuel());
        player.setShip(viewShip);
        viewShip = new Ship(viewShip.type, null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @Override
    public void lazyInitialize() {
        player = GameModel.getPlayer();
        viewShip = player.getShip();
        fuelCostLabel.setText(Integer.toString(fuelCost));
        moneyLabel.setText(Integer.toString(player.getMoney()));
        updateFuel();
        updateShip();
        for (ShipType type: ShipType.values()) {
            HBox row = new HBox();
            row.getChildren().add(new Label(type.name()));
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewShip = new Ship(type, null, null);
                for (Node node: shipList.getChildren()) {
                    node.setStyle("-fx-background-color: #FFFFFF;");
                }
                row.setStyle("-fx-background-color: #EEEEEE;");
                buyShip.setDisable(false);
                updateShip();
            });
            shipList.getChildren().add(row);
        }
        buyShip.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (viewShip.type.getCost() <= player.getMoney()) {
                buyShip();
                updateFuel();
            }
            updateShip();
        });
        viewMyShip.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            viewShip = player.getShip();
            updateShip();
        });
        emptyFuel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            player.getShip().emptyFuel();
            updateFuel();
        });
        add1Fuel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (player.getMoney() >= fuelCost) {
                player.getShip().addFuel(1);
                player.subtractMoney(fuelCost);
                updateFuel();
            }
        });
        add10Fuel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (player.getMoney() >= fuelCost * 10) {
                player.getShip().addFuel(10);
                player.subtractMoney(fuelCost * 10);
                updateFuel();
            }
        });
        add50Fuel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (player.getMoney() >= fuelCost * 50) {
                player.getShip().addFuel(50);
                player.subtractMoney(fuelCost * 50);
                updateFuel();
            }
        });
        add100Fuel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (player.getMoney() >= fuelCost * 100) {
                player.getShip().addFuel(100);
                player.subtractMoney(fuelCost * 100);
                updateFuel();
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }   
}