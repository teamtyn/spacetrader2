package spacetrader;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spacetrader.market.MarketSetup;
import spacetrader.star_system.Planet;

/**
 * @author TYN
 * @version 1.0
 */
public class SpaceTrader extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen("Menu", "Menu.fxml");
        mainContainer.loadScreen("SkillSetup", "SkillSetup.fxml");
        mainContainer.loadScreen("StarMap", "StarMap.fxml");
        mainContainer.loadScreen("Market", "Market.fxml");
        mainContainer.loadScreen("SpaceStation", "SpaceStation.fxml");
        mainContainer.setScreen("Menu");

        Group root = new Group();
        root.getChildren().addAll(mainContainer);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}