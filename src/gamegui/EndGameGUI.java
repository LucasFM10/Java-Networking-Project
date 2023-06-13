package gamegui;

import application.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EndGameGUI {

    App app;

    private Label firstPlaceLabel;

    public EndGameGUI(App app){
        this.app = app;
    }

    public void show() {
        firstPlaceLabel = new Label("1st Place: -");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(firstPlaceLabel);


        Button backButton = new Button("Voltar ao Menu");
        backButton.setOnAction(event -> {

            EndGameGUI.this.app.endGameGUI.voltarAoMenu();

        });

            System.out.println("Voltando ao menu...");
        

        root.getChildren().add(backButton);

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                EndGameGUI.this.app.getStage().setScene(new Scene(root, 400, 300));
            }
        });

    }

    public void voltarAoMenu(){


            if(this.app.isServer)
                this.app.getGameServer().closeConnection();
            else{
                this.app.getClientSideConnection().closeConnection();
            }

            App newApp = new App();

            EndGameGUI.this.app.menuGUI = new MenuGUI(newApp);
            EndGameGUI.this.app.preGameGUI = new LobbyGUI(newApp);
            EndGameGUI.this.app.gameRunningGUI = new GameRunningGUI(newApp);
            EndGameGUI.this.app.endGameGUI = new EndGameGUI(newApp);

            newApp.start(EndGameGUI.this.app.stage);
 
    }

    public void setFirstPlace(String playerName) {
        firstPlaceLabel.setText("O vencedor é: " + playerName);
    }

}