package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import spacetrader.star_system.Planet;
import spacetrader.star_system.StarSystem;

/**
 * FXML Controller for the generation of the universe
 * @author David Purcell
 */
public class StarMapController implements Initializable, ControlledScreen {
    @FXML
    private Pane systemPane;

    @FXML
    private Button monarchyButton;

    @FXML
    private Button revolutionButton;

    @FXML
    private Button toUniverseButton;

    @FXML
    private Button toSystemButton;

    @FXML
    private Button generateButton;

    @FXML
    private Button viewButton;

    private ScreensController parentController;
    private StarSystem[] systems;

    // Just for M4 demo, dumps star system data in console
    public void demoM4() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String str = "";
        for (StarSystem system: systems) {
            str += system + "\n";
        }
        return str;
    }

    public void viewUniverse(StarSystem[] systems) {
        for (StarSystem system : systems) {
            Circle star = new Circle(system.getCoordinateX(), system.getCoordinateY(), 10, Color.YELLOW);
            star.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                systemPane.getChildren().removeAll(systemPane.getChildren());
                viewSystem(system, systems);
            });         
            systemPane.getChildren().add(star);

            Text systemText = new Text(system.getCoordinateX() - 30, system.getCoordinateY() - 30, system.getName());
            systemText.setFont(Font.font ("Verdana", 20));
            systemText.setFill(Color.WHITE);
            systemPane.getChildren().add(systemText);

            int numPlanets = system.getPlanets().length;
            int degrees = 0;
            for (Planet planet : system.getPlanets()) {
                double planetX = system.getCoordinateX() + (planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
                double planetY = system.getCoordinateY() + (planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
                Circle planetCircle = new Circle(planetX, planetY, planet.getSize(), planet.getColor());
                systemPane.getChildren().add(planetCircle);
                degrees += 360 / numPlanets;
            }
        }
    }

    public void viewSystem(StarSystem system, StarSystem[] systems) {
        Text systemText = new Text(600, 50, system.getName());
        systemText.setFont(Font.font("Verdana", 40));
        systemText.setFill(Color.WHITE);
        systemPane.getChildren().add(systemText);

        Circle star = new Circle(600, 300, 50, Color.YELLOW);
        systemPane.getChildren().add(star);

        Button button = new Button("GO BACK");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                systemPane.getChildren().removeAll(systemPane.getChildren());
                viewUniverse(systems);
            });
        systemPane.getChildren().add(button);

        int numPlanets = system.getPlanets().length;
        int degrees = 0;
        for (Planet planet : system.getPlanets()) {
            double planetX = star.getCenterX() + (5 * planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
            double planetY = star.getCenterY() + (5 * planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
            Circle planetCircle = new Circle(planetX, planetY, planet.getSize() * Math.sqrt(5), planet.getColor());
            planetCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                systemPane.getChildren().removeAll(systemPane.getChildren());
                viewPlanet(planet, system, systems);
            }); 
            systemPane.getChildren().add(planetCircle);

            Text planetText = new Text(planetCircle.getCenterX() - planet.getName().length() * 3, planetCircle.getCenterY() - 30, planet.getName());
            planetText.setFont(Font.font ("Verdana", 20));
            planetText.setFill(Color.WHITE);
            systemPane.getChildren().add(planetText);

            degrees += 360 / numPlanets;
        }
    }

    public void viewPlanet(Planet planet, StarSystem system, StarSystem[] systems) {
        Text planetTitle = new Text(600, 50, planet.getName());
        planetTitle.setFont(Font.font ("Verdana", 40));
        planetTitle.setFill(Color.WHITE);
        systemPane.getChildren().add(planetTitle);

        Circle planetCircle = new Circle(600, 300, 50, planet.getColor());
        systemPane.getChildren().add(planetCircle);

        Text planetText = new Text(planetCircle.getCenterX() - 60, planetCircle.getCenterY() + 100, planet.toString());
        planetText.setFont(Font.font ("Verdana", 20));
        planetText.setFill(Color.WHITE);
        systemPane.getChildren().add(planetText);

        Button button = new Button("GO BACK");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                systemPane.getChildren().removeAll(systemPane.getChildren());
                viewSystem(system, systems);
            });
        systemPane.getChildren().add(button);

        Button revolt = new Button("Sponsor Revolution");
        revolt.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.revolt();
            planetText.setText(planet.toString());
        });
        revolt.setLayoutX(250);
        revolt.setLayoutY(225);
        systemPane.getChildren().add(revolt);

        Button monarch = new Button("Take control");
        monarch.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.becomeMonarchy(SkillSetupController.player.getName());
            planetText.setText(planet.toString());
        });
        monarch.setLayoutX(250);
        monarch.setLayoutY(250);
        systemPane.getChildren().add(monarch);
    }

    @FXML
    private void monarchyButtonAction(ActionEvent event) {
        System.out.println("pressed for monarchy");
    }

    @FXML
    private void revolutionButtonAction(ActionEvent event) {
        System.out.println("pressed for revolution");
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        System.out.println("pressed for back");
    }

    @FXML
    private void viewButtonAction(ActionEvent event) {
        System.out.println("pressed for planet viewing");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systems = new StarSystem[]{new StarSystem("test1"), new StarSystem("test2")};
        demoM4();
        this.viewUniverse(systems);
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}