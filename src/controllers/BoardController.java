package controllers;

import business.Player;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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

    private ArrayList<Player> otherPlayers;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void addPlayer() {
        this.player = new Player(100, 200);
        this.boardPane.getChildren().add(player.getCircle());
        this.player.runMovementThread(this.mainController.getStage().getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
