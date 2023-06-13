package controllers;

import application.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class LobbyController {

    App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private Label statusLabel;
    @FXML
    private Label playersLabel;
    @FXML
    private Label playerIDLabel;
    @FXML
    private Button startGameButton;

    @FXML
    public void initialize() {
        // inicialize aqui seus componentes, se necessário
    }

    @FXML
    public void startGame() {
        this.app.getGameServer().endConnections();
        System.out.println("Jogo iniciado");
    }

}
