/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import spacetrader.player.Player;
import spacetrader.star_system.PlanetView;
import spacetrader.star_system.StarSystemView;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class UniverseMapController extends AnimationTimer implements Initializable, ControlledScreen {

    public static final int UNIVERSE_WIDTH = 900;
    public static final int UNIVERSE_HEIGHT = 600;
    
    private Sphere highlighted;
    private StarSystemView selectedSystem;
    private PlanetView selectedPlanet;
    private ScreensController parentController;
    private UniverseView universeView;
    private SubScene subScene;
    
    @FXML private Pane subScenePane;
    @FXML private Pane infoPane;
    @FXML private Label systemField;
    @FXML private Label distanceField;
    @FXML private Label fuelCostField;
    @FXML private Label planetField;
    @FXML private Label governmentField;
    @FXML private Label techLevelField;
    @FXML private Label environmentField;
    @FXML private Button travelButton;
    
    double mousePosX;
    double mousePosY;
    double cameraVelocityX;
    double cameraVelocityY;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        infoPane.setTranslateY(193);
        travelButton.translateYProperty().bind(infoPane.translateYProperty());
        
        universeView = new UniverseView();
        subScene = universeView.getSubScene();
        subScenePane.getChildren().add(subScene);
        //flashCamera();
        handleMouse();
        start();
    }
    
    @Override
    public void handle(long now) {
        if (selectedSystem != null) {
            selectedSystem.incrementOrbits();
        }
        if (highlighted != null) {
            Sphere highlight = universeView.getHighlight();
            highlight.setTranslateX(highlighted.getLocalToSceneTransform().getTx());
            highlight.setTranslateY(highlighted.getLocalToSceneTransform().getTy());
            highlight.setTranslateZ(highlighted.getLocalToSceneTransform().getTz());
        }
    }
    
    private void setHighlighted(Sphere h) {
        highlighted = h;
        universeView.getHighlight().setRadius(1.2 * h.getRadius());
        universeView.getHighlight().setVisible(true);
    }
    
    public void handleMouse() {
        PerspectiveCamera camera = universeView.getCamera();
        
        EventHandler<MouseEvent> bodySelect;
        bodySelect = (MouseEvent event) -> {
            PickResult pickResult = event.getPickResult();
            if (pickResult != null) {
                Node intersect = pickResult.getIntersectedNode();
                if (intersect instanceof StarSystemView) {
                    StarSystemView system = (StarSystemView) intersect;
                    if (selectedPlanet == null) {
                        if (system != selectedSystem) {
                            setHighlighted(system);
                            showSystemInfo(system);
                        }
                    } else if (system == selectedSystem) {
                        setHighlighted(system);
                        showSystemInfo(system);
                    }
                } else if (intersect instanceof PlanetView) {
                    PlanetView planet = (PlanetView) intersect;
                    if (selectedSystem != null && selectedPlanet == null) {
                        if (selectedSystem.containsPlanet(planet)) {
                            setHighlighted(planet);
                            showPlanetInfo(planet);
                        }
                    }
                }
            }
        };
        
        EventHandler<MouseEvent> bodyDeselect = (MouseEvent event) -> {
            highlighted = null;
            universeView.getHighlight().setVisible(false);
            if (selectedPlanet == null) {
                if (selectedSystem == null) {
                    hideInfo();
                } else {
                    showSystemInfo(selectedSystem);
                }
            } else {
                showPlanetInfo(selectedPlanet);
            }
        };
        
        for (StarSystemView systemView : universeView.getSystemViews()) {
            systemView.setOnMouseEntered(bodySelect);
            systemView.setOnMouseExited(bodyDeselect);
            for (PlanetView planetView : systemView.getPlanetViews()) {
                planetView.setOnMouseEntered(bodySelect);
                planetView.setOnMouseExited(bodyDeselect);
            }
        }
        
        subScene.setOnMousePressed((MouseEvent event) -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            
            PickResult pickResult = event.getPickResult();
            if (pickResult != null) {
                Node intersect = pickResult.getIntersectedNode();
                if (intersect instanceof StarSystemView) {
                    StarSystemView system = (StarSystemView) intersect;
                    if (selectedPlanet == null) {
                        if (system != selectedSystem) {
                            if (selectedSystem != null) {
                                selectedSystem.collapse();
                                selectedSystem.updateTextures(100, 50);
                            }
                            selectedSystem = system;
                            selectedSystem.expand();
                            selectedSystem.updateTextures(1000, 500);
                            selectedSystem.setLightOn(true);
                            universeView.cameraToSystem(selectedSystem);
                        }
                    } else if (system == selectedSystem) {
                        selectedPlanet.updateTextures(1000, 500, null);
                        selectedPlanet = null;
                        universeView.cameraToSystem(selectedSystem);
                    }
                } else if (intersect instanceof PlanetView) {
                    PlanetView planet = (PlanetView) intersect;
                    if (selectedSystem != null && selectedPlanet == null) {
                        if (selectedSystem.containsPlanet(planet)) {
                            selectedPlanet = planet;
                            selectedPlanet.updateTextures(2000, 1000, null);
                            universeView.cameraToPlanet(selectedSystem, selectedPlanet);
                        }
                    }
                }
            }
        });
        
        subScene.setOnMouseDragged((MouseEvent event) -> {
            double mouseOldX = mousePosX;
            double mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            
            if (selectedPlanet != null) {
                Xform cameraXform = (Xform) universeView.getBaseXform();
                cameraXform.setRz(cameraXform.rz.getAngle() + (mousePosX - mouseOldX)/2);
                cameraXform.setRx(cameraXform.rx.getAngle() - (mousePosY - mouseOldY)/2);
            } else if (selectedSystem != null) {
                Xform cameraXform = (Xform) universeView.getBaseXform();
                cameraXform.setRy(cameraXform.ry.getAngle() + (mousePosX - mouseOldX)/2);
                cameraXform.setRx(cameraXform.rx.getAngle() - (mousePosY - mouseOldY)/2);
            } else {
                Xform cameraXform = (Xform) universeView.getTopXform();
                cameraXform.setTx(cameraXform.t.getX() + camera.getTranslateZ() * (mousePosX - mouseOldX)/1000);
                cameraXform.setTy(cameraXform.t.getY() + camera.getTranslateZ() * (mousePosY - mouseOldY)/1000);
            }
        });
        
        subScene.setOnScroll((ScrollEvent event) -> {
            camera.setTranslateZ(camera.getTranslateZ() + event.getDeltaY()/40);
        });
    }
    
    public void flashCamera() {
        Timeline hideInfo = new Timeline(
            new KeyFrame(Duration.seconds(0.1),
                new KeyValue(universeView.getTopXform().rx.angleProperty(), 45)
            )
        );
        hideInfo.play();
    }
    
    public void hideInfo() {
        Timeline hideInfo = new Timeline(
            new KeyFrame(Duration.seconds(0.1),
                new KeyValue(infoPane.translateYProperty(), 193)
            )
        );
        hideInfo.play();
    }
    
    public void showSystemInfo(StarSystemView system) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        
        travelButton.setDisable(true);
        
        Player player = GameModel.getGameModel().getPlayer();
        double distance = player.getSystem().getSystemDistance(system.getSystem());
        double fuelCost = distance / player.getShip().getFuelEfficiency();
        
        systemField.setText(system.getSystem().getName());
        distanceField.setText(nf.format(distance) + " pc");
        fuelCostField.setText(nf.format(fuelCost) + " gallons");
        Timeline systemInfo = new Timeline(
            new KeyFrame(Duration.seconds(0.1),
                new KeyValue(infoPane.translateYProperty(), 128)
            )
        );
        systemInfo.play();
    }
    
    public void showPlanetInfo(PlanetView planet) {
        Player player = GameModel.getGameModel().getPlayer();
        
        if (planet == selectedPlanet) {
            travelButton.setDisable(false);
        }
        if (player.getPlanet() == planet.getPlanet()) {
            travelButton.setText("To Surface");
        } else {
            travelButton.setText("Travel");
        }
        
        if (player.knowsPlanet(planet.getPlanet())) {
            planetField.setText(planet.getPlanet().getName());
            governmentField.setText("" + planet.getPlanet().getGovernment());
            techLevelField.setText("" + planet.getPlanet().getTechLevel());
            environmentField.setText("" + planet.getPlanet().getResourceLevel());
        } else {
            planetField.setText("Unknown");
            governmentField.setText("Unknown");
            techLevelField.setText("Unknown");
            environmentField.setText("Unknown");
        }    
        
        Timeline planetInfo = new Timeline(
            new KeyFrame(Duration.seconds(0.1),
                new KeyValue(infoPane.translateYProperty(), 0)
            )
        );
        planetInfo.play();
    }

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}
