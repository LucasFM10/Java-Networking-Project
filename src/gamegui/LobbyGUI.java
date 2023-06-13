package gamegui;

import java.util.Optional;

import application.App;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LobbyGUI {

    App app;

    public LobbyGUI(App app) {
        this.app = app;
    }

    public void showServerScreen() {
        String nicknameString = promptForNickname();
        if (nicknameString != null) {
            System.out.println(nicknameString);
            this.app.nickName = nicknameString;
            System.out.println(this.app.nickName);
        }
        // Reset the status and player labels
        this.app.setStatusLabel("");
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
        String nicknameString = promptForNickname();
        if (nicknameString != null) {
            System.out.println(nicknameString);
            this.app.nickName = nicknameString;
            System.out.println(this.app.nickName);
        }
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

    private String promptForNickname() {
        
        final String[] nickname = new String[1];
        Platform.runLater(() -> {
            TextInputDialog nicknameDialog = new TextInputDialog("");
            nicknameDialog.setTitle("Insira um apelido");
            nicknameDialog.setHeaderText(null);
            nicknameDialog.setContentText("Por favor, insira seu apelido:");

            Optional<String> result = nicknameDialog.showAndWait();
            if (result.isPresent()) {
                nickname[0] = result.get();
            }
        });

        return nickname[0];
    }
}
