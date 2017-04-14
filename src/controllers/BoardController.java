package controllers;

import business.game.Player;
import business.game.PlayerDisplayTask;
import business.util.FPSManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BoardController implements Initializable {
    @FXML private Circle outsideBoardCircle;
    @FXML private Circle insideBoardCircle;
    private ExecutorService executor = Executors.newFixedThreadPool(3);
    public AnchorPane getBoardPane() {
        return boardPane;
    }

    @FXML private AnchorPane boardPane;

    private MainController mainController;

    private PlayerDisplayTask playerDisplayTask;

    private ArrayList<PlayerDisplayTask> displayTasks = new ArrayList<>(3);
    private ArrayList<Player> players = new ArrayList<>(3);

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void addPlayer(Player player) {

        Platform.runLater(() -> {
            //this.playerMovementTask = new PlayerMovementTask(this, this.mainController.getStage().getScene(), player);
            players.add(player);
            PlayerDisplayTask playerDisplayTask = new PlayerDisplayTask(player);
            displayTasks.add(playerDisplayTask);
            //this.executor.submit(playerMovementTask);
            //this.executor.submit(playerDisplayTask);
            this.boardPane.getChildren().add(playerDisplayTask.getCircle());
            this.boardPane.getChildren().add(playerDisplayTask.getDirectionLine());
        });

    }

    public void addOtherPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void displayTask() {
        FPSManager fpsManager = new FPSManager(57.0);
        Player player;
        PlayerDisplayTask displayTask;
        fpsManager.start();
        while (!Thread.interrupted()) {
            for (int i = 0; i < players.size(); i++) {
                player = players.get(i);
                displayTask = displayTasks.get(i);

                displayTask.setpX(player.getCoords().getX()+293);
                displayTask.setpY(player.getCoords().getY()+310);

                displayTask.seteX(player.getCoords().getX()+293+displayTask.getCircle().getRadius()*Math.cos(player.getRotation()*Math.PI/180.0));
                displayTask.seteY(player.getCoords().getY()+310+displayTask.getCircle().getRadius()*Math.sin(player.getRotation()*Math.PI/180.0));
            }
            fpsManager.waitForNextFrame();
        }
    }
}
