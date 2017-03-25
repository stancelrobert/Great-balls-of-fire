package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private BoardController boardController;

    @FXML private MenuController menuController;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setBoardController(boardController);
        menuController.setMainController(this);

        boardController.setMainController(this);
    }

    public void initServer() {
        //TODO postawić serwer

        boardController.addPlayer();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

}
