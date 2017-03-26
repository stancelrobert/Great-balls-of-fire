package controllers;

import business.game.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML private Circle outsideBoardCircle;
    @FXML private Circle insideBoardCircle;

    @FXML private AnchorPane boardPane;

    private MainController mainController;

    private Player player;

    private ArrayList<Player> otherPlayers = new ArrayList<>(3);

    public void serveCollision() {
        double distance;
        for (Player otherPlayer : otherPlayers) {
            distance = Math.sqrt(
                        Math.pow(player.getCoords().getX()-otherPlayer.getCoords().getX(), 2)
                        + Math.pow(player.getCoords().getY()-otherPlayer.getCoords().getY(), 2));
            if (distance <= Player.RADIUS) {
                //TODO ustawienie prędkości oraz obrotu obu ciał po kolizji

            }
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void addPlayer() {
        this.player = new Player(this, 100, 200);
        this.boardPane.getChildren().add(player.getCircle());
        this.boardPane.getChildren().add(player.getDirectionLine());
        this.player.runMovementThread(this.mainController.getStage().getScene());
    }

    public void addOtherPlayer(Player player) {
        otherPlayers.add(player);
        this.boardPane.getChildren().add(player.getCircle());
        this.boardPane.getChildren().add(player.getDirectionLine());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public AnchorPane getBoardPane() {
        return boardPane;
    }
}
