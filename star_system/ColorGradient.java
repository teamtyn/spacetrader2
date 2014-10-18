/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.star_system;

import java.util.TreeMap;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class ColorGradient {
    private TreeMap<Float, Color> colors;
    
    public ColorGradient(Color min, Color max) {
        colors = new TreeMap<>();
        colors.put(0.0f, min);
        colors.put(1.0f, max);
    }
    
    public void addColor(Color color, float value) {
        colors.put(value, color);
    }
    
    public Color getColor(float value) {
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
