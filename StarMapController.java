package spacetrader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import spacetrader.market.MarketPlace;
import spacetrader.player.Player;
import spacetrader.star_system.Planet;
import spacetrader.star_system.Planet.TechLevel;
import spacetrader.star_system.StarSystem;
import spacetrader.ui.Point;

/**
 * FXML Controller for the generation of the universe
 * @author David Purcell
 */
public class StarMapController implements ControlledScreen {
    @FXML private Pane systemPane;
    @FXML private Pane shipDataPane;
    @FXML private Label fuelLabel;
    @FXML private Label rangeLabel;
    @FXML private Label hullLabel;
    @FXML private Label dialogueField;
    @FXML private Rectangle playerRectangle;
    @FXML private Text playerText;
    @FXML private Button backButton;
    @FXML private Button viewPlayerCardButton;
    
    private ScreensController parentController;
    private StarSystem[] systems;
    private Player player;
    public static MarketPlace marketSetup;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (StarSystem system: systems) {
            str.append(system).append("\n");
        }
        return str.toString();
    }

    /**
     * Overall view of all systems and other entities in the universe
     */
    public void viewUniverse() {
        systemPane.getChildren().removeAll(systemPane.getChildren());
        
        // Update player ship display
        fuelLabel.setText(Double.toString(player.getShip().getFuel()));
        rangeLabel.setText(Integer.toString(player.getShip().getRange()));
        hullLabel.setText(Integer.toString(player.getShip().getHull()));
        
        // If the player doesn't have a system or planet, just draw them somewhere
        // TODO: Randomize start location or pick a noob spot
        if (player.getSystem() == null && player.getPlanet() == null) {
            player.setCoordinates(new Point(100, 100));
            drawPlayer(player.getX(), player.getY());
        }

        // Adding systems to map
        for (StarSystem system: systems) {

            // Draw a yellow circle to represent star at center of system
            Circle star = new Circle(system.getCoordinateX(), system.getCoordinateY(), 10, system.getColor());
            star.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewSystem(system);
            });         
            systemPane.getChildren().add(star);

            // If player is in this system, draw them
            if (system.hasPlayer && player.getPlanet() == null) {
                player.setCoordinates(new Point(system.getCoordinateX(), system.getCoordinateY()));
                drawPlayer(player.getX() - 50, player.getY() - 25);
            }

            // Loop through planets, adding them at equal intervals around the star
            int numPlanets = system.getPlanets().length;
            int degrees = 0;

            // Adding planets to map
            for (Planet planet : system.getPlanets()) {

                // Draw circle for planet
                double planetX = system.getCoordinateX() + (planet.getOrbitDistance() * Math.cos(degrees * 0.0174532925));
                double planetY = system.getCoordinateY() + (planet.getOrbitDistance() * Math.sin(degrees * 0.0174532925));
                Circle planetCircle = new Circle(planetX, planetY, planet.getSize(), planet.getColor());
                systemPane.getChildren().add(planetCircle);
                if (planet.hasPlayer) {
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
                travelButton.setDisable(getDistanceToSystem(system) > player.getShip().getRange());
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
        systemPane.getChildren().removeAll(systemPane.getChildren());

        // Update player ship display
        fuelLabel.setText(Double.toString(player.getShip().getFuel()));
        rangeLabel.setText(Integer.toString(player.getShip().getRange()));
        hullLabel.setText(Integer.toString(player.getShip().getHull()));
        
        // If the player is in the system, but has not traveled to a planet yet, draw player at arbitrary point
        if (system.hasPlayer && player.getPlanet() == null) {
            player.setCoordinates(new Point(100, 100));
            drawPlayer(player.getX(), player.getY());
        }

        // Go back to the universe view
        backButton = new Button("GO BACK");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewUniverse();
            });
        systemPane.getChildren().add(backButton);
        
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
        for (Planet planet: system.getPlanets()) {

            // Draw circle for planet
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
            
            // Space station for player to buy things for their ship, not clickable from here
            Rectangle spaceStation = new Rectangle(5, 5);
            spaceStation.setFill(Color.GREY);
            spaceStation.setLayoutX(planetX + 25);
            spaceStation.setLayoutY(planetY);
            if (planet.getTechLevel() == TechLevel.HIGHTECH) {
                systemPane.getChildren().add(spaceStation);   
            }

            // If the player is at this planet, draw the player
            if (planet.hasPlayer) {
                drawPlayer(planetX - (planet.getSize() + 3) * Math.sqrt(5), planetY - (planet.getSize() + 3) * Math.sqrt(5));
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
        systemPane.getChildren().removeAll(systemPane.getChildren());
        
        // Update player ship display
        fuelLabel.setText(Double.toString(player.getShip().getFuel()));
        rangeLabel.setText(Integer.toString(player.getShip().getRange()));
        hullLabel.setText(Integer.toString(player.getShip().getHull()));

        // If the player is at this planet, draw them in
        if (planet.hasPlayer) {
            drawPlayer(100, 100);
        }

        // Button to return to system view
        backButton = new Button("GO BACK");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
                viewSystem(system);
            });
        systemPane.getChildren().add(backButton);

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

        // Space station for player to buy things for their ship
        Rectangle spaceStation = new Rectangle(50, 50);
        spaceStation.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            if (planet == player.getPlanet()) {
                parentController.setScreen("SpaceStation");
            }
        });
        spaceStation.setFill(Color.GREY);
        spaceStation.setLayoutX(400);
        spaceStation.setLayoutY(260);
        if (planet.getTechLevel() == TechLevel.HIGHTECH) {
            systemPane.getChildren().add(spaceStation);   
        }

        // Button to go to the market
        Button marketButton = new Button("BUY THINGS");
        marketButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent MouseEvent) -> {
            MarketPlace market = new MarketPlace(planet);
            if (ScreensController.isInitialized("Market")) {
                ((MarketController)ScreensController.getController("Market")).display();
            }
            parentController.setScreen("Market");
        });
        marketButton.setLayoutX(100);
        marketButton.setLayoutY(300);
        marketButton.setDisable(!planet.hasPlayer);
        systemPane.getChildren().add(marketButton);
    }

    /**
     * Method to draw the player on the screen at given coordinates
     * @param x Horizontal component of the player
     * @param y Vertical component of the player
     */
    public void drawPlayer(double x, double y) {
        playerRectangle = new Rectangle(x, y, 5, 5);
        playerRectangle.setFill(player.getShip().type.getColor());
        playerText = new Text(x - 30, y - 10, player.getName());
        playerText.setFont(Font.font("Verdana", 20));
        playerText.setFill(Color.WHITE);
        systemPane.getChildren().add(playerRectangle);
        systemPane.getChildren().add(playerText);
    }

    /**
     * 
     */
    private void showPlayerCard() {
        // TODO
    }

    /**
     * Calculate integer distance from the player's coordinates to the given system
     * @param system The system in question
     * @return  The distance between the player and the system
     */
    public int getDistanceToSystem(StarSystem system) {
        double distance = Math.sqrt(Math.pow(system.getCoordinateX() - player.getSystem().getCoordinateX(), 2) +
                                    Math.pow(system.getCoordinateY() - player.getSystem().getCoordinateY(), 2));
        return (int)distance;
    }

    // TODO: Animations?
    // TODO: Random Encounters (pirates / police)?
    /**
     * Method for the player to travel to a given system
     * TODO: Here or model?
     * @param system The system to be traveled to
     */
    public void travelToSystem(StarSystem system) {
        // Only travel if you can
        if (player.getShip().travelDistance(getDistanceToSystem(system))) {

            // Update player ship display
            fuelLabel.setText(Double.toString(player.getShip().getFuel()));
            rangeLabel.setText(Integer.toString(player.getShip().getRange()));

            // Make sure past planet and system no longer have player
            if (player.getSystem() != null) {
                player.getSystem().hasPlayer = false;
            }
            if (player.getPlanet() != null) {
                player.getPlanet().hasPlayer = false;
            }

            // System you are traveling to has player
            system.hasPlayer = true;
            // Set system to target system and null planet.  Travel to planet later
            player.setSystem(system);
            player.setPlanet(null);

            // Set new player coordinates, only currently used for distance calculations from system to system
            player.setCoordinates(new Point(system.getCoordinateX(), system.getCoordinateY()));
            viewSystem(system);
        }
    }

    // TODO: Animations?
    // TODO: Random Encounters (pirates / police)?
    // TODO: Here or model?
    /**
     * Method for the player to travel to a given planet
     * @param planet The planet to be traveled to
     * @param system The system that planet resides in
     */
    public void travelToPlanet(Planet planet, StarSystem system) {

        // Make sure past planet no longer has player.  System shouldn't change
        if (player.getPlanet() != null) {
            player.getPlanet().hasPlayer = false;
        }

        // Planet you are traveling to has player
        planet.hasPlayer = true;
        player.setPlanet(planet);
        viewPlanet(planet, system);
    }

    @FXML
    private void viewPlayerCardButtonAction(ActionEvent event) {
        System.out.println("Viewing player card now.");
        showPlayerCard();
    }
    
    @Override
    public void lazyInitialize() {
        systems = GameModel.getSystems();
        player = GameModel.getPlayer();
        player.setSystem(systems[0]);
        player.setCoordinates(new Point(systems[0].getCoordinateX(), systems[0].getCoordinateY()));
        systems[0].hasPlayer = true;
        fuelLabel.setText(Double.toString(player.getShip().getFuel()));
        rangeLabel.setText(Integer.toString(player.getShip().getRange()));
        hullLabel.setText(Integer.toString(player.getShip().getHull()));
        viewUniverse();
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}