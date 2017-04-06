package business.game;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;

public class Point implements Serializable {
    transient private DoubleProperty xProperty = new SimpleDoubleProperty();
    transient private DoubleProperty yProperty = new SimpleDoubleProperty();
    double x = 0.0;
    double y = 0.0;

    public Point() {
        this(0.0, 0.0);
    }

    public Point(double x, double y) {
        this.setLocation(x, y);
    }

    public void setLocation(double x, double y) {
        this.xProperty.set(x);
        this.yProperty.set(y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() { return y; }

    public DoubleProperty getXProperty() { return xProperty; }

    public DoubleProperty getYProperty() { return yProperty; }
}
