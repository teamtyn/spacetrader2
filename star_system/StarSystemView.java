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
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import spacetrader.UniverseView;
import spacetrader.Xform;
import spacetrader.Xform.RotateOrder;

/**
 *
 * @author Administrator
 */
public class StarSystemView extends Sphere {
    private StarSystem system;
    private ArrayList<PlanetView> planetViews;
    private Xform systemXform;
    private Xform planetsXform;
    private PointLight light;
    private PhongMaterial material;
    
    public StarSystemView(StarSystem system) {
        super(system.getSize());
        
        Random r = new Random();
        
        this.system = system;
        planetViews = new ArrayList<>(system.getPlanets().length);
        systemXform = new Xform(RotateOrder.ZYX);
        planetsXform = new Xform();
        light = new PointLight();
        light.setLightOn(false);
        material = new PhongMaterial();
        setMaterial(new PhongMaterial(Color.rgb(240, 255, 100)));//system.getColor()));
        
        systemXform.setTranslate(system.getCoordinateX(), system.getCoordinateY());
        systemXform.setRotate(180 * r.nextDouble(), 180 * r.nextDouble(), 0);
        
        for(Planet planet : system.getPlanets()) {
            PlanetView planetView = new PlanetView(planet);
            planetsXform.getChildren().add(planetView.getOrbitXform());
            planetViews.add(planetView);
        }
        systemXform.getChildren().addAll(this, planetsXform, light);
        
        light.getScope().add(planetsXform);
        UniverseView.AMBIENT.getScope().add(planetsXform);
        UniverseView.NO_SHADE.getScope().add(this);
        
        
        light.getScope().add(planetsXform);
        UniverseView.AMBIENT.getScope().add(planetsXform);
        UniverseView.NO_SHADE.getScope().add(this);
    }
    
    public StarSystem getSystem() {
        return system;
    }
    
    public ArrayList<PlanetView> getPlanetViews() {
        return planetViews;
    }
    
    public boolean containsPlanet(PlanetView planet) {
        return planetViews.contains(planet);
    }
    
    public Xform getSystemXform() {
        return systemXform;
    }
    
    public Xform getPlanetsXform() {
        return planetsXform;
    }
    
    public double getX() {
        return systemXform.t.getX();
    }
    
    public double getY() {
        return systemXform.t.getY();
    }
    
    public double getZ() {
        return systemXform.t.getZ();
    }
    
    public double getRx() {
        return systemXform.rx.getAngle();
    }
    
    public double getRy() {
        return systemXform.ry.getAngle();
    }
    
    public double getRz() {
        return systemXform.rz.getAngle();
    }
    
    public void setLightOn(boolean on) {
        light.setLightOn(on);
    }
    
    public void expand() {
        for (PlanetView planet : planetViews) {
            planet.expand();
        }
    }
    
    public void collapse() {
        for (PlanetView planet : planetViews) {
            planet.collapse();
        }
    }
    
//    public void setServiceExecutor() {
//        for (PlanetView planet : planetViews) {
//            planet.setServiceExecutor(exe);
//        }
//    }
    
    public void updateTextures(int width, int height) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (PlanetView planet : planetViews) {
            planet.updateTextures(width, height, es);
        }
        es.shutdown();
    }
    
    public void incrementOrbits() {
        for (PlanetView planet : planetViews) {
            planet.incrementOrbit();
        }
    }
}
