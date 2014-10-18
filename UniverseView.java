/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import spacetrader.Xform.RotateOrder;
import spacetrader.star_system.Planet;
import spacetrader.star_system.PlanetView;
import spacetrader.star_system.StarSystem;
import spacetrader.star_system.StarSystemView;

/**
 *
 * @author Administrator
 */
public class UniverseView {
    public static final AmbientLight NO_SHADE = new AmbientLight();
    public static final AmbientLight AMBIENT = new AmbientLight(Color.rgb(20, 20, 20));
    
    private final SubScene subScene;
//    private final ExecutorService texGenExe;
    private final ArrayList<StarSystemView> systemViews;
    private final Xform topXform;
    private final Xform baseXform;
    private final PerspectiveCamera camera;
    private final Sphere highlight;

    public UniverseView() {
        Group root = new Group();
        subScene = new SubScene(root, SpaceTrader.SCREEN_WIDTH,
                SpaceTrader.SCREEN_HEIGHT, true,
                SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        
//        texGenExe = new 
        
        systemViews = new ArrayList<>();
        
        topXform = new Xform(RotateOrder.ZYX);
        baseXform = new Xform();
        
        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        subScene.setCamera(camera);
        
        highlight = new Sphere();
        highlight.setMaterial(new PhongMaterial(Color.WHITE));
        highlight.setCullFace(CullFace.FRONT);
        NO_SHADE.getScope().add(highlight);
        highlight.setVisible(false);
        
        root.getChildren().addAll(NO_SHADE, AMBIENT, highlight);
        buildCamera(root);
        buildSystems(root);
        buildSkybox(root);
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.9);
        subScene.setEffect(bloom);
    }
    
    public SubScene getSubScene() {
        return subScene;
    }
    
    public ArrayList<StarSystemView> getSystemViews() {
        return systemViews;
    }
    
    public Xform getTopXform() {
        return topXform;
    }
    
    public Xform getBaseXform() {
        return baseXform;
    }
    
    public PerspectiveCamera getCamera() {
        return camera;
    }
    
    public Sphere getHighlight() {
        return highlight;
    }

    private void buildCamera(Group root) {
        topXform.setTranslate(UniverseMapController.UNIVERSE_WIDTH, UniverseMapController.UNIVERSE_HEIGHT);
        camera.setTranslateZ(-2000);
        
        baseXform.getChildren().add(camera);
        topXform.getChildren().add(baseXform);
        root.getChildren().add(topXform);
    }
    
    public void cameraToSystem(StarSystemView system) {
        topXform.rz.angleProperty().unbind();
        Timeline toSystem = new Timeline(
            new KeyFrame(Duration.seconds(2),
                new KeyValue(baseXform.t.xProperty(), 0),
                new KeyValue(baseXform.t.yProperty(), 0),
                new KeyValue(baseXform.t.zProperty(), 0),
                
                new KeyValue(baseXform.rx.angleProperty(), 0),
                new KeyValue(baseXform.ry.angleProperty(), 0),
                new KeyValue(baseXform.rz.angleProperty(), 0),
                    
                new KeyValue(topXform.t.xProperty(), system.getX()),
                new KeyValue(topXform.t.yProperty(), system.getY()),
                new KeyValue(topXform.t.zProperty(), system.getZ()),
                new KeyValue(camera.translateZProperty(), -200),
                
                new KeyValue(topXform.rx.angleProperty(), system.getRx()),
                new KeyValue(topXform.ry.angleProperty(), system.getRy()),
                new KeyValue(topXform.rz.angleProperty(), system.getRz())
            )
        );
        toSystem.play();
    }
    
    public void cameraToPlanet(StarSystemView system, PlanetView planet) {
        DoubleProperty angleOffset = new SimpleDoubleProperty(topXform.rz.getAngle() - planet.getRz());
        topXform.rz.angleProperty().bind(planet.getOrbitXform().rz.angleProperty().add(angleOffset));
        Timeline toPlanet = new Timeline(
            new KeyFrame(Duration.seconds(2),
                new KeyValue(topXform.rx.angleProperty(), system.getRx()),
                new KeyValue(topXform.ry.angleProperty(), system.getRy()),
                
                new KeyValue(angleOffset, 0),
                new KeyValue(baseXform.t.xProperty(), planet.getAxisXform().t.getX()),
                new KeyValue(baseXform.t.yProperty(), planet.getAxisXform().t.getY()),
                new KeyValue(baseXform.t.zProperty(), planet.getAxisXform().t.getZ()),
                new KeyValue(camera.translateZProperty(), -30),
                    
                new KeyValue(baseXform.rx.angleProperty(), 90),
                new KeyValue(baseXform.ry.angleProperty(), 0),
                new KeyValue(baseXform.rz.angleProperty(), 0)
            )
        );
        toPlanet.play();
    }
    
    private void buildSystems(Group root) {
        Random r = new Random();
        
        StarSystem[] systems = GameModel.getGameModel().getSystems();
        for (StarSystem system : systems) {
            StarSystemView systemView = new StarSystemView(system);
//            systemView.setServiceExecutor(null);
            root.getChildren().add(systemView.getSystemXform());
            systemViews.add(systemView);
        }
    }
    
    private void buildSkybox(Group root) {
        TriangleMesh skyboxMesh = new TriangleMesh();
        skyboxMesh.getPoints().addAll(
            -1f, -1f, -1f,
            -1f, 1f, -1f,
            1f, -1f, -1f,
            1f, 1f, -0.999999f,
            1f, -1f, 1f,
            0.999999f, 1f, 1.000001f,
            -1f, -1f, 1f,
            -1f, 1f, 1f
        );
        skyboxMesh.getTexCoords().addAll(
            0f, 0f,
            0f, 1.0f,
            0.166687f, 0f,
            0.166687f, 1.0f,
            0.333133f, 0.000200f,
            0.333133f, 1.0f,
            0.5f, 0f,
            0.5f, 1.0f,
            0.66666666667f, 0f,
            0.66666666667f, 1.0f,
            0.83333333333f, 0f,
            0.83333333333f, 1.0f,
            1.0f, 0f,
            1.0f, 1.0f
        );
        skyboxMesh.getFaces().addAll(
            0, 0, 2, 2, 1, 1,
            1, 1, 2, 2, 3, 3,
            2, 2, 4, 4, 3, 3,
            3, 3, 4, 4, 5, 5,
            4, 4, 6, 6, 5, 5,
            5, 5, 6, 6, 7, 7,
            6, 6, 0, 8, 7, 7,
            7, 7, 0, 8, 1, 9,
            6, 8, 4, 10, 0, 9,
            0, 9, 4, 10, 2, 11,
            1, 10, 3, 12, 7, 11,
            7, 11, 3, 12, 5, 13
        );
        MeshView skybox = new MeshView(skyboxMesh);
        skybox.setScaleX(5000);
        skybox.setScaleY(5000);
        skybox.setScaleZ(5000);
        
        PhongMaterial skyboxMaterial = new PhongMaterial();
        skyboxMaterial.setDiffuseMap(new Image(getClass().getResource("skybox1.png").toExternalForm()));
        skybox.setMaterial(skyboxMaterial);
        NO_SHADE.getScope().add(skybox);
        
        root.getChildren().add(skybox);
    }
}
