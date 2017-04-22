package business.game;

import java.io.Serializable;

/**
 * Created by Robert on 14.04.2017.
 */
public class Vector extends Point implements Serializable {


    public Vector(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += " " + Math.sqrt(getX()*getX()+getY()*getY());
        return s;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(double delta) {
        double angle = Math.atan2(y, x);
        this.add(delta*Math.cos(angle), delta*Math.sin(angle));
    }

    public void rotate(double delta_angle) {
        setLocation(
                x*Math.cos(delta_angle) - y*Math.sin(delta_angle),
                x*Math.sin(delta_angle) + y*Math.cos(delta_angle)

        );
    }

    public double value() {
        return Math.sqrt(x*x+y*y);
    }
}
