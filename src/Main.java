import controllers.MainController;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static final int      KEYBOARD_MOVEMENT_DELTA = 1;
    private static final Duration TRANSLATE_DURATION      = Duration.seconds(0.25);
    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);

    final BooleanBinding upAndRightPressed = upPressed.and(rightPressed);
    final BooleanBinding downAndRightPressed = downPressed.and(rightPressed);
    final BooleanBinding upAndLeftPressed = upPressed.and(leftPressed);
    final BooleanBinding downAndLeftPressed = downPressed.and(leftPressed);

    Runnable task;
    Thread thread;

    public static void main(String[] args) { launch(args); }
    @Override public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/main.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        mainController.setStage(primaryStage);


//        final Circle circle = createCircle();
//        final Group group = new Group(createInstructions(), circle);
//        final TranslateTransition transition = createTranslateTransition(circle);
//
//        final Scene scene = new Scene(group, 800, 640, Color.CORNSILK);
//        moveCircleOnKeyPress(scene, circle);
//        moveCircleOnMousePress(scene, circle, transition);
//
//        task = () ->
//            dlugoTrwaleObliczenia(circle);
//
//
//        thread = new Thread(task);
//        thread.start();
//
//        stage.setScene(scene);
//        stage.sizeToScene();
//        stage.show();
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            public void handle(WindowEvent we) {
//                thread.interrupt();
//            }
//        });
    }

    private Circle createCircle() {
        final Circle circle = new Circle(400, 320, 50, Color.BLUEVIOLET);
        circle.setOpacity(0.7);
        return circle;
    }

    void dlugoTrwaleObliczenia(Circle circle) {
        while (!Thread.interrupted()) {
//            if (upAndLeftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (upAndRightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downAndLeftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downAndRightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (upPressed.get()) {
//                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (rightPressed.get()) {
//                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (leftPressed.get()) {
//                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
//            }
//            else if (downPressed.get()) {
//                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
//            }



            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
                break;
            }
        }
    }

    private void moveCircleOnKeyPress(Scene scene, final Circle circle) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    upPressed.set(true); break;
                    case RIGHT: rightPressed.set(true); break;
                    case DOWN:  downPressed.set(true); break;
                    case LEFT:  leftPressed.set(true); break;
                }

            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    upPressed.set(false); break;
                    case RIGHT: rightPressed.set(false); break;
                    case DOWN:  downPressed.set(false); break;
                    case LEFT:  leftPressed.set(false); break;
                }
            }
        });
    }

}