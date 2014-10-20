package spacetrader.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.scene.paint.Color;

public class SerializableColor implements Serializable {
    private transient Color color;
    
    public SerializableColor(Color color) {
        this.color = color;
    }
    
    public Color toJavaFX() {
        return color;
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        color = Color.color(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(color.getRed());
        out.writeDouble(color.getGreen());
        out.writeDouble(color.getBlue());
        out.writeDouble(color.getOpacity());
    }
}
