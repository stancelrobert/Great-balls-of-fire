package business;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point {
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    public Point() {
        this.x.set(0);
        this.y.set(0);
    }

    public Point(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public void setLocation(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public double getX() {
        return x.get();
    }

    public double getY() { return y.get(); }

    public DoubleProperty getXProperty() { return x; }

    public DoubleProperty getYProperty() { return y; }
}
