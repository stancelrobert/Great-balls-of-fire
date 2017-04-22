import business.game.Player;
import business.game.Point;
import business.game.Vector;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/main.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        primaryStage.setTitle("Great Balls of Fire");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        mainController.setStage(primaryStage);
    }
}