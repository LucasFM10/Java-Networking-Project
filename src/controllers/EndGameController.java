package controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EndGameController {

    @FXML
    private Label labelJogadorVencedor;
    
    @FXML
    private Label segundoLugarLabel;
    
    @FXML
    private Label terceiroLugarLabel;
    
    @FXML
    private Button voltarAoMenuButton;
    
    @FXML
    void voltarAoMenu(ActionEvent event) {
        this.app.setSceneFXML("Menu");
    }
    
    App app;

    public void mostrarPlacar(int winnerPlayerID){
        terceiroLugarLabel.setText("Terceiro lugar: ");
        labelJogadorVencedor.setText("jogador #"+ winnerPlayerID +"venceu!");

    }


    public void setApp(App app) {
        this.app = app;
    }

    public EndGameController() {
    // Construtor vazio
    }

    public EndGameController(App app) {
        this.app = app;
    }
    


}
