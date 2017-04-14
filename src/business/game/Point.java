package business.game;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;

public class Point implements Serializable {
    double x = 0.0;
    double y = 0.0;

    public Point() {
        this(0.0, 0.0);
    }

    public Point(double x, double y) {
        this.setLocation(x, y);
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() { return y; }
}
