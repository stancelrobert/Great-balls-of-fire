package controllers;

import business.client.Client;
import business.game.Player;
import business.server.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private BoardController boardController;

    @FXML private MenuController menuController;

    private Stage stage;

    private Server server;
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setBoardController(boardController);
        menuController.setMainController(this);

        boardController.setMainController(this);
    }

    /**
     * initializes new server and adds local player to service
     */
    public void initServer() {
        //TODO postawić serwer
        try {
            server = new Server();
            server.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        boardController.addPlayer();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initClient() {
        try {
            client = new Client();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
