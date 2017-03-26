package controllers;

import javafx.event.ActionEvent;

public class MenuController {
    private BoardController boardController;
    private MainController mainController;

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void newGameButtonClicked(ActionEvent actionEvent) {
        mainController.initServer();
    }

    public void joinGameButtonClicked(ActionEvent actionEvent) {
        mainController.initClient();
    }
}
