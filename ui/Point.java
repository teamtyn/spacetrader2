package spacetrader.ui;

import java.io.Serializable;

public class Point implements Serializable {
    private final double x, y, z;
    
    public Point(double x, double y) {
        this(x, y, 0);
    }
    
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
