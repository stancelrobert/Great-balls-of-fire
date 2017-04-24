package controllers;

import business.game.PlayerDisplayTask;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private BoardController boardController;
    private MainController mainController;
    @FXML TableView<PlayerDisplayTask> table;
    @FXML TableColumn<PlayerDisplayTask, String> playerColumn;
    @FXML TableColumn<PlayerDisplayTask, Integer> pointsColumn;

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void newGameButtonClicked(ActionEvent actionEvent) {
        mainController.getParent().requestFocus();
        mainController.initNewGame();

    }


    public void joinGameButtonClicked(ActionEvent actionEvent) {
        mainController.getParent().requestFocus();
        mainController.initClient();

        table.setItems(mainController.getBoardController().getDisplayTasks());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        playerColumn.setCellValueFactory(
//                p -> new SimpleStringProperty(p.getValue().getFile().getName()));
//
//        pointsColumn.setCellValueFactory(
//                p -> displayTask.getValue().
//        );

        playerColumn.setCellValueFactory(
                p -> p.getValue().playerNameProperty()
        );

        pointsColumn.setCellValueFactory(
                p -> p.getValue().playerPointsProperty().asObject()
        );


    }

    public void addBotButtonClicked(ActionEvent actionEvent) {
        mainController.getParent().requestFocus();
        mainController.addBot();
    }
}
