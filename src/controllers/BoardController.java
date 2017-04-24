package controllers;

import business.game.Player;
import business.game.PlayerDisplayTask;
import business.server.Server;
import business.util.FPSManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BoardController {
    @FXML private Circle outsideBoardCircle;
    @FXML private Circle insideBoardCircle;

    public AnchorPane getBoardPane() {
        return boardPane;
    }

    @FXML private AnchorPane boardPane;

    private MainController mainController;

    private ObservableList<PlayerDisplayTask> displayTasks = FXCollections.observableArrayList();
    private List<Player> players = new ArrayList<>(3);

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void addPlayer(Player player) {
        players.add(player);
        PlayerDisplayTask playerDisplayTask = new PlayerDisplayTask(player);
        displayTasks.add(playerDisplayTask);

        Platform.runLater(() -> {
            this.boardPane.getChildren().add(playerDisplayTask.getCircle());
            this.boardPane.getChildren().add(playerDisplayTask.getDirectionLine());
        });
    }

    public synchronized void displayTask() {
        FPSManager fpsManager = new FPSManager(57.0);
        Player player;
        PlayerDisplayTask displayTask;
        fpsManager.start();
        while (!Thread.interrupted()) {
            for (int i = 0; i < players.size(); i++) {
                try {
                    player = players.get(i);
                    displayTask = displayTasks.get(i);

                    if (player.isActive()) {
                        displayTask.setActive(true);
                        displayTask.setpX(player.getCoords().getX()+293);
                        displayTask.setpY(player.getCoords().getY()+310);

                        displayTask.seteX(player.getCoords().getX()+293+displayTask.getCircle().getRadius()*Math.cos(player.getRotation()*Math.PI/180.0));
                        displayTask.seteY(player.getCoords().getY()+310+displayTask.getCircle().getRadius()*Math.sin(player.getRotation()*Math.PI/180.0));
                        displayTask.setPlayerPoints(player.getPoints());
                    }
                    else {
                        displayTask.setActive(false);
                    }

                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("Must wait a sec.");
                }
            }
            fpsManager.waitForNextFrame();
        }
    }

    public ObservableList<PlayerDisplayTask> getDisplayTasks() {
        return displayTasks;
    }

}
