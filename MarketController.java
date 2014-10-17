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
import spacetrader.star_system.Planet;

/**
 * FXML Controller for the generation of a market
 * @author Clayton Kucera
 */
public class MarketController implements Initializable, ControlledScreen {
    @FXML private VBox buyGoodsVBox;
    @FXML private AnchorPane chartPane;
    @FXML private VBox sellGoodsVBox;
    @FXML private Label sellLabel;
    @FXML private Label buyLabel;
    @FXML private Label playerInfo;
    @FXML private Label statusLabel;
    @FXML private Button backButton;

    private ScreensController parentController;
    // THIS WILL BE CHANGED ONCE WE HAVE A SINGLETON
    public static MarketSetup market;
    private FadeTransition ft;
    private GoodsList buyList;
    private GoodsList sellList;
    // THIS WILL BE CHANGED ONCE WE HAVE A SINGLETON
    private Player player;


    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ft = new FadeTransition(Duration.millis(1000), statusLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
    }
    
    @Override
    public void lazyInitialize() {
        player = GameModel.getPlayer();
        // DELETE FROM HERE
        player.getShip().storeTradeGood("Furs", 3);
        player.getShip().storeTradeGood("Water", 3);
        player.getShip().storeTradeGood("Firearms", 3);
        player.getShip().storeTradeGood("Narcotics", 3); 
        // DELETE TO HERE
        display();
    }

    /**
     * Simply updates the text displayed on our playerInfo label.
     * Cargo space and money.
     */
    public void updatePlayerInfo() {
        playerInfo.setText("Cargo Space Remaining: " + player.getShip().getExtraSpace() + "\nMoney Remaining: " + 
                player.getMoney() + " bitcoins");
    }

    /**
     * Display calls the methods for updating the player info and updating the
     * trade goods lists.
     */
    public void display() {
        setMarketSetup();
        setUpGoodsLists();
        updatePlayerInfo();
    }
    
    public static void setMarketSetup() {
        MarketController.market = GameModel.getPlayer().getPlanet().getMarketSetup();
    }

    /**
     * setUpGoodsLists does several things:
     *      clears the current lists of nodes
     *      sets the prices of the player's trade goods
     *      creates the GoodsLists that display our items
     */
    public void setUpGoodsLists() {
        //Okay, sorry. I made this SUPER CONFUSING when I first coded this.
        //First clear the lists, because we use this method to update the list as well.
            buyGoodsVBox.getChildren().clear();
            sellGoodsVBox.getChildren().clear();
            
        //The marketInventory is what the market has and is willing to sell, told to it by
        //its getSellable method.
            ArrayList<TradeGood> marketInventory = market.getSellable();
            
        //marketDemand is what the market is willing to buy, and for how much. Gets this from
        //.getBuyable method.
            ArrayList<TradeGood> marketDemand = market.getBuyable();
        
        //The Buy List displays what the player can buy.
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
     * the trade good in marketList. Note that the quantity is not currently
     * considered.
     * 
     * Right now, if the price of the good is not updated,
     * then that means that the planet's market does not want
     * that good. If the price isn't updated it remains at 0.
     * @param marketList
     * @param playerList
     */
    public ArrayList<TradeGood> addPricesToCargo(ArrayList<TradeGood> marketList, ArrayList<TradeGood> playerList) {
        for (TradeGood tgm: marketList) {
            for (TradeGood tgp: playerList) {
                if (tgm.type == tgp.type) {
                    tgp.setPrice(tgm.getPrice());
                    System.out.println("market " + tgm.type.name + " $ " + tgm.getPrice() + "\n player " + tgp.type.name + " $ " + tgp.getPrice());
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
     * Updates the status pane, which gives the player feedback from his
     * attempted purchases or sales.
     * @param status
     */
    public void statusPanelMessage(String status) {
        statusLabel.setText(status);
        //using a fade transition
        if(ft != null) {
            ft.play();
        }
    }

    /**
     * Updates the labels displaying buy price and sell price.
     * @param name
     * @param price
     */
    public void updateLabels(String name, int price) {
        sellLabel.setText(name + " will sell for " + price + " bitcoins per unit.");
        buyLabel.setText("You can purchase " + name + " for " + price + " bitcoins per unit.");
    }

    /**
     * For setting up and then updating the chart. Right now all values are
     * delightfully random except for the final value of the chart, which is
     * the true trade good price.
     * @param name
     * @param price
     */
    public void chart(String name, int price) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        AreaChart<Number,Number> ac = new AreaChart<>(xAxis,yAxis);
        ac.setTitle(name + " prices over last 5 days");

        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for(int i = 0; i < 4; i++) {
            series.getData().add(new XYChart.Data(i - 5, Math.random() * price * 3 + 1));
        }
        series.getData().add(new XYChart.Data(0, price));

        ac.getData().addAll(series);
        chartPane.getChildren().clear();
        chartPane.getChildren().add(ac);
        ac.setMaxHeight(300);
        ac.setMaxWidth(300);
        ac.setId("chart");
    }

    /**
     * Makes a purchase. Subtracts the price of the good, stores it, removes it
     * from the market, then updates the display.
     * @param good - the good to be purchased
     * @param amount - the amount of good to be purchased
     */
    public void buy(TradeGood good, int amount) {
        int newMoney = player.getMoney() - good.getPrice() * amount;
        if (newMoney > 0) {
            // TODO: Buy more than one thing at at time
            if (player.getShip().storeTradeGood(good.type.name, 1) > 0) {
                player.setMoney(player.getMoney() - good.getPrice() * amount);
                market.decreaseQuantity(good, amount);
            } else {
                statusPanelMessage("Hey-oh! You're out of cargo space.");
            }
        } else {
            statusPanelMessage("Ohohoho! You don't have enough money!");
        }
        display();
    }

    /**
     * Making a sale. Removes the good from the cargo, adds the price to
     * the player's money, and adds the good to the market.
     * @param good
     * @param amount
     */
    public void sell(TradeGood good, int amount) {
        // TODO: Sell more than one thing at at time
        if (player.getShip().removeTradeGood(good.type.name, 1)) {
            player.setMoney(player.getMoney() + good.getPrice() * amount);
            market.increaseQuantity(good, amount);
        }
        display();
    }

    /**
     * Buy Button.
     */
    private class BuyButton extends Button {
        public BuyButton(TradeGood good) {
            super("BUY");
            this.setOnAction((ActionEvent event) -> {
                buy(good, 1);
            });
            this.setId("row-button");
        }
    }

    /**
     * Sell Button.
     */
    private class SellButton extends Button {
        public SellButton(TradeGood good) {
            super("SELL");
            this.setOnAction((ActionEvent event) -> {
                sell(good, 1);
            });
            this.setId("row-button");
        }
    }

    /**
     * Goods Row is an HBox that contains two labels and a buy/sell button.
     */
    private class GoodsRow extends HBox {
        public GoodsRow(TradeGood good, boolean isABuyRow, boolean isDisabled) {
            //add the labels for good's name and amount
                this.getChildren().add(new Label(good.type.name));
                this.getChildren().add(new Label("x" + good.getQuantity()));
            
            Button button;
            //if the row is a Buy Row
            if (isABuyRow) {
                //make a buy button
                button = new BuyButton(good);
            } else {
                //otherwise make a sell button
                button = new SellButton(good);
            }
            if (isDisabled) {
                button.setDisable(true);
            }
            this.getChildren().add(button);
            this.setId("goods-row");
            this.setOnMouseEntered((MouseEvent event) -> {
                chart(good.type.name, good.getPrice());
                updateLabels(good.type.name, good.getPrice());
            });
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

                //Right now, if the price of the good is not updated,
                //then that means that the planet's market does not want
                //that good. If the price isn't updated it remains at 0.
                
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