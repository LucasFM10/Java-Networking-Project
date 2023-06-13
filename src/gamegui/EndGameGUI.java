package gamegui;

import application.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndGameGUI extends Application {

    App app;

    private Label firstPlaceLabel;
    private Label secondPlaceLabel;
    private Label thirdPlaceLabel;


    public EndGameGUI(App app){
        this.app = app;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        firstPlaceLabel = new Label("1st Place: -");
        secondPlaceLabel = new Label("2nd Place: -");
        thirdPlaceLabel = new Label("3rd Place: -");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(firstPlaceLabel, secondPlaceLabel, thirdPlaceLabel);

        Button backButton = new Button("Voltar ao Menu");
        backButton.setOnAction(event -> {
            // Lógica para voltar ao menu aqui
            System.out.println("Voltando ao menu...");
        });

        root.getChildren().add(backButton);

        Scene scene = new Scene(root, 200, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("End Game");
        primaryStage.show();
    }

    public void setFirstPlace(String playerName) {
        firstPlaceLabel.setText("1st Place: " + playerName);
    }

    public void setSecondPlace(String playerName) {
        secondPlaceLabel.setText("2nd Place: " + playerName);
    }

    public void setThirdPlace(String playerName) {
        thirdPlaceLabel.setText("3rd Place: " + playerName);
    }
}