package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CenarioComLogica extends Application {

    private Scene gameMenu, gameRunning, gameEnd;
    private Pane paneGameRunning, paneGameMenu, paneGameEnd;

    private static final int panelWidth = 300;
    private static final int panelHeight = 200;

    private static final int boxInitPosX = panelWidth / 4;
    private static final int boxInitPosY = panelHeight / 4;
    private static final int BOX_WIDTH = panelWidth / 2;
    private static final int BOX_HEIGHT = 50;
    private static final int SLIDER_SPEED = 4;
    public static final int CAR_SPEED = 40;
    private static int carXPosition = 50;
    private static int carYPosition = 115;

    private static int isGameRunning;

    private Car player;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Undertale Fight");

        // Creating gameRunning scene
        paneGameRunning = new Pane();
        gameRunning = new Scene(paneGameRunning, panelWidth, panelHeight);

        // Draw start line
        Rectangle start = new Rectangle(carXPosition, panelHeight - carYPosition + 20, 10, BOX_HEIGHT);
        start.setFill(Color.BLUE);
        start.setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(start);

        // Draw finish line
        Rectangle finish = new Rectangle(panelWidth - carXPosition - 10, panelHeight - carYPosition + 20, 10, BOX_HEIGHT);
        finish.setFill(Color.RED);
        finish.setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(finish);

        // Draw player
        player = new Car(new Image("/car1.png"), CAR_SPEED, carXPosition, carYPosition, 0, 0);

        //setting the fit height and width of the image view 
        player.setFitHeight(20); 
        player.setFitWidth(30);

        player.setScaleX(-1);

        paneGameRunning.getChildren().addAll(player);

        // Draw the box
        Box box = new Box(boxInitPosX, boxInitPosY, BOX_WIDTH, BOX_HEIGHT, SLIDER_SPEED);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.getSlider().setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(box, box.getSlider());

        gameRunning.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getText().equals("x")) {
                    // print the attack value based on the position of the slider
                    double attackValue = Math.abs(((double) box.getSlider().getEndX() - boxInitPosX) - BOX_WIDTH / 2) / (BOX_WIDTH / 2);
                    System.out.println("Attack power1: " + (1 - attackValue));

                    // move the player1 based on the attack value
                    player.setX(player.getX() + (0.5 - attackValue) * CAR_SPEED);

                }
                
            }
        });

        // Animation to move the slider
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (box.isMovingRight()) {
                    box.setSliderPosX(box.getSliderPosX() + SLIDER_SPEED);
                    if (box.getSlider().getEndX() >= BOX_WIDTH + boxInitPosX) {
                        box.setMovingRight(false);
                    }
                } else {
                    box.setSliderPosX(box.getSliderPosX()- SLIDER_SPEED);
                    if (box.getSlider().getEndX() <= + boxInitPosX) {
                        box.setMovingRight(true);
                    }
                }

                if(player.getX() > panelWidth - carXPosition) {
                    isGameRunning = 2;
                    primaryStage.setScene(gameEnd);
                    primaryStage.show();
                }
            }
        }.start();

        // Create the menu scene
        paneGameMenu = new Pane();
        gameMenu = new Scene(paneGameMenu, 100, 100);
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> primaryStage.setScene(gameRunning));
        startButton.setMinWidth(100);
        paneGameMenu.getChildren().addAll(startButton);

        // Create the end scene
        Button endButton = new Button("Menu");
        paneGameEnd = new Pane();
        gameEnd = new Scene(paneGameEnd, 100, 100);
        endButton.setOnAction(e -> {
            primaryStage.setScene(gameMenu);
            player.setX(0);
        });
        paneGameEnd.getChildren().addAll(endButton);

        primaryStage.setScene(gameMenu);
        primaryStage.show();
        }

    public static void main(String[] args) {
        launch(args);
    }

    public Object getGameRunningScene() {
        return null;
    }
}