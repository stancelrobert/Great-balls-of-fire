package business.game;

import business.util.FPSManager;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Robert on 07.04.2017.
 */
public class PlayerDisplayTask extends Task<Void> {
    public static final double RADIUS = 25.0;
    private static final double FPS = 57.0;
    private final FPSManager fpsManager = new FPSManager(FPS);

    private Player player;
    private Circle circle = new Circle(RADIUS, Color.web("#FF0000"));
    private Line directionLine = new Line();
    private DoubleProperty pX = new SimpleDoubleProperty(0.0);
    private DoubleProperty pY = new SimpleDoubleProperty(0.0);
    private DoubleProperty eX = new SimpleDoubleProperty(0.0);
    private DoubleProperty eY = new SimpleDoubleProperty(0.0);

    public Circle getCircle() {
        return circle;
    }

    public Line getDirectionLine() {
        return directionLine;
    }

    public PlayerDisplayTask(Player player) {
        this.player = player;
        circle.centerXProperty().bind(pX);
        circle.centerYProperty().bind(pY);
        circle.setFill(Color.web(player.getColor()));

        directionLine.startXProperty().bind(pX);
        directionLine.startYProperty().bind(pY);

        directionLine.endXProperty().bind(eX);
        directionLine.endYProperty().bind(eY);
    }

    @Override
    protected Void call() {
        try {
            //fpsManager.start();
            while (!Thread.interrupted()) {

                pX.set(player.getCoords().getX()+293);
                pY.set(player.getCoords().getY()+310);

                eX.set(player.getCoords().getX()+293+circle.getRadius()*Math.cos(player.getRotation()*Math.PI/180.0));
                eY.set(player.getCoords().getY()+310+circle.getRadius()*Math.sin(player.getRotation()*Math.PI/180.0));


                //fpsManager.waitForNextFrame();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setpX(double pX) {
        this.pX.set(pX);
    }

    public void setpY(double pY) {
        this.pY.set(pY);
    }

    public void seteX(double eX) {
        this.eX.set(eX);
    }

    public void seteY(double eY) {
        this.eY.set(eY);
    }
}
