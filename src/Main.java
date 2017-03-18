import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {
    private static final int      KEYBOARD_MOVEMENT_DELTA = 1;
    //private static final Duration TRANSLATE_DURATION      = Duration.seconds(0.25);
    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);

    final BooleanProperty wPressed = new SimpleBooleanProperty(false);
    final BooleanProperty sPressed = new SimpleBooleanProperty(false);
    final BooleanProperty aPressed = new SimpleBooleanProperty(false);
    final BooleanProperty dPressed = new SimpleBooleanProperty(false);

    final BooleanBinding upAndRightPressed = upPressed.and(rightPressed);
    final BooleanBinding downAndRightPressed = downPressed.and(rightPressed);
    final BooleanBinding upAndLeftPressed = upPressed.and(leftPressed);
    final BooleanBinding downAndLeftPressed = downPressed.and(leftPressed);

    final BooleanBinding wAnddPressed = wPressed.and(dPressed);
    final BooleanBinding sAnddPressed = sPressed.and(dPressed);
    final BooleanBinding wAndaPressed = wPressed.and(aPressed);
    final BooleanBinding sAndaPressed = sPressed.and(aPressed);


    Runnable task;
    Thread thread;

    public static void main(String[] args) { launch(args); }
    @Override public void start(Stage stage) throws Exception {
        final Circle circle = createCircle();
        final Circle circle2 = createCircle2();
        final Group group = new Group(createInstructions(), circle, circle2);
        //final TranslateTransition transition = createTranslateTransition(circle);

        final Scene scene = new Scene(group, 800, 600, Color.CORNSILK);
        moveCircleOnKeyPress(scene, circle);
        moveCircleOnKeyPress2(scene, circle2);
        //moveCircleOnMousePress(scene, circle, transition);

        task = () ->
            dlugoTrwaleObliczenia(circle, circle2);


        thread = new Thread(task);
        thread.start();
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                thread.interrupt();
            }
        });
    }

    private Label createInstructions() {
        Label instructions = new Label(
                "Use the arrow keys to move the circle in small increments\n" +
                        "Click the mouse to move the circle to a given location\n" +
                        "Ctrl + Click the mouse to slowly translate the circle to a given location"
        );
        instructions.setTextFill(Color.FORESTGREEN);
        return instructions;
    }

    private Circle createCircle() {
        final Circle circle = new Circle(800, 600, 50, Color.BLUEVIOLET);
        circle.setOpacity(0.7);
        return circle;
    }

    private Circle createCircle2() {
        final Circle circle = new Circle(400, 300, 50, Color.GREENYELLOW);
        circle.setOpacity(0.7);
        return circle;
    }

//    private TranslateTransition createTranslateTransition(final Circle circle) {
//        final TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, circle);
//        transition.setOnFinished(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent t) {
//                circle.setCenterX(circle.getTranslateX() + circle.getCenterX());
//                circle.setCenterY(circle.getTranslateY() + circle.getCenterY());
//                circle.setTranslateX(0);
//                circle.setTranslateY(0);
//            }
//        });
//        return transition;
//    }

    void dlugoTrwaleObliczenia(Circle circle, Circle circle2) {
        while (!Thread.interrupted()) {
            if (upAndLeftPressed.get()) {
                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (upAndRightPressed.get()) {
                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (downAndLeftPressed.get()) {
                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (downAndRightPressed.get()) {
                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (upPressed.get()) {
                circle.setCenterY(circle.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (rightPressed.get()) {
                circle.setCenterX(circle.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (leftPressed.get()) {
                circle.setCenterX(circle.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (downPressed.get()) {
                circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }


            if (wAndaPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                circle2.setCenterY(circle2.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (wAnddPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                circle2.setCenterY(circle2.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (sAndaPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
                circle2.setCenterY(circle2.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (sAnddPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
                circle2.setCenterY(circle2.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (wPressed.get()) {
                circle2.setCenterY(circle2.getCenterY() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (dPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() + KEYBOARD_MOVEMENT_DELTA);
            }
            else if (aPressed.get()) {
                circle2.setCenterX(circle2.getCenterX() - KEYBOARD_MOVEMENT_DELTA);
            }
            else if (sPressed.get()) {
                circle2.setCenterY(circle2.getCenterY() + KEYBOARD_MOVEMENT_DELTA);
            }


            try {
                Thread.sleep(10);
            } catch (InterruptedException e){
                break;
            }
        }
    }

    private void moveCircleOnKeyPress(Scene scene, final Circle circle) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        upPressed.set(true);
                        break;
                    case RIGHT:
                        rightPressed.set(true);
                        break;
                    case DOWN:
                        downPressed.set(true);
                        break;
                    case LEFT:
                        leftPressed.set(true);
                        break;
                }

            }

        });


        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        upPressed.set(false);
                        break;
                    case RIGHT:
                        rightPressed.set(false);
                        break;
                    case DOWN:
                        downPressed.set(false);
                        break;
                    case LEFT:
                        leftPressed.set(false);
                        break;
                }
            }
        });


    }

    private void moveCircleOnKeyPress2(Scene scene, final Circle circle) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        wPressed.set(true);
                        break;
                    case D:
                        dPressed.set(true);
                        break;
                    case S:
                        sPressed.set(true);
                        break;
                    case A:
                        aPressed.set(true);
                        break;
                }

            }

        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        wPressed.set(false);
                        break;
                    case D:
                        dPressed.set(false);
                        break;
                    case S:
                        sPressed.set(false);
                        break;
                    case A:
                        aPressed.set(false);
                        break;
                }
            }
        });
    }}

//    private void moveCircleOnMousePress(Scene scene, final Circle circle, final TranslateTransition transition) {
//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent event) {
//                if (!event.isControlDown()) {
//                    circle.setCenterX(event.getSceneX());
//                    circle.setCenterY(event.getSceneY());
//                } else {
//                    transition.setToX(event.getSceneX() - circle.getCenterX());
//                    transition.setToY(event.getSceneY() - circle.getCenterY());
//                    transition.playFromStart();
//                }
//            }
//        });
//    }