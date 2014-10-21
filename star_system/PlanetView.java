/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader2.star_system;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import spacetrader2.Xform;
import spacetrader2.star_system.ColorGradient.ColorScheme;
import spacetrader2.star_system.NoiseGenerator.NoiseMode;

/**
 *
 * @author Administrator
 */
public class PlanetView extends Sphere {
    private final Planet planet;
    private final Xform orbitXform;
    private final Xform axisXform;
    private final NoiseGenerator noise;
    private final GenerateNoiseService generateNoise;
    private final GenerateDiffuseService generateDiffuse;
    private final GenerateNormalService generateNormal;
    
    public PlanetView(Planet planet) {
        super(planet.getSize());
        
        Random r = new Random();
        
        this.planet = planet;
        orbitXform = new Xform();
        axisXform = new Xform();
        
        PhongMaterial material = new PhongMaterial();
        setMaterial(material);
        
        ColorScheme[] colorSchemes = ColorScheme.values();
        ColorGradient colors = new ColorGradient(0.05f, colorSchemes[r.nextInt(colorSchemes.length)]);
        noise = new NoiseGenerator(r.nextLong(), 0.5, 1, 2, 0.5, 15, NoiseMode.SQUARE, colors);
        noise.initNoiseBuffer(100, 50);
        noise.addOctaves();
        material.setDiffuseMap(noise.getDiffuse());
        noise.clearBuffer();
        
        generateNoise = new GenerateNoiseService();
        generateDiffuse = new GenerateDiffuseService();
        generateNormal = new GenerateNormalService();
        initTextures(material);
        
        setRotationAxis(Rotate.Y_AXIS);
        axisXform.setRotate(90 + planet.getAxialTilt(), 0, 360 * r.nextDouble());
        orbitXform.setRz(360 * r.nextDouble());
        
        axisXform.getChildren().add(this);
        orbitXform.getChildren().add(axisXform);
    }
    
    public Planet getPlanet() {
        return planet;
    }
    
    public Xform getOrbitXform() {
        return orbitXform;
    }
    
    public Xform getAxisXform() {
        return axisXform;
    }
    
    public double getX() {
        return getLocalToSceneTransform().getTx();
    }
    
    public double getY() {
        return getLocalToSceneTransform().getTy();
    }
    
    public double getZ() {
        return getLocalToSceneTransform().getTz();
    }
    
    public double getRx() {
        return orbitXform.rx.getAngle();
    }
    
    public double getRy() {
        return orbitXform.ry.getAngle();
    }
    
    public double getRz() {
        return orbitXform.rz.getAngle();
    }
    
    public void expand() {
        Timeline expand = new Timeline(
            new KeyFrame(Duration.seconds(2),
                new KeyValue(axisXform.t.xProperty(), planet.getOrbitDistance()),
                new KeyValue(axisXform.s.xProperty(), 1),
                new KeyValue(axisXform.s.yProperty(), 1),
                new KeyValue(axisXform.s.zProperty(), 1)
            )
        );
        expand.play();
    }
    
    public void collapse() {
        Timeline collapse = new Timeline(
            new KeyFrame(Duration.seconds(2),
                new KeyValue(axisXform.t.xProperty(), 0),
                new KeyValue(axisXform.s.xProperty(), 0),
                new KeyValue(axisXform.s.yProperty(), 0),
                new KeyValue(axisXform.s.zProperty(), 0)
            )
        );
        collapse.play();
    }
    
    private void initTextures(PhongMaterial material) {
        generateDiffuse.setOnSucceeded((WorkerStateEvent t) -> {
            material.setDiffuseMap((Image)t.getSource().getValue());
            generateDiffuse.setExecutor(null);
            if (!generateNormal.isRunning()) {
                noise.clearBuffer();
            }
        });
        
        generateNormal.setOnSucceeded((WorkerStateEvent t) -> {
            material.setBumpMap((Image)t.getSource().getValue());
            generateNormal.setExecutor(null);
            if (!generateDiffuse.isRunning()) {
                noise.clearBuffer();
            }
        });
    }
    
    public void updateTextures(int width, int height, ExecutorService es) {
        generateNoise.cancel();
        generateDiffuse.cancel();
        generateNormal.cancel();
        
        ExecutorService execute;
        if (es == null) {
            execute = Executors.newSingleThreadExecutor((Runnable runnable) -> {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                thread.setDaemon(true);
                return thread;
            });
        } else {
            execute = es;
        }
        
        generateNoise.setExecutor(execute);
        generateDiffuse.setExecutor(execute);
        generateNormal.setExecutor(execute);
        
        generateNoise.width = width;
        generateNoise.height = height;
        generateNormal.intensity = 7;

        generateNoise.restart();
        generateDiffuse.restart();
        generateNormal.restart();
        
        if (es == null) {
            execute.shutdown();
        }
    }
    
    public void incrementOrbit() {
        orbitXform.setRz((orbitXform.rz.getAngle() + planet.getOrbitSpeed()) % 360);
        setRotate(getRotate() + 0.1);
    }
    
    private class GenerateNoiseService extends Service<Void>{
        int width;
        int height;

        @Override
        protected Task<Void> createTask() {
            return new GenerateNoiseTask(width, height);
        }
        
        private class GenerateNoiseTask extends Task<Void> {
            private final int width;
            private final int height;
            
            public GenerateNoiseTask(int w, int h) {
                width = w;
                height = h;
            }

            @Override
            protected Void call() throws Exception {
                noise.initNoiseBuffer(width, height);
                noise.addOctaves();
                return null;
            }
        }
    }
    
    private class GenerateDiffuseService extends Service<Image> {
        @Override
        protected Task<Image> createTask() {
            return new GenerateDiffuseTask();
        }
        
        private class GenerateDiffuseTask extends Task<Image> {
            @Override
            protected Image call() throws Exception {
                return noise.getDiffuse();
            }
        }
    }
    
    private class GenerateNormalService extends Service<Image> {
        double intensity;
        
        @Override
        protected Task<Image> createTask() {
            System.out.println(getExecutor());
            return new GenerateNormalTask(intensity);
        }
        
        private class GenerateNormalTask extends Task<Image> {
            private final double intensity;
            
            public GenerateNormalTask(double i) {
                intensity = i;
            }

            @Override
            protected Image call() throws Exception {
                return noise.getNormal(intensity);
            }
        }
    }
}
