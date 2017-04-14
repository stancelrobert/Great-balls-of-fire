package business.game;

import java.io.Serializable;

public class Point implements Serializable {
    private static final long serialVersionUID = -4548763690658025255L;
    private double x = 0.0;
    private double y = 0.0;

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

    @Override
    public String toString() {
        return "{ " + this.x + ", " + this.y + " }";
    }
}
