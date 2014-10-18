/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class Test extends Application {
    
    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 720;

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root,960,720,true,SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(5000);
        scene.setCamera(camera);
        camera.setTranslateZ(-500);
        
        Sphere center = new Sphere(5);
        System.out.println(center.getDivisions());
        center.setMaterial(new PhongMaterial());
        
        Xform test = new Xform();
        Box box = new Box(10,10,10);
        test.getChildren().add(box);
        test.setRz(-20);
        box.setTranslateX(-20);
        

        root.getChildren().addAll(camera, center, test);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
