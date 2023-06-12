package controllers;
import java.io.IOException;
import java.net.ServerSocket;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UpdateGameController {

    App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private Text titleText;

    public void initialize() {
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    }

    @FXML
    protected void exibirDialogoCriarServidor(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
    }
}