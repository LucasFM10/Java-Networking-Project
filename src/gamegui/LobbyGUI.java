package gamegui;

import java.util.Optional;

import application.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LobbyGUI {

    App app;

    public LobbyGUI(App app) {
        this.app = app;
    }

    public void showServerScreen() {

        // Reset the status and player labels
        this.app.setStatusLabel("");
        this.app.getStatusLabel().setWrapText(true);
        this.app.getStatusLabel().setTextAlignment(TextAlignment.CENTER);
        this.app.setPlayersLabel("");
        this.app.setPlayerIDLabel("");
        this.app.setServidorIPLabel("");

        // Create a new VBox with 10px spacing
        VBox serverLobbyScreen = new VBox(10);
        // Set padding of 20px on all sides
        serverLobbyScreen.setPadding(new Insets(20, 20, 20, 20));
        // Center all items in the VBox
        serverLobbyScreen.setAlignment(Pos.CENTER);

        // Add status and players labels to the VBox
        serverLobbyScreen.getChildren().addAll(this.app.getStatusLabel(), this.app.getPlayersLabel(), this.app.getPlayerIDLabel());

        // Create a start game button
        Button btn = new Button("Iniciar Jogo");
        // Set the font size to 16px
        btn.setFont(new Font(16));

        // Add an action to the button
        btn.setOnAction(e -> {
            System.out.println("Jogo iniciado");
            this.app.getGameServer().endConnections();
        });

        // Add button to the VBox
        serverLobbyScreen.getChildren().addAll(btn);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LobbyGUI.this.app.getStage().setScene(new Scene(serverLobbyScreen, 400, 300));
            }
        });
    }

    public void showClientScreen() {
        
        this.app.setStatusLabel("");
        this.app.setPlayersLabel("");
        this.app.setPlayerIDLabel("");

        VBox clientLobbyScreen = new VBox(10);
        clientLobbyScreen.setPadding(new Insets(20, 20, 20, 20));
        clientLobbyScreen.setAlignment(Pos.CENTER);
        clientLobbyScreen.getChildren().addAll(this.app.getStatusLabel(), this.app.getPlayersLabel(), this.app.getPlayerIDLabel());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LobbyGUI.this.app.getStage().setScene(new Scene(clientLobbyScreen, 400, 300));
            }
        });
    }
}
