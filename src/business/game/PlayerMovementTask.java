package business.game;

import controllers.BoardController;
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

/**
 * Created by Robert on 02.04.2017.
 */
public class PlayerMovementTask extends Task<Void> {
    public static final double RADIUS = 25.0;
    private static final double ACCELERATION = 700.0;
    private static final double MAX_SPEED = 450.0;
    private static final double ROTATION_SPEED = 360.0;
    private static final double PASSIVE_ACCELERATION = 300.0;

    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);

    private Player player;
    private BoardController boardController;
    private Scene scene;
    private double boardSizeX;
    private double boardSizeY;

    public PlayerMovementTask(BoardController boardController, Scene scene, Player player) {
        this.player = player;
        this.scene = scene;
        this.boardController = boardController;
        this.boardSizeX = this.boardController.getBoardPane().getWidth();
        this.boardSizeY = this.boardController.getBoardPane().getHeight();
        player.setCoords(300, 300);
        move(300, 300);

    }

    public Player getPlayer() {
        return player;
    }

    @Override
    protected Void call() throws Exception {
        initKeysEvents(scene);

        long currTime;
        double delta_t;
        long lastTime = System.nanoTime();
        while (!Thread.interrupted()) {
            currTime = System.nanoTime();
            /*
                set speed and rotation
             */
            delta_t = (double)(currTime - lastTime)/(1000000000.0);

            if (upPressed.get() && player.getSpeed() < MAX_SPEED) {
                player.setSpeed(player.getSpeed() + (ACCELERATION - Math.signum(player.getSpeed()) * PASSIVE_ACCELERATION) * delta_t);
            }
            else if (downPressed.get() && player.getSpeed() > -MAX_SPEED) {
                player.setSpeed(player.getSpeed() - (ACCELERATION + Math.signum(player.getSpeed()) * PASSIVE_ACCELERATION) * delta_t);
            }
            else if (player.getSpeed() != 0) {
                player.setSpeed(player.getSpeed() - Math.signum(player.getSpeed()) * PASSIVE_ACCELERATION * delta_t);
            }

            if (leftPressed.get()) {
                player.setRotation((player.getRotation() - ROTATION_SPEED * delta_t) % (360));
            }
            else if (rightPressed.get()) {
                player.setRotation((player.getRotation() + ROTATION_SPEED * delta_t) % (360));
            }

            /*
                perform movement
             */
            move(player.getCoords().getX()+Math.cos(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t,
                    player.getCoords().getY()+Math.sin(Math.toRadians(player.getRotation())) * player.getSpeed() * delta_t);


            lastTime = currTime;
        }
        return null;
    }

    private boolean correctCoords(double x, double y) {
        return (x > RADIUS && y > RADIUS && x < boardSizeX-RADIUS && y < boardSizeY-RADIUS);
    }

    private void move(double x, double y) {
        if (correctCoords(x, y)) {
            player.setCoords(x, y);
        }
        else {
            player.setSpeed(0.0);
        }
    }

    private void initKeysEvents(Scene scene) {
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

}
