/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import spacetrader.star_system.Planet;
import spacetrader.star_system.PlanetView;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemView;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class UniverseMapController extends AnimationTimer implements Initializable, ControlledScreen {

    public static final int UNIVERSE_WIDTH = 900;
    public static final int UNIVERSE_HEIGHT = 600;
    
    //private int scope;
    //private IntegerProperty scope;
    
    private Sphere highlighted;
    private StarSystemView selectedSystem;
    private PlanetView selectedPlanet;
    private ScreensController parentController;
    private UniverseView universeView;
    private SubScene subScene;
    //private PerspectiveCamera camera;
    @FXML private Pane subScenePane;
    //@FXML private ImageView test;
    
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
        universeView = new UniverseView();
        subScene = universeView.getSubScene();
        subScenePane.getChildren().add(subScene);
        //camera = universeView.getCamera();
        //scope = new SimpleIntegerProperty(0);
        //initScope();
        handleMouse();
        start();
    }
    
    /*public void initScope() {
        scope.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() == 0) {
                    ArrayList<KeyValue> universeKeyValues = new ArrayList<>();
                    universeKeyValues.add(
                        new KeyValue(camera.translateZProperty(), -2000));
                    universeKeyValues.add(
                        new KeyValue(universeView.getTopXform().rx.angleProperty(), 0));
                    universeKeyValues.add(
                        new KeyValue(universeView.getTopXform().ry.angleProperty(), 0));
                    if (selectedSystem != null) {
                        for (PlanetView planetView : selectedSystem.getPlanetViews()) {
                            universeKeyValues.add(
                                new KeyValue(planetView.getAxisXform().t.xProperty(), 0));
                            universeKeyValues.add(
                                new KeyValue(planetView.getAxisXform().s.xProperty(), 0));
                            universeKeyValues.add(
                                new KeyValue(planetView.getAxisXform().s.yProperty(), 0));
                            universeKeyValues.add(
                                new KeyValue(planetView.getAxisXform().s.zProperty(), 0));
                        }
                    }
                    Timeline toUniverseView = new Timeline(
                        new KeyFrame(Duration.seconds(2), "Universe View Transition", (ActionEvent e) -> {
                            //Do nothing.
                        }, universeKeyValues
                    ));
                    toUniverseView.play();
                } else if (newValue.intValue() == 1) {
                    universeView.getTopXform().rz.angleProperty().unbind();
                    selectedSystem.getLight().setLightOn(true);
                    ArrayList<KeyValue> systemKeyValues = new ArrayList<>();
                    systemKeyValues.add(
                        new KeyValue(universeView.getTopXform().t.xProperty(),
                            selectedSystem.getSystem().getCoordinateX(), Interpolator.EASE_OUT));
                    systemKeyValues.add(
                        new KeyValue(universeView.getTopXform().t.yProperty(),
                            selectedSystem.getSystem().getCoordinateY(), Interpolator.EASE_OUT));
                    systemKeyValues.add(
                        new KeyValue(camera.translateZProperty(),
                            -200, Interpolator.EASE_OUT));
                    systemKeyValues.add(
                        new KeyValue(universeView.getTopXform().rx.angleProperty(),
                            selectedSystem.getSystemXform().rx.getAngle()));
                    systemKeyValues.add(
                        new KeyValue(universeView.getTopXform().ry.angleProperty(),
                            selectedSystem.getSystemXform().ry.getAngle()));
                    systemKeyValues.add(
                        new KeyValue(universeView.getBaseXform().t.xProperty(), 0));
                    systemKeyValues.add(
                        new KeyValue(universeView.getBaseXform().rx.angleProperty(), 0));
                    systemKeyValues.add(
                        new KeyValue(universeView.getBaseXform().ry.angleProperty(), 0));
                    for (PlanetView planetView : selectedSystem.getPlanetViews()) {
                        systemKeyValues.add(
                            new KeyValue(planetView.getAxisXform().t.xProperty(),
                                planetView.getPlanet().getOrbitDistance()));
                        systemKeyValues.add(
                            new KeyValue(planetView.getAxisXform().s.xProperty(), 1));
                        systemKeyValues.add(
                            new KeyValue(planetView.getAxisXform().s.yProperty(), 1));
                        systemKeyValues.add(
                            new KeyValue(planetView.getAxisXform().s.zProperty(), 1));
                    }
                    Timeline toSystemView = new Timeline(
                        new KeyFrame(Duration.seconds(2), "System View Transition", (ActionEvent e) -> {
                            //Do nothing.
                        }, systemKeyValues
                    ));
                    toSystemView.play();
                } else if (newValue.intValue() == 2) {
                    DoubleProperty angleOffset = new SimpleDoubleProperty(universeView.getTopXform().rz.getAngle() -
                        selectedPlanet.getOrbitXform().rz.getAngle());
                    universeView.getTopXform().rz.angleProperty().bind(selectedPlanet.getOrbitXform().rz.angleProperty().add(angleOffset));
                    ArrayList<KeyValue> planetKeyValues = new ArrayList<>();
                    planetKeyValues.add(
                        new KeyValue(angleOffset, 0));
                    planetKeyValues.add(
                        new KeyValue(universeView.getBaseXform().rx.angleProperty(), -100));
                    planetKeyValues.add(
                        new KeyValue(universeView.getBaseXform().ry.angleProperty(), 0));
                    planetKeyValues.add(
                        new KeyValue(universeView.getBaseXform().t.xProperty(), selectedPlanet.getAxisXform().t.getX()));
                    planetKeyValues.add(    
                        new KeyValue(camera.translateZProperty(), -30));
//                    for (PlanetView planetView : selectedSystem.getPlanetViews()) {
//                        if (planetView != selectedPlanet) {
//                            planetKeyValues.add(
//                                new KeyValue(planetView.getAxisXform().t.xProperty(), 0));
////                            planetKeyValues.add(
////                                new KeyValue(planetView.getAxisXform().s.xProperty(), 0));
////                            planetKeyValues.add(
////                                new KeyValue(planetView.getAxisXform().s.yProperty(), 0));
////                            planetKeyValues.add(
////                                new KeyValue(planetView.getAxisXform().s.zProperty(), 0));
//                        }
//                    }
                    Timeline toPlanetView = new Timeline(
                        new KeyFrame(Duration.seconds(2), "Planet View Transition", (ActionEvent e) -> {
                            //Do nothing.
                        }, planetKeyValues
                    ));
                    toPlanetView.play();
                }
            }
        });
    }*/
    
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
                        }
                    } else if (system == selectedSystem) {
                        setHighlighted(system);
                    }
                } else if (intersect instanceof PlanetView) {
                    PlanetView planet = (PlanetView) intersect;
                    if (selectedSystem != null && selectedPlanet == null) {
                        if (selectedSystem.containsPlanet(planet)) {
                            setHighlighted(planet);
                        }
                    }
                }
            }
        };
        
        EventHandler<MouseEvent> bodyDeselect = (MouseEvent event) -> {
            highlighted = null;
            universeView.getHighlight().setVisible(false);
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
////                if (scope.get() <= 1) {
////                    if (pickResult.getIntersectedNode() instanceof StarSystemView) {
////                       selectedSystem = (StarSystemView) pickResult.getIntersectedNode();
////                       System.out.println(scope.get());
////                       scope.set(1);
////                       System.out.println(scope.get());
////                    }
////                }
////                if (scope.get() == 1) {
////                    if (selectedSystem.getPlanetViews().contains(pickResult.getIntersectedNode())) {
////                        selectedPlanet = (PlanetView) pickResult.getIntersectedNode();
////                        Image img = ((PhongMaterial)selectedPlanet.getMaterial()).getDiffuseMap();
////                        test.setImage(img);
////                        System.out.println(test.getImage());
////                        scope.set(2);
////                    }
////                } else if (scope.get() == 2) {
////                    if (pickResult.getIntersectedNode() == selectedSystem) {
////                        selectedPlanet = null;
////                        scope.set(1);
////                    }
////                }
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
    
    /*public void handleMouse() {
        SubScene subScene = universeView.getSubScene();
        HashMap<Sphere, StarSystem> starMap = universeView.getStarMap();
        HashMap<Sphere, Planet> planetMap = universeView.getPlanetMap();
        PerspectiveCamera camera = universeView.getCamera();
        Sphere highlight = universeView.getHighlight();
        
        EventHandler<MouseEvent> bodySelect;
        bodySelect = (MouseEvent event) -> {
            PickResult pickResult = event.getPickResult();
            if (pickResult != null && pickResult.getIntersectedNode() instanceof Sphere) {
                StarSystem system = starMap.get((Sphere) pickResult.getIntersectedNode());
                if (system != null && system != selectedSystem) {
                    Sphere star = (Sphere) pickResult.getIntersectedNode();
                    universeView.getHighlight().setRadius(1.2 * star.getRadius());
//                    highlight.setTranslateX(starMap.get(star).getCoordinateX());
//                    highlight.setTranslateY(starMap.get(star).getCoordinateY());
//                    highlight.setTranslateZ(0);
                    highlighted = star;
                    highlight.setVisible(true);
                }
                if (scope == 1) {
                    Planet p = planetMap.get((Sphere) pickResult.getIntersectedNode());
                    if (p != null && p != selectedPlanet && Arrays.asList(selectedSystem.getPlanets()).contains(p)) {
                        Sphere planet = (Sphere) pickResult.getIntersectedNode();
                        highlight.setRadius(1.2 * planet.getRadius());
//                        highlight.setTranslateX(planet.getLocalToSceneTransform().getTx());
//                        highlight.setTranslateY(planet.getLocalToSceneTransform().getTy());
//                        highlight.setTranslateZ(planet.getLocalToSceneTransform().getTz());
                        highlighted = planet;
                        highlight.setVisible(true);
                    }
                }
            }
        };
        
        EventHandler<MouseEvent> bodyDeselect = event -> {
                highlighted = null;
                highlight.setVisible(false);
        };
        
        for (Sphere star : starMap.keySet()) {
                star.setOnMouseEntered(bodySelect);
                star.setOnMouseExited(bodyDeselect);
        }
        for (Sphere planet : planetMap.keySet()) {
                planet.setOnMouseEntered(bodySelect);
                planet.setOnMouseExited(bodyDeselect);
        }
        
        subScene.setOnMousePressed((MouseEvent event) -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            
            PickResult pickResult = event.getPickResult();
            if (pickResult != null && pickResult.getIntersectedNode() instanceof Sphere) {
                StarSystem system = starMap.get((Sphere) pickResult.getIntersectedNode());
                if (system != null && system != selectedSystem) {
                    highlight.setVisible(false);
                    selectedSystem = system;
                    universeView.getLightMap().get(selectedSystem).setLightOn(true);
                    scope = 3;

                    Xform cameraXformTop = (Xform) camera.getParent().getParent();
                    Xform cameraXformBottom = (Xform) camera.getParent();
                    Xform planetsXform = (Xform) universeView.getSystemMap().get(selectedSystem).get(0).getParent().getParent();
                    ArrayList<KeyValue> systemViewValues = new ArrayList<>();
                    systemViewValues.add(new KeyValue(cameraXformTop.t.xProperty(),
                        selectedSystem.getCoordinateX(), Interpolator.EASE_OUT));
                    systemViewValues.add(new KeyValue(cameraXformTop.t.yProperty(),
                        selectedSystem.getCoordinateY(), Interpolator.EASE_OUT));
                    systemViewValues.add(new KeyValue(camera.translateZProperty(),
                        -200, Interpolator.EASE_OUT));
                    systemViewValues.add(new KeyValue(cameraXformTop.rx.angleProperty(),
                        planetsXform.rx.getAngle()));
                    systemViewValues.add(new KeyValue(cameraXformTop.ry.angleProperty(),
                        planetsXform.ry.getAngle()));
                    systemViewValues.add(new KeyValue(cameraXformBottom.rx.angleProperty(), 0));
                    systemViewValues.add(new KeyValue(cameraXformBottom.ry.angleProperty(), 0));
                    for (Sphere planet : universeView.getSystemMap().get(selectedSystem)) {
                        systemViewValues.add(new KeyValue(planet.translateXProperty(), universeView.getPlanetMap().get(planet).getOrbitDistance()));
                    }
                    Timeline toSystemView = new Timeline(
                        new KeyFrame(Duration.seconds(2), "systemView", (ActionEvent e) -> {
                            scope = 1;
                        }, systemViewValues
                    ));
                    toSystemView.play();
                }
                if (scope == 1) {
                    Planet p = planetMap.get((Sphere) pickResult.getIntersectedNode());
                    if (p != null && Arrays.asList(selectedSystem.getPlanets()).contains(p)) {
                        highlight.setVisible(false);
                        selectedPlanet = p;
                        scope = 3;
                        
                        Xform cameraXformTop = (Xform) camera.getParent().getParent();
                        Xform cameraXformBottom = (Xform) camera.getParent();
                        Xform planetXform = (Xform) pickResult.getIntersectedNode().getParent();
                        Xform planetsXform = (Xform) planetXform.getParent();
                        DoubleProperty angleOffset = new SimpleDoubleProperty(cameraXformTop.rz.getAngle() - planetXform.rz.getAngle());
                        cameraXformTop.rz.angleProperty().bind(planetXform.rz.angleProperty().add(angleOffset));
                        Timeline toPlanetView = new Timeline(
                            new KeyFrame(Duration.seconds(2), (ActionEvent e) -> {
                                scope = 2;
                            },
                            new KeyValue(angleOffset, 0),
                            //new KeyValue(cameraXformTop.rx.angleProperty(), planetsXform.rx.getAngle()),
                            //new KeyValue(cameraXformTop.ry.angleProperty(), planetsXform.ry.getAngle()),
                            new KeyValue(cameraXformBottom.rx.angleProperty(), -90),
                            new KeyValue(cameraXformBottom.ry.angleProperty(), 0),
                            new KeyValue(cameraXformBottom.translateXProperty(), pickResult.getIntersectedNode().getTranslateX()),
                            new KeyValue(camera.translateZProperty(), -30))
                        );
                        toPlanetView.play();
                    }
                }
            }
        });
        
        subScene.setOnMouseDragged((MouseEvent event) -> {
            double mouseOldX = mousePosX;
            double mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            //cameraVelocityX = (event.getSceneX() - mousePosX) / 5;// - mouseOldX) / 5;
            //cameraVelocityY = (event.getSceneY() - mousePosY) / 5;//mousePosY - mouseOldY) / 5;
            
            if (scope == 0) {
                Xform cameraXform = (Xform) camera.getParent().getParent();
                cameraXform.setTx(cameraXform.t.getX() + camera.getTranslateZ() * (mousePosX - mouseOldX)/1000);
                cameraXform.setTy(cameraXform.t.getY() + camera.getTranslateZ() * (mousePosY - mouseOldY)/1000);
            }
           
            if (scope == 1) {
                Xform cameraXform = (Xform) camera.getParent();
                cameraXform.setRy(cameraXform.ry.getAngle() + (mousePosX - mouseOldX)/2);
                cameraXform.setRx(cameraXform.rx.getAngle() - (mousePosY - mouseOldY)/2);
            }
            
            if (scope == 2) {
                Xform cameraXform = (Xform) camera.getParent();
                cameraXform.setRz(cameraXform.rz.getAngle() - (mousePosX - mouseOldX)/2);
                //cameraXform.setRz(cameraXform.rz.getAngle() - (mousePosY - mouseOldY)/2);
            }
        });
        
        subScene.setOnMouseReleased((MouseEvent event) -> {
            cameraVelocityX = 0;
            cameraVelocityY = 0;
        });
        
        subScene.setOnScroll((ScrollEvent event) -> {
            System.out.println(event.getDeltaY());
            camera.setTranslateZ(camera.getTranslateZ() + event.getDeltaY()/2);
        });
    }*/

    @Override
    public void setScreenParent(ScreensController parentController) {
        this.parentController = parentController;
    }
}
