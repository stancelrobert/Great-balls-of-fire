package business;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player extends Task<Point> {
    private static final double RADIUS = 25.0;
    private static final double ACCELERATION = 400.0;
    private static final double ROTATION_SPEED = 180.0;
    private static final double PASSIVE_ACCELERATION = 200.0;
    private static final double FPS = 57.0;

    private Circle circle = new Circle();

    private Line directionLine = new Line();

    private Point coords = new Point();

    double speed = 0.0;
    double rotation = 0.0;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Player(double x, double y) {
        Platform.runLater(() -> {
            circle.setRadius(RADIUS);
            circle.setFill(Color.web("#FF0000"));
        });
        move(x, y);
    }

    private void move(double x, double y) {
        coords.setLocation(coords.getX()+x, coords.getY()+y);
        updateValue(this.coords);

        /*
            directionLines' properties don't need to be bind as circles' ones, because
            theirs changes per time unit are much lower
         */
        Platform.runLater(() -> {
            directionLine.setStartX(circle.getCenterX());
            directionLine.setStartY(circle.getCenterY());

            directionLine.setEndX(circle.getCenterX()+circle.getRadius()*Math.cos(rotation*Math.PI/180.0));
            directionLine.setEndY(circle.getCenterY()+circle.getRadius()*Math.sin(rotation*Math.PI/180.0));
        });
    }


    private static final double      KEYBOARD_MOVEMENT_DELTA = 1.0;
    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);

    void dlugoTrwaleObliczenia() {
        long lastTime = -1;
        long currTime;
        double delta_t;
        double rotationInRadians;
        double currTime2;
        long millis;
        int nanos;
        while (!Thread.interrupted()) {
            currTime = System.nanoTime();
            if (lastTime != -1) {
                /*
                    set speed and rotation
                 */
                delta_t = (double)(currTime - lastTime)/(1000000000.0);

                if (upPressed.get()) {
                    speed += ACCELERATION * delta_t;
                }
                else if (downPressed.get()) {
                    speed -= ACCELERATION * delta_t;
                }
                else if (speed != 0) {
                    speed -= Math.signum(speed) * PASSIVE_ACCELERATION * delta_t;
                }

                if (leftPressed.get()) {
                    rotation -= (ROTATION_SPEED * delta_t)%360;
                }
                else if (rightPressed.get()) {
                    rotation += (ROTATION_SPEED * delta_t)%360;
                }

                /*
                    perform movement
                 */
                rotationInRadians=rotation*Math.PI/180.0;
                move(Math.cos(rotationInRadians) * speed * delta_t,
                        Math.sin(rotationInRadians) * speed * delta_t);
            }

            /*
                refresh rate
             */
            currTime2 = System.nanoTime();
            millis = (long)(1000.0/FPS-(currTime2-currTime)/1000000);
            nanos = (int)(((1000.0/FPS-(currTime2-currTime)/1000000.0)-((double)millis))*1000000.0);
            try {
                Thread.sleep(millis, nanos);
            } catch (InterruptedException e){
                break;
            }

            lastTime = currTime;
        }
    }


    private void moveCircleOnKeyPress(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    upPressed.set(true); break;
                case RIGHT: rightPressed.set(true); break;
                case DOWN:  downPressed.set(true); break;
                case LEFT:  leftPressed.set(true); break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:    upPressed.set(false); break;
                case RIGHT: rightPressed.set(false); break;
                case DOWN:  downPressed.set(false); break;
                case LEFT:  leftPressed.set(false); break;
            }
        });
    }

    public void runMovementThread(Scene scene) {
        moveCircleOnKeyPress(scene);

        circle.centerXProperty().bind(this.valueProperty().getValue().getXProperty());
        circle.centerYProperty().bind(this.valueProperty().getValue().getYProperty());

        executor.submit(this);
    }

    public Circle getCircle() {
        return circle;
    }

    public Line getDirectionLine() {
        return directionLine;
    }


    @Override
    protected Point call() throws Exception {
        dlugoTrwaleObliczenia();
        return this.coords;
    }
}
