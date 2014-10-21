package spacetrader2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author TYN
 * @version 1.0
 */
public class SpaceTrader extends Application {

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 720;

    @Override
    public void start(Stage stage) throws Exception {
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen("Menu", "Menu.fxml");
        mainContainer.loadScreen("SkillSetup", "SkillSetup.fxml");
        mainContainer.loadScreen("Test", "Test.fxml");
        //mainContainer.loadScreen("StarMap", "StarMap.fxml");
        //mainContainer.loadScreen("UniverseMap", "UniverseMap.fxml");
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