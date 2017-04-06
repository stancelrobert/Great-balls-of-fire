package controllers;

import business.game.Player;
import business.game.PlayerMovementTask;
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


    public AnchorPane getBoardPane() {
        return boardPane;
    }

    @FXML private AnchorPane boardPane;

    private MainController mainController;

    public Player getPlayer() {
        return playerMovementTask.getPlayer();
    }

    private PlayerMovementTask playerMovementTask;

    private ArrayList<Player> otherPlayers = new ArrayList<>(3);

    public void serveCollision() {
        double distance;
        for (Player otherPlayer : otherPlayers) {
            distance = Math.sqrt(
                        Math.pow(playerMovementTask.getPlayer().getCoords().getX()-otherPlayer.getCoords().getX(), 2)
                        + Math.pow(playerMovementTask.getPlayer().getCoords().getY()-otherPlayer.getCoords().getY(), 2));
            if (distance <= PlayerMovementTask.RADIUS) {
                //TODO ustawienie prędkości oraz obrotu obu ciał po kolizji

            }
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void addPlayer(Player player) {
        this.playerMovementTask = new PlayerMovementTask(this, player);

        this.boardPane.getChildren().add(playerMovementTask.getCircle());
        this.boardPane.getChildren().add(playerMovementTask.getDirectionLine());

        this.playerMovementTask.runMovementThread(this.mainController.getStage().getScene());
    }

    public void addOtherPlayer(Player player) {
        otherPlayers.add(player);
        this.boardPane.getChildren().add(playerMovementTask.getCircle());
        this.boardPane.getChildren().add(playerMovementTask.getDirectionLine());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
