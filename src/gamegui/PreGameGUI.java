package gamegui;

import application.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PreGameGUI {
    App app;

    public PreGameGUI(App app) {
        this.app = app;
    }

    public void showServerScreen() {
        
        this.app.setStatusLabel("");
        this.app.setPlayersLabel("");

        VBox serverLobbyScreen = new VBox(10);
        serverLobbyScreen.setPadding(new Insets(20, 20, 20, 20));
        serverLobbyScreen.getChildren().addAll(this.app.getStatusLabel(), this.app.getPlayersLabel());

        Button btn = new Button("Iniciar Jogo");

        btn.setOnAction(e -> {
            System.out.println("Jogo iniciado");
            // this.app.getClientSideConnection().sendMessage("oi");
            this.app.getGameServer().endConnections();
        });

        serverLobbyScreen.getChildren().addAll(btn);

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                PreGameGUI.this.app.getStage().setScene(new Scene(serverLobbyScreen, 400, 300));
            }
        });

    }

    public void showClientScreen() {
        
        this.app.setStatusLabel("");
        this.app.setPlayersLabel("");


        VBox clientLobbyScreen = new VBox(10);
        clientLobbyScreen.setPadding(new Insets(20, 20, 20, 20));
        clientLobbyScreen.getChildren().addAll(this.app.getStatusLabel(), this.app.getPlayersLabel());

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                PreGameGUI.this.app.getStage().setScene(new Scene(clientLobbyScreen, 400, 300));
            }
        });
    }
}
