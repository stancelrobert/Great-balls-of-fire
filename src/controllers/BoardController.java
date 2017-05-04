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
        PlayerDisplayTask playerDisplayTask = new PlayerDisplayTask(player, boardPane);
        displayTasks.add(playerDisplayTask);
    }

    public synchronized void displayTask() {
        FPSManager fpsManager = new FPSManager(57.0);
        PlayerDisplayTask displayTask;
        fpsManager.start();
        while (!Thread.interrupted()) {
            for (int i = 0; i < players.size(); i++) {
                try {
                    displayTask = displayTasks.get(i);

                    displayTask.run();

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
