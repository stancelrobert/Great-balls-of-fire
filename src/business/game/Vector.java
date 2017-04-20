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

    public void scale(double factor) {
        this.x *= factor;
        this.y *= factor;
    }

    public double value() {
        return Math.sqrt(x*x+y*y);
    }

    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    public static double getAngle(Vector v1, Vector v2) {
        return Math.atan2(v2.getY(), v2.getX()) - Math.atan2(v1.getY(), v1.getX());
    }

    public static Vector getRotatedVector(Vector v1, double angle) {
        return new Vector(v1.getX()*Math.cos(angle) - v1.getY()*Math.sin(angle),
                v1.getX()*Math.sin(angle) + v1.getY()*Math.cos(angle));
    }

    public static Vector getScaledVector(Vector v1, double factor) {
        return new Vector(v1.getX()*factor, v1.getY()*factor);
    }

    public static Vector addVectors(Vector v1, Vector v2) {
        return new Vector(v1.x+v2.x, v1.y+v2.y);
    }
}
