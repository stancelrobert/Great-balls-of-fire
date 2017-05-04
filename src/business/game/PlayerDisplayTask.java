package business.game;

import business.util.FPSManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Robert on 07.04.2017.
 */
public class PlayerDisplayTask implements Runnable {
    public static final double RADIUS = 25.0;
    private static final double FPS = 57.0;


    private Pane board;
    private Player player;
    private Circle circle = new Circle(RADIUS, Color.web("#FF0000"));
    private Line directionLine = new Line();
    private DoubleProperty pX = new SimpleDoubleProperty(0.0);
    private DoubleProperty pY = new SimpleDoubleProperty(0.0);
    private DoubleProperty eX = new SimpleDoubleProperty(0.0);
    private DoubleProperty eY = new SimpleDoubleProperty(0.0);
    private BooleanProperty active = new SimpleBooleanProperty(true);
    private StringProperty playerName = new SimpleStringProperty();
    private IntegerProperty playerPoints = new SimpleIntegerProperty();


    /**
     * Constructor
     *
     * @param player player (or bot) to display
     */
    public PlayerDisplayTask(Player player, Pane board) {
        this.player = player;
        this.board = board;

        playerName.set(player.getColor());
        playerPoints.set(player.getPoints());

        circle.centerXProperty().bind(pX);
        circle.centerYProperty().bind(pY);
        circle.setFill(Color.web(player.getColor()));
        circle.visibleProperty().bind(active);

        directionLine.startXProperty().bind(pX);
        directionLine.startYProperty().bind(pY);

        directionLine.endXProperty().bind(eX);
        directionLine.endYProperty().bind(eY);
        directionLine.visibleProperty().bind(active);

        Platform.runLater(() -> {
            board.getChildren().add(circle);
            board.getChildren().add(directionLine);
        });
    }



    @Override
    public void run() {

        if (player.isActive()) {
            active.set(true);
            pX.set(player.getCoords().getX()+293);
            pY.set(player.getCoords().getY()+310);

            eX.set(player.getCoords().getX()+293+circle.getRadius()*Math.cos(player.getRotation()*Math.PI/180.0));
            eY.set(player.getCoords().getY()+310+circle.getRadius()*Math.sin(player.getRotation()*Math.PI/180.0));
            setPlayerPoints(player.getPoints());

        }
        else {
            active.set(false);
            setPlayerPoints(1);
        }
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

    public Circle getCircle() {
        return circle;
    }

    public Line getDirectionLine() {
        return directionLine;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public int getPlayerPoints() {
        return playerPoints.get();
    }

    public IntegerProperty playerPointsProperty() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints.set(playerPoints);
    }
}
