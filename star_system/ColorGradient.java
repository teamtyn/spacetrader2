/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.star_system;

import static java.util.Arrays.asList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class ColorGradient {
    public enum ColorScheme {EARTH, LAVA, ICE, DESERT, ALIEN, ROCKY};
    
    private TreeMap<Float, Color> colors;
    private float seaLevel;
    
    public ColorGradient(float sL, ColorScheme scheme) {
        colors = new TreeMap<>();
        seaLevel = sL;
        
        switch (scheme) {
            case EARTH:
                colors.put(-1f, Color.rgb(0, 47, 100));
                colors.put(-0.5f, Color.rgb(13, 87, 140));
                colors.put(-0.05f, Color.rgb(153, 204, 190));
                colors.put(0f, Color.rgb(235, 221, 162));
                colors.put(0.05f, Color.rgb(185, 199, 129));
                colors.put(0.1f, Color.rgb(49, 99, 33));
                colors.put(0.5f, Color.rgb(50, 56, 48));
                colors.put(0.8f, Color.rgb(136, 145, 121));
                colors.put(1f, Color.rgb(255, 255, 255));
                break;
            case LAVA:
                colors.put(-1f, Color.rgb(255, 255, 100));
                colors.put(-0.9f, Color.rgb(255, 200, 0));
                colors.put(-0.5f, Color.rgb(255, 100, 0));
                colors.put(-0.05f, Color.rgb(159, 57, 24));
                colors.put(0f, Color.rgb(45, 45, 45));
                colors.put(1f, Color.rgb(160, 160, 160));
                break;
            case ICE:
                colors.put(-1f, Color.rgb(36, 44, 64));
                colors.put(-0.05f, Color.rgb(91, 151, 212));
                colors.put(0f, Color.rgb(151, 182, 201));
                colors.put(0.5f, Color.rgb(128, 128, 128));
                colors.put(0.7f, Color.rgb(171, 199, 217));
                colors.put(1f, Color.rgb(255, 255, 255));
                break;
            case DESERT:
                colors.put(-1f, Color.rgb(41, 82, 82));
                colors.put(-0.05f, Color.rgb(107, 144, 144));
                colors.put(0f, Color.rgb(60, 128, 0));
                colors.put(0.01f, Color.rgb(217, 212, 143));
                colors.put(0.3f, Color.rgb(135, 120, 70));
                colors.put(1f, Color.rgb(105, 101, 90));
                break;
            case ALIEN:
                colors.put(-1f, Color.rgb(70, 42, 156));
                colors.put(-0.5f, Color.rgb(81, 48, 179));
                colors.put(-0.08f, Color.rgb(227, 89, 222));
                colors.put(0f, Color.rgb(150, 52, 47));
                colors.put(0.1f, Color.rgb(94, 20, 20));
                colors.put(0.5f, Color.rgb(28, 12, 12));
                colors.put(1f, Color.rgb(253, 245, 255));
                break;
            case ROCKY:
                Random r = new Random();
                double hue = 360 * r.nextDouble();
                colors.put(-1f, Color.hsb(hue, 0.2, 0.2));
                colors.put(1f, Color.hsb(hue, 0.2, 0.7));
                break;
        }
    }
    
    public void addColor(float value, Color color) {
        colors.put(value, color);
    }
    
    public Color getColor(float value) {
        if (seaLevel > 0 && seaLevel < 1) {
            if (value >= seaLevel) {
                value = (value - seaLevel) / (1 - seaLevel);
            } else {
                value = (value - seaLevel) / seaLevel;
            }
        }
        if (colors.containsKey(value)) {
            return colors.get(value);
        } else {
            float low = colors.floorKey(value);
            float high = colors.ceilingKey(value);
            double scalar = (value - low) / (high - low);
            return colors.get(low).interpolate(colors.get(high), scalar);
        }
    }
}
