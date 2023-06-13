package gamegui;

import application.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameRunningGUI {

    App app;
    boolean xPressed = false;

    public GameRunningGUI(App app) {
        this.app = app;
    }

    public void show() {

        Label countdownLabel = new Label();
        countdownLabel.setFont(new Font("Arial", 50));  // Define a fonte e o tamanho do texto na label
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);  // Centraliza os itens no VBox
        layout.getChildren().add(countdownLabel);
        Scene scene = new Scene(layout, 300, 200);

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                GameRunningGUI.this.app.getStage().setScene(scene);
            }
        });

        IntegerProperty countdownSeconds = new SimpleIntegerProperty(5);
        Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (countdownSeconds.get() > 0) {
                countdownLabel.setText(Integer.toString(countdownSeconds.get()));
                countdownSeconds.set(countdownSeconds.get() - 1);
            }
        }));

        countdownTimeline.setCycleCount(countdownSeconds.get() + 1);

        Pane gameRunningScreen = new Pane();

         // Creating gameRunning scene
        Scene gameRunning = new Scene(gameRunningScreen, App.panelWidth, App.panelHeight + App.carHeight * this.app.getPlayers().size());

        int squareSize = 5; // Define the size of your checker pattern squares
        int numberOfSquares = App.carHeight * this.app.getPlayers().size() / squareSize;

        // Draw start line
        for (int i = 0; i < numberOfSquares; i++) {
            Rectangle square = new Rectangle(App.carXPosition + App.carWidth, App.carYPosition + (i * squareSize), 10, squareSize);
            square.setFill(i % 2 == 0 ? Color.BLACK : Color.WHITE);
            square.setStroke(Color.BLACK); // Add border to the square
            gameRunningScreen.getChildren().add(square);
        }

        // Draw finish line
        for (int i = 0; i < numberOfSquares; i++) {
            Rectangle square = new Rectangle(App.panelWidth - App.carXPosition - App.carWidth - squareSize, App.carYPosition + (i * squareSize), 10, squareSize);
            square.setFill(i % 2 == 0 ? Color.BLACK : Color.WHITE);
            square.setStroke(Color.BLACK); // Add border to the square
            gameRunningScreen.getChildren().add(square);
        }


        Label nameLabels[] = new Label[this.app.getPlayers().size()];

        // Draw players
        for(int i = 0; i < GameRunningGUI.this.app.getPlayers().size(); i++) {
            GameRunningGUI.this.app.getPlayers().set(i, new Car(new Image("/assets/car" + ((i % 3) + 1) + ".png"), App.CAR_SPEED, App.carXPosition, App.carYPosition + i * App.CAR_DIST, App.carWidth, App.carHeight, this.app.getPlayers().get(i).nickName));

            nameLabels[i] = new Label(this.app.getPlayers().get(i).nickName);
            nameLabels[i].setLayoutX(App.carXPosition - 55);
            nameLabels[i].setLayoutY(App.carYPosition + i * App.CAR_DIST);
            
            gameRunningScreen.getChildren().addAll(GameRunningGUI.this.app.getPlayers().get(i), nameLabels[i]);
        }

        Stop[] stops = new Stop[] { new Stop(0, Color.RED), new Stop(0.75, Color.YELLOW), new Stop(1, Color.GREEN)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0.5, 0, true, CycleMethod.REFLECT, stops);

        // Draw the box
        Box box = new Box(App.panelWidth / 2 - App.BOX_WIDTH / 4, App.boxInitPosY, App.BOX_WIDTH / 2, App.BOX_HEIGHT, App.SLIDER_SPEED);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.getSlider().setStroke(Color.BLACK);
        box.setFill(lg1);
        gameRunningScreen.getChildren().addAll(box, box.getSlider());

        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getText().equals("x") && GameRunningGUI.this.xPressed == false) {
                    // print the attack value based on the position of the slider
                    double attackValue = Math.abs(((double) box.getSlider().getEndX() - App.boxInitPosX) - App.BOX_WIDTH / 2) / (App.BOX_WIDTH / 2);
                    // System.out.println("Attack power: " + (1 - attackValue));

                    GameRunningGUI.this.app.getClientSideConnection().sendMessage(GameRunningGUI.this.app.getPlayerID() + " " + (0.5 - attackValue));
                    GameRunningGUI.this.xPressed = true;
                }
            }
        };

        gameRunning.setOnKeyPressed(eventHandler);

        // Animation to move the slider
        AnimationTimer slider = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate <= 5_000_000) {
                    return;
                }
                lastUpdate = now ;
                if (box.isMovingRight()) {
                    box.setSliderPosX(box.getSliderPosX() + App.SLIDER_SPEED);
                    if (box.getSlider().getEndX() >= App.BOX_WIDTH / 2 + App.panelWidth / 2 - App.BOX_WIDTH / 4) {
                        box.setMovingRight(false);
                        GameRunningGUI.this.xPressed = false;
                    }
                } else {
                    box.setSliderPosX(box.getSliderPosX()- App.SLIDER_SPEED);
                    if (box.getSlider().getEndX() <= App.panelWidth / 2 - App.BOX_WIDTH / 4) {
                        box.setMovingRight(true);
                        GameRunningGUI.this.xPressed = false;
                    }
                }
                
                for(int i = 0; i < GameRunningGUI.this.app.getPlayers().size(); i++) {
                    GameRunningGUI.this.app.getPlayers().get(i).setX(GameRunningGUI.this.app.getPlayers().get(i).getCurrentX());
                }
                
            }
        };

        slider.start();

        countdownTimeline.setOnFinished(e -> {
            Platform.runLater(new Runnable() {

            @Override
            public void run() {
                GameRunningGUI.this.app.getStage().setScene(gameRunning);
            }
            });
        });
        
        // Platform.runLater(new Runnable() {

        //     @Override
        //     public void run() {
        //         GameRunningGUI.this.app.getStage().setScene(gameRunning);
        //     }
        // });

        countdownTimeline.play();
    }
}
