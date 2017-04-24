package business.game;


import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = -4123126425075180848L;
    private Point coords = new Point();
    private double rotation = 0.0;
    private double speed = 0.0;
    private boolean active = true;
    private int points;

    private Vector speedXY = new Vector(0.0, 0.0);
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
        this.rotation = rotation % 360;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Vector getSpeedXY() {
        return speedXY;
    }

    public void setSpeedXY(Vector speedXY) {
        this.speedXY = speedXY;
    }

    @Override
    public String toString() {
        return this.coords + " " + this.rotation + " " + color + this.speedXY.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return color.equals(player.color);
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    public void rotate(double delta_angle) {
        speedXY.rotate(delta_angle);
    }

    public double getSpeedValue() {
        return speedXY.value();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
