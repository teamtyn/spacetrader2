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
    private ArrayList<TradeGood> buyGoods;
    private ArrayList<TradeGood> sellGoods;

    @Override
    public void setScreenParent(ScreensController parentController) {
           this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        market = new MarketSetup(new Planet());
        player = new Player();
        updatePlayerInfo();
        setUpGoodsLists();
        ft = new FadeTransition(Duration.millis(1000), statusLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
    }

    public void updatePlayerInfo() {
        playerInfo.setText("Cargo Space Remaining: " + player.getShip().getExtraSpace() + "\nMoney Remaining: " + 
                player.getMoney() + " bitcoins");
    }

    public void display() {
        setUpGoodsLists();
        updatePlayerInfo();
    }

    public void setUpGoodsLists() {
        buyGoodsVBox.getChildren().clear();
        sellGoodsVBox.getChildren().clear();
        ArrayList<TradeGood> buyGoods = market.getGoods();
        ArrayList<TradeGood> sellGoods = market.getGoods();
        buyList = new GoodsList(buyGoodsVBox, buyGoods, true);
        sellList = new GoodsList(sellGoodsVBox, sellGoods, false);
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        parentController.setScreen("StarMap");
    }

    public static void setMarket(MarketSetup newMarket) {
        market = newMarket;
    }

    public void statusPanelMessage(String status) {
        statusLabel.setText(status);
        ft.play();
    }

    public void updateLabels(String name, int price) {
        sellLabel.setText(name + " will sell for " + price + " bitcoins per unit.");
        buyLabel.setText("You can purchase " + name + " for " + price + " bitcoins per unit.");
    }

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

    public void buy(TradeGood good, int amount) {
        int newMoney = player.getMoney() - good.getPrice() * amount;
        if (newMoney > 0) {
            // TODO: Buy more than one thing at at time
            if (player.getShip().storeTradeGood(good.type.name, 1)) {
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

    public void sell(TradeGood good, int amount) {
        // TODO: Sell more than one thing at at time
        if (player.getShip().removeTradeGood(good.type.name, 1)) {
            player.setMoney(player.getMoney() + good.getPrice() * amount);
            market.increaseQuantity(good, amount);
        }
        display();
    }

    /**
     * 
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
     * 
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
     * 
     */
    private class GoodsRow extends HBox {
        public GoodsRow(TradeGood good, boolean isABuyRow, boolean isDisabled) {
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
                if (isABuyList) {
                    if (good.getQuantity() <= 0) {
                        isDisabled = true;
                    }
                }
                GoodsRow row = new GoodsRow(good, isABuyList, isDisabled);
                this.addChild(row);
            }
        }

        public void addChild(Node node) {
            vBox.getChildren().add(node);
        }
    }
}