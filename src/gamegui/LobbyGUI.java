package gamegui;

import application.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

        // cria uma nova vbox
        VBox serverLobbyScreen = new VBox(10);

        // define a padding
        serverLobbyScreen.setPadding(new Insets(20, 20, 20, 20));

        // Centraliza os itens na vbox
        serverLobbyScreen.setAlignment(Pos.CENTER);

        // adiciona as labels de status e player id para a vbox
        serverLobbyScreen.getChildren().addAll(this.app.getStatusLabel(), this.app.getPlayersLabel(), this.app.getPlayerIDLabel());

        // cria o botão de start
        Button btn = new Button("Iniciar Jogo");

        // define o tamanho da fonte
        btn.setFont(new Font(16));

        // adiciona a acao para o botao
        btn.setOnAction(e -> {
            System.out.println("Jogo iniciado");
            this.app.getGameServer().endConnections();
        });

        // adiciona o botao da vbox
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
