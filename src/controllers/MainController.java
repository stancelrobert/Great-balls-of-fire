package controllers;

import business.client.Client;
import business.game.Game;
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
        boardController.addPlayer(new Player());
        //TODO postawić serwer
//        try {
//            server = new Server();
//            server.run();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            System.out.println(boardController.getPlayer());
//            client = new Client(boardController.getPlayer(), Server.PORTS[0]);
//            client.run();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initClient() {
//        boardController.addPlayer(new Player(boardController, 200, 300));
//        try {
//            client = new Client(boardController.getPlayer(), Server.PORTS[0]);
//            client.run();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void initNewGame() {
        Player player = new Player();
        Game game = new Game(Game.InstanceType.HOST);
        game.addPlayer(player);
        boardController.addPlayer(player);
    }
}
