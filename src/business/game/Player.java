package business.game;


import java.io.Serializable;

public class Player implements Serializable {
    private Point coords = new Point();
    private double rotation = 0.0;
    private double speed = 0.0;
    private String color = "#000000";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Point getCoords() {
        return coords;
    }

    public void setCoords(double x, double y) {
        this.coords.setLocation(x, y);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
