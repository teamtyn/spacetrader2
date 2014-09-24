package spacetrader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import spacetrader.player.Player;
import spacetrader.star_system.Planet;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemNames;

/**
 * FXML Controller for the generation of the universe
 * @author David Purcell
 */
public class StarMapController implements Initializable, ControlledScreen {
    @FXML
    private Pane systemPane;

    //Remove?
    @FXML
    private Button monarchyButton;

    //Remove?
    @FXML
    private Button revolutionButton;

    //Remove?
    @FXML
    private Button toUniverseButton;

    //Remove?
    @FXML
    private Button toSystemButton;

    //Remove?
    @FXML
    private Button generateButton;

    //Remove?
    @FXML
    private Button viewButton;

    private ScreensController parentController;
    private StarSystem[] systems;
    //Temporary player until we figure out how we are passing the actual player around.
    private Player tempPlayer;

    // Just for M4 demo, dumps star system data in console
    //Remove?
    public void demoM4() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (StarSystem system : systems) {
            builder.append(system).append("\n");
        }
        return builder.toString();
    }
    
    //Generate new systems to avoid collisions (Nico give better docs?)
    private void generateSystems() {
        Random random = new Random();
        List<Point2D> positions = new ArrayList<>();
        for (int x = 100; x <= 700; x += 200) {
            for (int y = 100; y <= 500; y += 200) {
                positions.add(new Point2D(x + random.nextInt(100) - 50, y + random.nextInt(100) - 50));
            }
        }
        Collections.shuffle(positions, random);
        
        systems = new StarSystem[random.nextInt(5) + 7];
        for (int i = 0; i < systems.length; i++) {
            systems[i] = new StarSystem(StarSystemNames.getName(), positions.remove(0));
        }
    }

    //Overall view of all systems and other enteties
    public void viewUniverse() {
        //Clear all old stuff.
        systemPane.getChildren().removeAll(systemPane.getChildren());
        
        //If the player doesn't have a system or planet, just draw them somewhere.  
        //Should only matter on startup.
        //Create starting system?
        if(tempPlayer.getPlayerSystem() == null && tempPlayer.getPlayerPlanet() == null){
            drawPlayer(100,100);
        }
        
        //Adding systems to map
        for (StarSystem system : systems) {
            //Draw a yellow circle to represent star at center of system
            Circle star = new Circle(system.getCoordinateX(), system.getCoordinateY(), 10, system.getColor());
            star.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewSystem(system);
            });         
            systemPane.getChildren().add(star);
            
            //If player is in this system, draw them.
            if(system.hasPlayer && tempPlayer.getPlayerPlanet() == null){
                drawPlayer(system.getCoordinateX() - 50, system.getCoordinateY() - 25);
            }
            
            //Loop through planets, adding them at equal intervals around the star
            int numPlanets = system.getPlanets().length;
            int degrees = 0;
            for (Planet planet : system.getPlanets()) {
                //Draw circle for planet
                //TODO: Make better? 3D?
                double planetX = system.getCoordinateX() + (planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
                double planetY = system.getCoordinateY() + (planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
                Circle planetCircle = new Circle(planetX, planetY, planet.getSize(), planet.getColor());
                systemPane.getChildren().add(planetCircle);
                if(planet.hasPlayer){
                    drawPlayer(planetX - 5, planetY - 5);
                }
                degrees += 360 / numPlanets;
            }
            
            //Text displaying the system's name
            Text systemText = new Text(system.getCoordinateX() - 30, system.getCoordinateY() - 30, system.getName());
            systemText.setFont(Font.font("Verdana", 20));
            systemText.setFill(Color.WHITE);
            systemPane.getChildren().add(systemText);
            
            //If the player is not already in the system, allow travel to the system
            if(!system.hasPlayer){
                //Button to travel to system, displays distance to system
                Button travelButton = new Button("Travel to " + system.getName() + "\nDistance " + getDistanceToSystem(system));
                travelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                    //Method that handles traveling to the system
                    travelToSystem(system);
                });
                travelButton.setLayoutX(system.getCoordinateX() - 50);
                travelButton.setLayoutY(system.getCoordinateY() - 100);
                systemPane.getChildren().add(travelButton);   
            }
        }
    }

    //View of a specific system within the universe
    public void viewSystem(StarSystem system) {
        systemPane.getChildren().removeAll(systemPane.getChildren());
        //If the player is in the system, but has not travelled to a planet yet,
        //draw player at arbitrary point
        if(system.hasPlayer && tempPlayer.getPlayerPlanet() == null){
            drawPlayer(100,100);
        }
        //Name of system
        Text systemText = new Text(600, 50, system.getName());
        systemText.setFont(Font.font("Verdana", 40));
        systemText.setFill(Color.WHITE);
        systemPane.getChildren().add(systemText);

        //Draw circle to represent star at middle of system
        Circle star = new Circle(450, 300, 50, system.getColor());
        systemPane.getChildren().add(star);

        //Go back to the 
        Button backButton = new Button("GO BACK");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewUniverse();
            });
        systemPane.getChildren().add(backButton);

        //Loop through planets, adding them at equal intervals around the star
        int numPlanets = system.getPlanets().length;
        int degrees = 0;
        for (Planet planet : system.getPlanets()) {
            //Draw circle for planet
            //TODO: Make better? 3D?
            double planetX = star.getCenterX() + (5 * planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
            double planetY = star.getCenterY() + (5 * planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
            Circle planetCircle = new Circle(planetX, planetY, planet.getSize() * Math.sqrt(5), planet.getColor());
            planetCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewPlanet(planet, system);
            }); 
            systemPane.getChildren().add(planetCircle);

            //Write name of planet aboove planet
            Text planetText = new Text(planetCircle.getCenterX() - planet.getName().length() * 3, planetCircle.getCenterY() - 30, planet.getName());
            planetText.setFont(Font.font ("Verdana", 20));
            planetText.setFill(Color.WHITE);
            systemPane.getChildren().add(planetText);

            //If the player is in this system and not already at this planet, allow travel to this planet
            if(system.hasPlayer && !planet.hasPlayer){
                //Button to travel to this planet from inside system.
                Button travelButton = new Button("Travel to " + planet.getName());
                travelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                    travelToPlanet(planet, system, planetX - 10, planetY - 10);
                });
                travelButton.setLayoutX(planetX - 50);
                travelButton.setLayoutY(planetY - 75);
                systemPane.getChildren().add(travelButton);
            }
            //If the player is at this planet, draw the player
            if(planet.hasPlayer){
                drawPlayer(planetX - (planet.getSize() + 3) *Math.sqrt(5), planetY - (planet.getSize() + 3) *Math.sqrt(5));
            }
            
            degrees += 360 / numPlanets;
        }
    }

    //View for a specific planet
    public void viewPlanet(Planet planet, StarSystem system) {
        systemPane.getChildren().removeAll(systemPane.getChildren());
        //If the player is at this planet, draw them in.
        if(planet.hasPlayer){
            drawPlayer(100,100);
        }
        //Write the name of the planet
        Text planetTitle = new Text(600, 50, planet.getName());
        planetTitle.setFont(Font.font ("Verdana", 40));
        planetTitle.setFill(Color.WHITE);
        systemPane.getChildren().add(planetTitle);

        //Draw a circle to represent the planet
        //TODO: Make better? 3D?
        Circle planetCircle = new Circle(300, 300, 50, planet.getColor());
        systemPane.getChildren().add(planetCircle);

        //Write some more details about the planet such as government and circumstances
        Text planetText = new Text(planetCircle.getCenterX() - 60, planetCircle.getCenterY() + 100, planet.toString());
        planetText.setFont(Font.font("Verdana", 20));
        planetText.setFill(Color.WHITE);
        systemPane.getChildren().add(planetText);

        //Button to return to system view
        Button button = new Button("GO BACK");
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewSystem(system);
            });
        systemPane.getChildren().add(button);

        //Button to instantiate an anarchy government.  
        //TODO: Remove later, replace with better buttons
        Button revolt = new Button("Sponsor Revolution");
        revolt.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.revolt();
            planetText.setText(planet.toString());
        });
        revolt.setLayoutX(100);
        revolt.setLayoutY(225);
        systemPane.getChildren().add(revolt);

        //Button to instantiate a monarchy government.  
        //TODO: Remove later, replace with better buttons
        Button monarch = new Button("Take control");
        monarch.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planetText.setText(planet.toString());
        });
        monarch.setLayoutX(100);
        monarch.setLayoutY(250);
        systemPane.getChildren().add(monarch);
        
        //Button to goto market  
        //TODO: add market?
        Button market = new Button("BUY THINGS");
        market.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            //TODO: Make it not break?
            parentController.setScreen("Market");
        });
        market.setLayoutX(100);
        market.setLayoutY(275);
        systemPane.getChildren().add(market);
    }
    
    //Method to draw the player on the screen at given coordinates
    //TODO: Make better? 3D?
    public void drawPlayer(double x, double y){
        Rectangle playerRectangle = new Rectangle(x, y,5,5);
        playerRectangle.setFill(Color.AQUA);
        Text playerText = new Text(x - 30, y - 10, "Player");
        playerText.setFont(Font.font("Verdana", 20));
        playerText.setFill(Color.WHITE);
        systemPane.getChildren().add(playerRectangle);
        systemPane.getChildren().add(playerText);
    }
    
    //Calculate integer distance from the player's coordinates to the given system.
    public int getDistanceToSystem(StarSystem system){
        double distance = Math.sqrt(Math.pow(system.getCoordinateX() - tempPlayer.getPlayerCoordinateX(),2) +
                                    Math.pow(system.getCoordinateY() - tempPlayer.getPlayerCoordinateY(),2));
        return (int)distance;
    }
    
    //Method for the player to travel to a given system
    //TODO: Animations?
    //TODO: Fuel costs?
    //TODO: Random Encounters (pirates / police)?
    public void travelToSystem(StarSystem system){
        //Make sure past planet and system no longer have player
        if(tempPlayer.getPlayerSystem() != null)
            tempPlayer.getPlayerSystem().hasPlayer = false;
        if(tempPlayer.getPlayerPlanet() != null)
            tempPlayer.getPlayerPlanet().hasPlayer = false;
        
        //System you are travellinig to has player
        system.hasPlayer = true;
        //Set system to target system and null planet.  Travel to planet later.
        tempPlayer.setPlayerSyetem(system);
        tempPlayer.setPlayerPlanet(null);
        //Set new player coordinates, only currently used for distance calculations from system to system
        tempPlayer.setPlayerCoordinates(new Point2D(system.getCoordinateX(),system.getCoordinateY()));
        viewSystem(system);
    }
    
    //Method for the player to travel to a given planet
    //TODO: Animations?
    //TODO: Fuel costs?  Much smaller scale than system to system.
    //TODO: Random Encounters (pirates / police)?
    public void travelToPlanet(Planet planet, StarSystem system, double x, double y){
        //Make sure past planet no longer has player.  System shouldn't change.
        if(tempPlayer.getPlayerPlanet() != null)
            tempPlayer.getPlayerPlanet().hasPlayer = false;
        //Planet you are travellinig to has player
        planet.hasPlayer = true;
        tempPlayer.setPlayerPlanet(planet);
        viewPlanet(planet, system);
    }

    //Remove?
    @FXML
    private void monarchyButtonAction(ActionEvent event) {
        System.out.println("pressed for monarchy");
    }

    //Remove?
    @FXML
    private void revolutionButtonAction(ActionEvent event) {
        System.out.println("pressed for revolution");
    }

    //Remove?
    @FXML
    private void backButtonAction(ActionEvent event) {
        System.out.println("pressed for back");
    }

    //Remove?
    @FXML
    private void viewButtonAction(ActionEvent event) {
        System.out.println("pressed for planet viewing");
    }

    //Initialize the systems and then view the universe
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateSystems();
        //Remove?
        demoM4();
        //Replace with overall player
        tempPlayer = new Player();
        tempPlayer.setPlayerCoordinates(new Point2D(100,100));
        this.viewUniverse();
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}