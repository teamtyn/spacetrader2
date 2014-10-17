package spacetrader;

import spacetrader.market.MarketSetup;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import spacetrader.market.TradeGood;
import spacetrader.player.Player;

/**
 * FXML Controller for the generation of a market
 * @author Clayton Kucera
 */
public class MarketController implements Initializable, ControlledScreen {
    @FXML private VBox buyGoodsVBox;//?
    @FXML private AnchorPane chartPane;
    @FXML private VBox sellGoodsVBox;//?
    @FXML private Label cargoLabel;
    @FXML private Label moneyLabel;
    @FXML private Label dialogueField;
    @FXML private Label priceField;

    private ScreensController parentController;
    public static MarketSetup market;
    private FadeTransition ft;
    private GoodsList buyList;//?
    private GoodsList sellList;//?
    private Player player;


    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }

    // Sets up the fade transition specifications
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ft = new FadeTransition(Duration.millis(1000), dialogueField);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
    }
    
    @Override
    public void lazyInitialize() {
        player = GameModel.getPlayer();
        display();
    }

    /**
     * Display calls the methods for updating the player info and updating the
     *   trade goods lists
     */
    public void display() {
        MarketController.market = GameModel.getPlayer().getPlanet().getMarket();
        cargoLabel.setText(Integer.toString(player.getShip().getExtraSpace()));
        moneyLabel.setText(Integer.toString(player.getMoney()));
        setUpGoodsLists();
    }

    /**
     * Clears the current lists of nodes
     *   Sets the prices of the player's trade goods
     *   Creates the GoodsLists that display our items
     */
    public void setUpGoodsLists() {

        // First clear the lists, because this method updates the lists as well
        buyGoodsVBox.getChildren().clear();
        sellGoodsVBox.getChildren().clear();
            
        // marketInventory is what the market is willing to sell, from getSellable method
        ArrayList<TradeGood> marketInventory = market.getSellable();
            
        // marketDemand is what the market is willing to buy, from getBuyable method
        ArrayList<TradeGood> marketDemand = market.getBuyable();
        
        // buyList displays what the player can buy
        buyList = new GoodsList(buyGoodsVBox, marketInventory, true);

        /*
        The sellList displays what the player can sell. 
            
        But first we have to assign a price to each trade good in the player's cargo, and this price is
        what the market is willing to pay. We do this with the addPricesToCargo method.
        I'll probably change this later so that it doesn't directly modify the prices of
        the player's cargo, since that could potentially cause problems later on.
        */
            //change the prices
            ArrayList <TradeGood> pricedCargo = addPricesToCargo(marketDemand, player.getShip().getCargo());
            //set up the sellList using the player's cargo
            sellList = new GoodsList(sellGoodsVBox, pricedCargo, false);
    }
    
    /**
     * Updates the prices of each trade good in the playerList to match the price of
     *   the trade good in marketList
     * Note that the quantity is not currently considered
     * Right now, if the price of the good is not updated,
     *   then that means that the planet's market does not want that good
     * If the price isn't updated it remains at 0
     * @param marketList
     * @param playerList
     * @return 
     */
    public ArrayList<TradeGood> addPricesToCargo(ArrayList<TradeGood> marketList, ArrayList<TradeGood> playerList) {
        for (TradeGood tgm: marketList) {
            for (TradeGood tgp: playerList) {
                if (tgm.type == tgp.type) {
                    tgp.setPrice(tgm.getPrice());
                }
            }
        }
        return playerList;
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        parentController.setScreen("StarMap");
    }

    /**
     * Sets up and then updates the past pricing chart
     * @param name The name of the good the chart applies to
     * @param price The current price of that good
     */
    public void generateChart(String name, int price) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        AreaChart<Number, Number> ac = new AreaChart<>(xAxis, yAxis);
        ac.setTitle(name + " prices over last 5 days");
        XYChart.Series series = new XYChart.Series();
        // TODO: Make the chart values not random
        for (int i = 0; i < 4; i++) {
            series.getData().add(new XYChart.Data(i - 5, Math.random() * price * 3 + 1));
        }
        series.getData().add(new XYChart.Data(0, price));
        ac.getData().addAll(series);
        chartPane.getChildren().clear();
        chartPane.getChildren().add(ac);
        ac.setPrefHeight(300);
        ac.setPrefWidth(300);
        ac.setId("chart");
    }

    /**
     * Makes a purchase
     * Subtracts the price of the good, stores it, removes it
     *   from the market, then updates the display
     * If out of cargo space or money, a dialogue is shown on the bottom dock
     * @param good The good to be purchased
     */
    public void buy(TradeGood good) {
        if (player.getMoney() > good.getPrice()) {
            if (player.getShip().storeTradeGood(good.type.name, 1) > 0) {
                player.setMoney(player.getMoney() - good.getPrice());
                market.decreaseQuantity(good, 1);
            } else {
                dialogueField.setText("You're out of cargo space!");
                if (ft != null) {
                    ft.play();
                }
            }
        } else {
            dialogueField.setText("You don't have enough money!");
            if (ft != null) {
                ft.play();
            }
        }
        display();
    }

    /**
     * Makes a sale
     * Adds the price of the good, removes it from cargo bay, adds it
     *   to the market, then updates the display
     * @param good The good to be sold
     */
    public void sell(TradeGood good) {
        if (player.getShip().removeTradeGood(good.type.name, 1)) {
            player.setMoney(player.getMoney() + good.getPrice());
            market.increaseQuantity(good, 1);
        }
        display();
    }

    /**
     * Buy Button
     */
    private class BuyButton extends Button {
        public BuyButton(TradeGood good) {
            super("BUY");
            this.setOnAction((ActionEvent event) -> {
                buy(good);
            });
            this.setId("row-button");
        }
    }

    /**
     * Sell Button
     */
    private class SellButton extends Button {
        public SellButton(TradeGood good) {
            super("SELL");
            this.setOnAction((ActionEvent event) -> {
                sell(good);
            });
            this.setId("row-button");
        }
    }

    /**
     * GoodsRow is a HBox that contains two labels and a buy/sell button
     */
    private class GoodsRow extends HBox {
        public GoodsRow(TradeGood good, boolean isABuyRow, boolean isDisabled) {
            // Add the labels for good's name and amount
            this.getChildren().add(new Label(good.type.name));
            this.getChildren().add(new Label("x" + good.getQuantity()));
 
            Button button;
            if (isABuyRow) {
                button = new BuyButton(good);
            } else {
                button = new SellButton(good);
            }
            if (isDisabled) {
                button.setDisable(true);
            }
            this.getChildren().add(button);
            this.setOnMouseEntered((MouseEvent event) -> {
                generateChart(good.type.name, good.getPrice());
                priceField.setText(good.type.name + " costs " + good.getPrice() + " per unit.");
            });
            this.setId("goods-row");
            this.setAlignment(Pos.CENTER_RIGHT);
            this.setSpacing(30);
        }
    }

    /**
     * 
     */
    private class GoodsList {
        private final ArrayList<TradeGood> tradeGoods;
        private final boolean isABuyList;
        private final VBox vBox;

        public GoodsList(VBox vBox, ArrayList<TradeGood> tradeGoods, boolean isABuyList) {
            this.tradeGoods = tradeGoods;
            this.vBox = vBox;
            this.isABuyList = isABuyList;
            listGoods();
        }

        // Set up the VBox
        public final void listGoods() {
            for (TradeGood good: tradeGoods) {
                boolean isDisabled = false;

                // Right now, if the price of the good is not updated,
                // then that means that the planet's market does not want
                // that good. If the price isn't updated it remains at 0.

                //If the good's quantity or price are less than or equal to 0
                if (good.getQuantity() <= 0 || good.getPrice() <= 0) {
                        //disable the button for that good.
                        isDisabled = true;
                    }
                GoodsRow row = new GoodsRow(good, isABuyList, isDisabled);
                //add the row to the GoodsList
                this.addChild(row);
            }
        }

        public void addChild(Node node) {
            vBox.getChildren().add(node);
        }
    }
}