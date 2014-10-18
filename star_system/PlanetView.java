/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.star_system;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import spacetrader.Xform;
import spacetrader.star_system.NoiseGenerator.NoiseMode;

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
    private final GenerateIllumService generateIllum;
    
    
    public PlanetView(Planet planet) {
        super(planet.getSize());
        
        Random r = new Random();
        
        this.planet = planet;
        orbitXform = new Xform();
        axisXform = new Xform();
        
        PhongMaterial material = new PhongMaterial();
        setMaterial(material);
        
        ColorGradient colors = new ColorGradient(Color.rgb(0,47,100), Color.WHITE);
        colors.addColor(Color.rgb(13,87,140), 0.11f);
        colors.addColor(Color.rgb(52,217,187), 0.13f);
        colors.addColor(Color.rgb(235,221,162), 0.15f);
        colors.addColor(Color.rgb(185,199,129), 0.17f);
        colors.addColor(Color.rgb(49,99,33), 0.25f);
        colors.addColor(Color.rgb(50,56,48), 0.75f);
        colors.addColor(Color.rgb(136,145,121), 0.85f);
        
        noise = new NoiseGenerator(r.nextLong(), 0.5, 1, 2, 0.5, 10, NoiseMode.SQUARE, colors);
        noise.initNoiseBuffer(100, 50);
        noise.addOctaves();
        material.setDiffuseMap(noise.getDiffuse(1));
        noise.clearBuffer();
        
        generateNoise = new GenerateNoiseService();
        generateDiffuse = new GenerateDiffuseService();
        generateNormal = new GenerateNormalService();
        generateIllum = new GenerateIllumService();
        //generateNoise.includeIllum = true;
        initTextures(material);
        
        setRotationAxis(Rotate.Y_AXIS);
        axisXform.setRotate(planet.getAxialTilt(), 0, 0);//360 * r.nextDouble());
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
//        generateNoise.setOnSucceeded((WorkerStateEvent t) -> {
//            generateDiffuse.restart();
//            if (generateNoise.includeNormal) {
//                generateNormal.restart();
//            }
//            if (generateNoise.includeIllum) {
//                generateIllum.restart();
//            }
//        });
        
        generateDiffuse.setOnSucceeded((WorkerStateEvent t) -> {
            material.setDiffuseMap((Image)t.getSource().getValue());
            if (!generateNormal.isRunning() && !generateIllum.isRunning()) {
                noise.clearBuffer();
            }
        });
        
        generateNormal.setOnSucceeded((WorkerStateEvent t) -> {
            material.setBumpMap((Image)t.getSource().getValue());
            if (!generateDiffuse.isRunning() && !generateIllum.isRunning()) {
                noise.clearBuffer();
            }
        });
        
        generateIllum.setOnSucceeded((WorkerStateEvent t) -> {
            material.setSelfIlluminationMap((Image)t.getSource().getValue());
            if (!generateDiffuse.isRunning() && !generateNormal.isRunning()) {
                noise.clearBuffer();
            }
        });
    }
    
    public void setServiceExecutor(ExecutorService exe) {
        generateNoise.setExecutor(exe);
        generateDiffuse.setExecutor(exe);
        generateNormal.setExecutor(exe);
        generateIllum.setExecutor(exe);
    }
    
    public void updateTextures(int width, int height, ExecutorService es) {
        ExecutorService execute;
        if (es == null) {
            execute = Executors.newSingleThreadExecutor();
        } else {
            execute = es;
        }
        
        generateNoise.cancel();
        generateDiffuse.cancel();
        generateNormal.cancel();
        generateIllum.cancel();
        
        generateNoise.setExecutor(execute);
        generateDiffuse.setExecutor(execute);
        generateNormal.setExecutor(execute);
        generateIllum.setExecutor(execute);
        
        generateNoise.width = width;
        generateNoise.height = height;
        generateNormal.intensity = 7;
        generateIllum.intensity = 0.5;
        generateIllum.threshold = 0.15;
        generateNoise.includeNormal = width * height >= 500000;
        if (generateNoise.includeNormal) {
            generateDiffuse.scaleFactor = 2;
        } else {
            generateDiffuse.scaleFactor = 1;
        }
        generateNoise.restart();
        generateDiffuse.restart();
        if (generateNoise.includeNormal) {
            generateNormal.restart();
        }
        if (generateNoise.includeIllum) {
            generateIllum.restart();
        }
        if (es == null) {
            execute.shutdown();
        }
    }
    
    public void incrementOrbit() {
        orbitXform.setRz((orbitXform.rz.getAngle() + planet.getOrbitSpeed()) % 360);
        setRotate(getRotate() + 0.1);
    }
    
    private class GenerateNoiseService extends Service<Void>{
        boolean includeNormal;
        boolean includeIllum;
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
        int scaleFactor;
        
        @Override
        protected Task<Image> createTask() {
            return new GenerateDiffuseTask(scaleFactor);
        }
        
        private class GenerateDiffuseTask extends Task<Image> {
            private final int scaleFactor;
            
            public GenerateDiffuseTask(int sF) {
                scaleFactor = sF;
            }

            @Override
            protected Image call() throws Exception {
                return noise.getDiffuse(scaleFactor);
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
                System.out.println("Executing");
                return noise.getNormal(intensity);
            }
        }
    }
    
    private class GenerateIllumService extends Service<Image> {
        double intensity;
        double threshold;
        
        @Override
        protected Task<Image> createTask() {
            return new GenerateIllumTask(intensity, threshold);
        }
        
        private class GenerateIllumTask extends Task<Image> {
            private final double intensity;
            private final double threshold;
            
            public GenerateIllumTask(double i, double t) {
                intensity = i;
                threshold = t;
            }

            @Override
            protected Image call() throws Exception {
                return noise.getIllum(intensity, threshold);
            }
        }
    }
}
