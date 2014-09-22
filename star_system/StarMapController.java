package spacetrader.star_system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import spacetrader.*;

/**
 * 
 * @author David Purcell
 */
public class StarMapController implements Initializable, ControlledScreen {
    private ScreensController parentController;

    public void start(Stage primaryStage) {
        StarSystem[] systems = {new StarSystem("Sys 1"),new StarSystem("Sys 2"),new StarSystem("Sys 3"),
                                new StarSystem("Sys 4"),new StarSystem("Sys 5"),new StarSystem("Sys 6")};
        Group root = new Group();
        Scene scene = new Scene(root, 1400, 700, Color.BLACK);
        viewUniverse(root, systems);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void viewUniverse(Group root, StarSystem[] systems) {
        for(StarSystem sys : systems){
            Circle star = CircleBuilder.create()
                .centerX(sys.getCoordinateX())
                .centerY(sys.getCoordinateY())
                .radius(10)
                .fill(Color.YELLOW)
                .build();
            //Circle star = Circle(sys.getCoordinateX(), sys.getCoordinateY(), 10, Color.YELLOW);
            star.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                root.getChildren().removeAll(root.getChildren());
                viewSystem(root, sys, systems);
            });         
            root.getChildren().add(star);

            Text systemText = new Text(sys.getCoordinateX() - 30, sys.getCoordinateY() - 30, sys.getName());
            systemText.setFont(Font.font ("Verdana", 20));
            systemText.setFill(Color.WHITE);
            root.getChildren().add(systemText);

            int planets = sys.getPlanets().length;
            int degrees = 0;
            for(Planet planet : sys.getPlanets()){
                Circle planetCircle = CircleBuilder.create()
                .centerX(sys.getCoordinateX() + (planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925)))
                .centerY(sys.getCoordinateY() + (planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925)))
                .radius(planet.getSize())
                .fill(planet.getColor())
                .build();
                root.getChildren().add(planetCircle);
                degrees += 360 / planets;
            }
        }
    }

    public void viewSystem(Group root, StarSystem system, StarSystem[] systems) {
        Text systemText = new Text(600, 50, system.getName());
        systemText.setFont(Font.font("Verdana", 40));
        systemText.setFill(Color.WHITE);
        root.getChildren().add(systemText);
        Circle star = CircleBuilder.create()
            .centerX(600)
            .centerY(300)
            .radius(50)
            .fill(Color.YELLOW)
            .build();
        root.getChildren().add(star);
        Button button = new Button("GO BACK");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                root.getChildren().removeAll(root.getChildren());
                viewUniverse(root, systems);
            });
        root.getChildren().add(button);
        int planets = system.getPlanets().length;
        int degrees = 0;
        for(Planet planet : system.getPlanets()){
            Circle planetCircle = CircleBuilder.create()
            .centerX(star.getCenterX() + (5 * planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925)))
            .centerY(star.getCenterY() + (5 * planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925)))
            .radius(planet.getSize() * Math.sqrt(5))
            .fill(planet.getColor())
            .build();
            planetCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                root.getChildren().removeAll(root.getChildren());
                viewPlanet(root, planet, system, systems);
            }); 
            root.getChildren().add(planetCircle);
            Text planetText = new Text(planetCircle.getCenterX() - planet.getName().length() * 3, planetCircle.getCenterY() - 30, planet.getName());
            planetText.setFont(Font.font ("Verdana", 20));
            planetText.setFill(Color.WHITE);
            root.getChildren().add(planetText);
            degrees += 360 / planets;
        }
    }

    public void viewPlanet(Group root, Planet planet, StarSystem system, StarSystem[] systems) {
        Text planetTitle = new Text(600, 50, planet.getName());
        planetTitle.setFont(Font.font ("Verdana", 40));
        planetTitle.setFill(Color.WHITE);
        root.getChildren().add(planetTitle);

        Circle planetCircle = CircleBuilder.create()
            .centerX(600)
            .centerY(300)
            .radius(50)
            .fill(planet.getColor())
            .build();
        root.getChildren().add(planetCircle);

        Text planetText = new Text(planetCircle.getCenterX() - 60, planetCircle.getCenterY() + 100, planet.toString());
        planetText.setFont(Font.font ("Verdana", 20));
        planetText.setFill(Color.WHITE);
        root.getChildren().add(planetText);

        Button button = new Button("GO BACK");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                root.getChildren().removeAll(root.getChildren());
                viewSystem(root, system, systems);
            });
        root.getChildren().add(button);

        Button revolt = new Button("Sponsor Revolution");
        revolt.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.revolt();
            planetText.setText(planet.toString());
        });
        revolt.setLayoutX(250);
        revolt.setLayoutY(225);
        root.getChildren().add(revolt);

        Button monarch = new Button("Take control");
        monarch.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.becomeMonarchy(SkillSetupController.player.getName());
            planetText.setText(planet.toString());
        });
        monarch.setLayoutX(250);
        monarch.setLayoutY(250);
        root.getChildren().add(monarch);
    }

    @FXML
    private void monarchyButtonAction(ActionEvent event) {
        System.out.println("pressed for monarchy");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}