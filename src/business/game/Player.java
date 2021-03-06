package business.game;


import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = -4123126425075180848L;
    private Point coords = new Point();
    private double rotation = 0.0;
    private boolean active = true;
    private int points = 0;
    private boolean bot = false;

    private Vector speedXY = new Vector(0.0, 0.0);
    private String color = "#000000";

    public void setAll(Player player) {
        this.coords.setLocation(player.getCoords().getX(), player.getCoords().getY());
        this.rotation = player.rotation;
        this.speedXY = player.speedXY;
        this.active = player.active;
        this.points = player.points;
        this.bot = player.bot;
    }

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

    public Vector getSpeedXY() {
        return speedXY;
    }

    public void setSpeedXY(Vector speedXY) {
        this.speedXY = speedXY;
    }

    @Override
    public String toString() {
        return this.coords + " " + this.rotation + " " + color + " " + this.speedXY.value();
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }
}
