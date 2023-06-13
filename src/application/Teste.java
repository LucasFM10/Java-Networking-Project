package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Teste extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Countdown");

        Label countdownLabel = new Label();
        VBox layout = new VBox(10);
        layout.getChildren().add(countdownLabel);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);

        IntegerProperty countdownSeconds = new SimpleIntegerProperty(5);
        Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (countdownSeconds.get() > 0) {
                countdownLabel.setText(Integer.toString(countdownSeconds.get()));
                countdownSeconds.set(countdownSeconds.get() - 1);
            } else {
                countdownLabel.setText("Jogo começou!");
            }
        }));

        countdownTimeline.setCycleCount(countdownSeconds.get() + 1);
        countdownTimeline.play();

        primaryStage.show();
    }
}
