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
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import spacetrader.market.MarketSetup;
import spacetrader.player.Player;
import spacetrader.star_system.Planet;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemNames;

/**
 * FXML Controller for the generation of the universe
 * @author David Purcell
 */
public class StarMapController implements Initializable, ControlledScreen {
    @FXML private Pane systemPane;
    @FXML private Pane shipDataPane;
    @FXML private Label fuelLabel;
    @FXML private Label rangeLabel;
    @FXML private Label hullLabel;
    @FXML private Rectangle playerRectangle;
    @FXML private Text playerText;
    @FXML private Button backButton;
    @FXML private Button viewShipButton;
    @FXML private Button damageShipButton;
    @FXML private Button repairShipButton;
    @FXML private Button viewPlayerCardButton;
    
    private ScreensController parentController;
    private StarSystem[] systems;
    // Temporary player until we figure out how we are passing the actual player around
    private Player tempPlayer;
    private int curLevel;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (StarSystem system : systems) {
            builder.append(system).append("\n");
        }
        return builder.toString();
    }

    /**
     * Generates the star systems to avoid collisions
     * TODO: Factor out, put into MAP / Model / whatever
     */
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

    /**
     * Overall view of all systems and other entities in the universe
     */
    public void viewUniverse() {
        curLevel = 0;
        systemPane.getChildren().removeAll(systemPane.getChildren());
        systemPane.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
            if (keyEvent.getCharacter().equals("w")) {
                movePlayer(0, -1);
            } else if (keyEvent.getCharacter().equals("a")) {
                movePlayer(-1, 0);
            } else if (keyEvent.getCharacter().equals("s")) {
                movePlayer(0, 1);
            } else if (keyEvent.getCharacter().equals("d")) {
                movePlayer(1, 0);
            }
        });

        // If the player doesn't have a system or planet, just draw them somewhere
        // TODO: Randomize start location or pick a noob spot
        if (tempPlayer.getPlayerSystem() == null && tempPlayer.getPlayerPlanet() == null) {
            drawPlayer(100,100);
        }

        // Adding systems to map
        for (StarSystem system : systems) {

            // Draw a yellow circle to represent star at center of system
            Circle star = new Circle(system.getCoordinateX(), system.getCoordinateY(), 10, system.getColor());
            star.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewSystem(system);
            });         
            systemPane.getChildren().add(star);

            // If player is in this system, draw them
            if (system.hasPlayer && tempPlayer.getPlayerPlanet() == null) {
                drawPlayer(system.getCoordinateX() - 50, system.getCoordinateY() - 25);
            }

            // Loop through planets, adding them at equal intervals around the star
            int numPlanets = system.getPlanets().length;
            int degrees = 0;

            // Adding planets to map
            for (Planet planet : system.getPlanets()) {

                // Draw circle for planet
                // TODO: Make better? 3D?
                double planetX = system.getCoordinateX() + (planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
                double planetY = system.getCoordinateY() + (planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
                Circle planetCircle = new Circle(planetX, planetY, planet.getSize(), planet.getColor());
                systemPane.getChildren().add(planetCircle);
                if(planet.hasPlayer){
                    drawPlayer(planetX - 5, planetY - 5);
                }
                degrees += 360 / numPlanets;
            }

            // Text displaying the system's name
            Text systemText = new Text(system.getCoordinateX() - 30, system.getCoordinateY() - 30, system.getName());
            systemText.setFont(Font.font("Verdana", 20));
            systemText.setFill(Color.WHITE);
            systemPane.getChildren().add(systemText);

            // If the player is not already in the system, allow travel to the system
            if (!system.hasPlayer) {

                // Button to travel to system, displays distance to system
                Button travelButton = new Button("Travel to " + system.getName() + "\nDistance " + getDistanceToSystem(system));
                travelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {

                    // Method that handles traveling to the system
                    travelToSystem(system);
                });
                travelButton.setLayoutX(system.getCoordinateX() - 50);
                travelButton.setLayoutY(system.getCoordinateY() - 100);
                systemPane.getChildren().add(travelButton);   
            }
        }
    }

    /**
     * View of a specific system within the universe
     * @param system The system to be viewed
     */
    public void viewSystem(StarSystem system) {
        curLevel = 1;
        systemPane.getChildren().removeAll(systemPane.getChildren());
        systemPane.getChildren().add(backButton);

        // If the player is in the system, but has not traveled to a planet yet, draw player at arbitrary point
        if (system.hasPlayer && tempPlayer.getPlayerPlanet() == null) {
            drawPlayer(100,100);
        }

        // Name of system
        Text systemText = new Text(600, 50, system.getName());
        systemText.setFont(Font.font("Verdana", 40));
        systemText.setFill(Color.WHITE);
        systemPane.getChildren().add(systemText);

        // Draw circle to represent star at middle of system
        Circle star = new Circle(450, 300, 50, system.getColor());
        systemPane.getChildren().add(star);

        // Loop through planets, adding them at equal intervals around the star
        int numPlanets = system.getPlanets().length;
        int degrees = 0;
        for (Planet planet : system.getPlanets()) {

            // Draw circle for planet
            // TODO: Make better? 3D?
            double planetX = star.getCenterX() + (5 * planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
            double planetY = star.getCenterY() + (5 * planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
            Circle planetCircle = new Circle(planetX, planetY, planet.getSize() * Math.sqrt(5), planet.getColor());
            planetCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewPlanet(planet, system);
            }); 
            systemPane.getChildren().add(planetCircle);

            // Write name of planet above the planet's circle
            Text planetText = new Text(planetCircle.getCenterX() - planet.getName().length() * 3, planetCircle.getCenterY() - 30, planet.getName());
            planetText.setFont(Font.font ("Verdana", 20));
            planetText.setFill(Color.WHITE);
            systemPane.getChildren().add(planetText);

            // If the player is in this system and not already at this planet, allow travel to this planet
            if (system.hasPlayer && !planet.hasPlayer) {

                // Button to travel to this planet from inside system
                Button travelButton = new Button("Travel to " + planet.getName());
                travelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                    travelToPlanet(planet, system);
                });
                travelButton.setLayoutX(planetX - 50);
                travelButton.setLayoutY(planetY - 75);
                systemPane.getChildren().add(travelButton);
            }

            // If the player is at this planet, draw the player
            if(planet.hasPlayer){
                drawPlayer(planetX - (planet.getSize() + 3) *Math.sqrt(5), planetY - (planet.getSize() + 3) *Math.sqrt(5));
            }
            degrees += 360 / numPlanets;
        }
    }

    /**
     * View a specific planet
     * @param planet The planet to be viewed
     * @param system The system that the planet resides in
     */
    public void viewPlanet(Planet planet, StarSystem system) {
        curLevel = 2;
        systemPane.getChildren().removeAll(systemPane.getChildren());
        systemPane.getChildren().add(backButton);

        // If the player is at this planet, draw them in
        if (planet.hasPlayer) {
            drawPlayer(100,100);
        }

        // Write the name of the planet
        Text planetTitle = new Text(600, 50, planet.getName());
        planetTitle.setFont(Font.font ("Verdana", 40));
        planetTitle.setFill(Color.WHITE);
        systemPane.getChildren().add(planetTitle);

        // Draw a circle to represent the planet
        // TODO: Make better? 3D?
        Circle planetCircle = new Circle(300, 300, 50, planet.getColor());
        systemPane.getChildren().add(planetCircle);

        // Write some more details about the planet such as government and circumstance
        Text planetText = new Text(planetCircle.getCenterX() - 60, planetCircle.getCenterY() + 100, planet.toString());
        planetText.setFont(Font.font("Verdana", 20));
        planetText.setFill(Color.WHITE);
        systemPane.getChildren().add(planetText);

        // Button to instantiate an anarchy government
        // TODO: Remove later, replace with better buttons
        Button revolt = new Button("Sponsor Revolution");
        revolt.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.revolt();
            planetText.setText(planet.toString());
        });
        revolt.setLayoutX(100);
        revolt.setLayoutY(220);
        systemPane.getChildren().add(revolt);

        // Button to instantiate a monarchy government
        // TODO: Remove later, replace with better buttons
        Button monarch = new Button("Take control");
        monarch.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            planet.becomeMonarchy(tempPlayer.getName());
            planetText.setText(planet.toString());
        });
        monarch.setLayoutX(100);
        monarch.setLayoutY(260);
        systemPane.getChildren().add(monarch);

        // Button to go to the market  
        // TODO: Add market GUI
        Button marketButton = new Button("BUY THINGS");
        marketButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            // TODO: Make it not break?
            

            MarketSetup market = new MarketSetup(planet);
            MarketController.market = market;
            System.out.println(MarketController.market);
            parentController.setScreen("Market");
            System.out.println(market.toString());
        });
        marketButton.setLayoutX(100);
        marketButton.setLayoutY(300);
        systemPane.getChildren().add(marketButton);
    }

    // TODO: Make better? 3D?
    /**
     * Method to draw the player on the screen at given coordinates
     * @param x Horizontal component of the player
     * @param y Vertical component of the player
     */
    public void drawPlayer(double x, double y) {
        playerRectangle = new Rectangle(x, y, 5, 5);
        playerRectangle.setFill(Color.AQUA);
        playerText = new Text(x - 30, y - 10, tempPlayer.getName());
        playerText.setFont(Font.font("Verdana", 20));
        playerText.setFill(Color.WHITE);
        systemPane.getChildren().add(playerRectangle);
        systemPane.getChildren().add(playerText);
    }

    /**
     * Method to move the player dot on the screen a given amount
     * @param x Horizontal change of the player
     * @param y Vertical change of the player
     */
    public void movePlayer(double x, double y) {
        int iterations = 10;
        for (int i = 0; i < iterations; i++) {
            playerRectangle.setX(playerRectangle.getX() + (x / iterations));
            playerRectangle.setY(playerRectangle.getY() + (y / iterations));
            playerText.setX(playerText.getX() + (x / iterations));
            playerText.setY(playerText.getY() + (y / iterations));
        }
    }

    /**
     * 
     */
    private void showPlayerCard() {
        
    }

    /**
     * Calculate integer distance from the player's coordinates to the given system
     * @param system The system in question
     * @return  The distance between the player and the system
     */
    public int getDistanceToSystem(StarSystem system) {
        double distance = Math.sqrt(Math.pow(system.getCoordinateX() - tempPlayer.getPlayerCoordinateX(),2) +
                                    Math.pow(system.getCoordinateY() - tempPlayer.getPlayerCoordinateY(),2));
        return (int)distance;
    }

    // TODO: Animations?
    // TODO: Fuel costs?
    // TODO: Random Encounters (pirates / police)?
    /**
     * Method for the player to travel to a given system
     * TODO: Here or model?
     * @param system The system to be traveled to
     */
    public void travelToSystem(StarSystem system) {

        //Only travel if you can
        if (tempPlayer.getShip().travelDistance(getDistanceToSystem(system))) {

            //Update player ship display
            fuelLabel.setText("" + tempPlayer.getShip().getFuel());
            rangeLabel.setText("" + tempPlayer.getShip().getRange());

            // Make sure past planet and system no longer have player
            if (tempPlayer.getPlayerSystem() != null) {
                tempPlayer.getPlayerSystem().hasPlayer = false;
            }
            if (tempPlayer.getPlayerPlanet() != null) {
                tempPlayer.getPlayerPlanet().hasPlayer = false;
            }

            // System you are traveling to has player
            system.hasPlayer = true;
            // Set system to target system and null planet.  Travel to planet later.
            tempPlayer.setPlayerSystem(system);
            tempPlayer.setPlayerPlanet(null);

            // Set new player coordinates, only currently used for distance calculations from system to system
            tempPlayer.setPlayerCoordinates(new Point2D(system.getCoordinateX(),system.getCoordinateY()));
            viewSystem(system);
        }
    }

    // TODO: Animations?
    // TODO: Fuel costs?  Much smaller scale than system to system
    // TODO: Random Encounters (pirates / police)?
    /**
     * Method for the player to travel to a given planet
     * TODO: Here or model?
     * @param planet The planet to be traveled to
     * @param system The system that planet resides in
     */
    public void travelToPlanet(Planet planet, StarSystem system) {

        // Make sure past planet no longer has player.  System shouldn't change
        if (tempPlayer.getPlayerPlanet() != null) {
            tempPlayer.getPlayerPlanet().hasPlayer = false;
        }

        // Planet you are traveling to has player
        planet.hasPlayer = true;
        tempPlayer.setPlayerPlanet(planet);
        viewPlanet(planet, system);
    }

    @FXML
    private void damageShipButtonAction(ActionEvent event) {
        tempPlayer.getShip().takeDamage(10);
        hullLabel.setText("" + tempPlayer.getShip().getHull());
    }

    @FXML
    private void viewPlayerCardButtonAction(ActionEvent event) {
        System.out.println("Viewing player card now.");
        showPlayerCard();
    }

    @FXML
    private void viewShipButtonAction(ActionEvent event) {
        System.out.println("Viewing ship now.");
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        if (curLevel == 1) {
            viewUniverse();
            systemPane.getChildren().remove(backButton);
        } else if (curLevel == 2) {
            viewSystem(tempPlayer.getPlayerSystem());
        }
    }

    @FXML
    private void repairShipButtonAction(ActionEvent event) {
        tempPlayer.getShip().repairHull(10);
        hullLabel.setText("" + tempPlayer.getShip().getHull());
    }

    // Initialize the systems and then views the universe
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateSystems();

        // Replace with overall player
        tempPlayer = new Player();
        tempPlayer.setPlayerCoordinates(new Point2D(100, 100));
        
        fuelLabel.setText("" + tempPlayer.getShip().getFuel());
        rangeLabel.setText("" + tempPlayer.getShip().getRange());
        hullLabel.setText("" + tempPlayer.getShip().getHull());
        viewUniverse();
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}