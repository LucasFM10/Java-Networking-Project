package gamegui;

import application.*;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class GameRunningGUI {

    App app;

    public GameRunningGUI(App app) {
        this.app = app;
    }

    public void show() {

        Pane gameRunningScreen = new Pane();

         // Creating gameRunning scene
        Scene gameRunning = new Scene(gameRunningScreen, App.panelWidth, App.panelHeight);

        // Draw start line
        Rectangle startLine = new Rectangle(App.carXPosition, App.panelHeight - App.carYPosition + 20, 10, App.BOX_HEIGHT);
        startLine.setFill(Color.BLUE);
        startLine.setStroke(Color.BLACK);
        gameRunningScreen.getChildren().addAll(startLine);

        // Draw finish line
        Rectangle finish = new Rectangle(App.panelWidth - App.carXPosition - 10, App.panelHeight - App.carYPosition + 20, 10, App.BOX_HEIGHT);
        finish.setFill(Color.RED);
        finish.setStroke(Color.BLACK);
        gameRunningScreen.getChildren().addAll(finish);

        // Draw players
        for(int i = 0; i < GameRunningGUI.this.app.getPlayers().size(); i++) {
            GameRunningGUI.this.app.getPlayers().set(i, new Car(new Image("/assets/car" + ((i % 3) + 1) + ".png"), App.CAR_SPEED, App.carXPosition, App.carYPosition + i * App.CAR_DIST, App.carWidth, App.carHeight));
            gameRunningScreen.getChildren().addAll(GameRunningGUI.this.app.getPlayers().get(i));
        }

        Stop[] stops = new Stop[] { new Stop(0, Color.RED), new Stop(0.75, Color.YELLOW), new Stop(1, Color.GREEN)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0.5, 0, true, CycleMethod.REFLECT, stops);

        // Draw the box
        Box box = new Box(App.boxInitPosX, App.boxInitPosY, App.BOX_WIDTH, App.BOX_HEIGHT, App.SLIDER_SPEED);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.getSlider().setStroke(Color.BLACK);
        box.setFill(lg1);
        gameRunningScreen.getChildren().addAll(box, box.getSlider());

        gameRunning.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getText().equals("x")) {
                    // print the attack value based on the position of the slider
                    double attackValue = Math.abs(((double) box.getSlider().getEndX() - App.boxInitPosX) - App.BOX_WIDTH / 2) / (App.BOX_WIDTH / 2);
                    // System.out.println("Attack power: " + (1 - attackValue));

                    GameRunningGUI.this.app.getClientSideConnection().sendMessage(GameRunningGUI.this.app.getPlayerID() + " " + (0.5 - attackValue));
                }
                
            }
        });

        // Animation to move the slider
        AnimationTimer slider = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate <= 17_000_000) {
                    return;
                }
                lastUpdate = now ;
                if (box.isMovingRight()) {
                    box.setSliderPosX(box.getSliderPosX() + App.SLIDER_SPEED);
                    if (box.getSlider().getEndX() >= App.BOX_WIDTH + App.boxInitPosX) {
                        box.setMovingRight(false);
                    }
                } else {
                    box.setSliderPosX(box.getSliderPosX()- App.SLIDER_SPEED);
                    if (box.getSlider().getEndX() <= + App.boxInitPosX) {
                        box.setMovingRight(true);
                    }
                }
                
                for(int i = 0; i < GameRunningGUI.this.app.getPlayers().size(); i++) {
                    GameRunningGUI.this.app.getPlayers().get(i).setX(GameRunningGUI.this.app.getPlayers().get(i).getCurrentX());
                }
                
            }
        };

        slider.start();

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                GameRunningGUI.this.app.getStage().setScene(gameRunning);
            }
        });
    }
}
