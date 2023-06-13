package gamegui;

import application.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndGameGUI {

    App app;

    private Label firstPlaceLabel;
    private Label secondPlaceLabel;
    private Label thirdPlaceLabel;

    public EndGameGUI(App app){
        this.app = app;
    }

    public void show() {
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

            EndGameGUI.this.app.endGameGUI.voltarAoMenu();

        });

            System.out.println("Voltando ao menu...");
        

        root.getChildren().add(backButton);

        //primaryStage.setScene(scene);
        //primaryStage.setTitle("End Game");
        //primaryStage.show();

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                EndGameGUI.this.app.getStage().setScene(new Scene(root, 400, 300));
                //GameServer.getServerSocket().close;
            }
        });


        //this.app.getStage().setScene(new Scene(root, 400, 300));
    }

    
    public void voltarAoMenu(){

            if(this.app.isServer)
                this.app.getGameServer().closeConnection(this.app.gameServer.esteServerSideConnection);
            else{
                this.app.getClientSideConnection().closeConnection();
            }

            // App newApp = new App();

            //EndGameGUI.this.app.stage = appStage;
            EndGameGUI.this.app.menuGUI = new MenuGUI(EndGameGUI.this.app);
            EndGameGUI.this.app.preGameGUI = new LobbyGUI(EndGameGUI.this.app);
            EndGameGUI.this.app.gameRunningGUI = new GameRunningGUI(EndGameGUI.this.app);
            EndGameGUI.this.app.endGameGUI = new EndGameGUI(EndGameGUI.this.app);

            EndGameGUI.this.app.isServer = false;
            
            EndGameGUI.this.app.gameState = 0;
            EndGameGUI.this.app.menuGUI.showMenu();
            EndGameGUI.this.app.stage.show();

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