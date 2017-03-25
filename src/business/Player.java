package business;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;


public class Player {
    private static final double RADIUS = 25.0;

    private Circle circle = new Circle();

    public Player(double x, double y) {
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);
        this.circle.setRadius(RADIUS);
    }

    private static final double ACCELERATION = 50.0;
    private static final double ROTATION_SPEED = 90.0;

    private static final double      KEYBOARD_MOVEMENT_DELTA = 1.0;
    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);

    final BooleanBinding upAndRightPressed = upPressed.and(rightPressed);
    final BooleanBinding downAndRightPressed = downPressed.and(rightPressed);
    final BooleanBinding upAndLeftPressed = upPressed.and(leftPressed);
    final BooleanBinding downAndLeftPressed = downPressed.and(leftPressed);

    long lastTime = -1;
    long currTime;
    double speed = 0.0;
    double rotation = 0.0;

    private Thread movementThread;

    synchronized void dlugoTrwaleObliczenia() {
        double delta_t;
        double rotationInRadians;
        while (!Thread.interrupted()) {
            currTime = System.nanoTime();

            if (lastTime != -1) {
                /**
                 * Ustawienie prędkości i obrotu
                 */

                delta_t = (double)(currTime - lastTime)/(1000000000.0);

                if (upPressed.get()) {
                    speed += ACCELERATION * delta_t;
                }
                else if (downPressed.get()) {
                    speed -= ACCELERATION * delta_t;
                }

                if (leftPressed.get()) {
                    rotation -= (ROTATION_SPEED * delta_t)%360;
                }
                else if (rightPressed.get()) {
                    rotation += (ROTATION_SPEED * delta_t)%360;
                }

                rotationInRadians=rotation*Math.PI/180.0;
                /**
                 * Poruszanie na planszy
                 */
                circle.setCenterX(circle.getCenterX() + Math.cos(rotationInRadians) * speed * delta_t);
                circle.setCenterY(circle.getCenterY() + Math.sin(rotationInRadians) * speed * delta_t);


                try {
                    Thread.sleep(10);
                } catch (InterruptedException e){
                    break;
                }
            }

            lastTime = currTime;



//            if (upAndLeftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (upAndRightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downAndLeftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downAndRightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (upPressed.get()) {
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (rightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (leftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downPressed.get()) {
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }





//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e){
//                break;
//            }
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
        Runnable task = this::dlugoTrwaleObliczenia;
        movementThread = new Thread(task);
        movementThread.start();
    }

    public Circle getCircle() {
        return circle;
    }



}
