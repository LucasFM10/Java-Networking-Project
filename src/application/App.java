package application;


import gamegui.*;
import controllers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {

    public static final int panelWidth = 300;
    public static final int panelHeight = 200;

    public static final int boxInitPosX = panelWidth / 4;
    public static final int boxInitPosY = panelHeight / 4;
    public static final int BOX_WIDTH = panelWidth / 2;
    public static final int BOX_HEIGHT = 50;
    public static final int SLIDER_SPEED = 4;
    public static final int CAR_SPEED = 40;
    public static final int CAR_DIST = 30;
    public static final int carXPosition = 50;
    public static final int carYPosition = 115;
    public static final int carWidth = 20;
    public static final int carHeight = 30;

    private MenuGUI menuGUI = new MenuGUI(this);
    private LobbyGUI preGameGUI = new LobbyGUI(this);
    private GameRunningGUI gameRunningGUI = new GameRunningGUI(this);
    private EndGameGUI endGameGUI = new EndGameGUI(this);

    private Stage stage;
    private Label statusLabel = new Label("");
    private Label playersLabel = new Label("");
    private Label playerIDLabel = new Label("");
    private Label servidorIPLabel = new Label("");

    private GameServer gameServer;
    private ClientSideConnection clientSideConnection;
    private int gameState = 0;

    // 0 -> client
    // 1 -> server
    private boolean isServer = false;

    private Thread listenerLobby, listenerUpdateGame;

    public void createServer(int porta) {

        this.gameState = 1;
        System.out.println("Servidor criado!");
        this.gameServer = new GameServer(porta);
        this.gameServer.acceptConnections();
    }

    public void connectToServer(String ip, int porta) {
        
        App.this.setPlayers(new ArrayList<>());

        // if(isServer) {
        //     App.this.preGameGUI.showServerScreen();
        // } else {
        //     App.this.preGameGUI.showClientScreen();
        // }
        this.setSceneFXML("Lobby");
        this.gameState = 1;
        this.clientSideConnection = new ClientSideConnection(ip, porta);
        this.listenerLobby = new Thread(new listenerLobby());
        this.listenerUpdateGame = new Thread(new listenerUpdateGame());
        listenerLobby.start();
    }

    @Override
    public void start(Stage primaryStage) {
    

        // gameState = 0 -> jogo ainda não começou, não se sabe se o usuário quer criar
        // um servidor ou conectar um usuário
        // gameState = 1 -> jogo ainda não começou, estamos esperando os clientes
        // conectarem e o servidor decidir iniciar o jogo.
        // gameState = 2 -> jogo está rolando! que vença o melhor!
        // gameState = 3 -> jogo terminou, esperando ação do servidor (reiniciar o jogo?
        // encerrar servidor? retornar para menu inicial?)

        this.stage = primaryStage;

        this.gameState = 0;
        //this.menuGUI.showMenu();

        

        App.this.setSceneFXML("Menu");
        
        this.stage.show();
        

    }

    public void setSceneFXML(String sceneName) {
        try {
            Parent root;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/" + sceneName + "Scene.fxml"));
            root = fxmlLoader.load();

            switch(sceneName) {
                case "Menu":
                    MenuController menuController = fxmlLoader.getController();
                    menuController.setApp(this);
                    break;
                case "Lobby":
                    LobbyController lobbyController = fxmlLoader.getController();
                    lobbyController.setApp(this);
                    break;
                case "EndGame":
                    EndGameController controllerEndGameGUI = fxmlLoader.getController();
                    controllerEndGameGUI.setApp(this);
                    break;
            }

            Scene scene = new Scene(root);
            
            Platform.runLater(() -> {
                this.getStage().setScene(scene);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateLobbyLabels() {

        String statusText = isServer ? ("Esperando mais players") : "Esperando host iniciar o jogo";
        this.statusLabel.setText("Status: " + statusText);
        this.playersLabel.setText("Número de Jogadores: " + this.players.size());
        this.playerIDLabel.setText("Player: #" + this.playerID);
    }

    private class listenerLobby implements Runnable {

        @Override
        public void run() {
            
            while (true) {

                String string;
                if ((string = clientSideConnection.receiveMessage()) != null) {

                    if(string.split(" ")[3].equals("não")) {

                        App.this.setGameState(2);
                        break;
                    }

                    System.out.println(string);

                    if (playerID == 0) {
                        playerID = Integer.parseInt(string.split(" ")[6]);
                    }

                    for (int i = players.size(); i < Integer.parseInt(string.split(" ")[3]); i++) {
                        players.add(i, new Car());
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                App.this.updateLobbyLabels();
                            }
                            
                        });
                    }
                }
            }

            App.this.gameRunningGUI.show();
            // App.this.setSceneFXML("GameRunning");
            App.this.listenerUpdateGame.start();
        }
    }

    private class listenerUpdateGame implements Runnable {

        @Override
        public void run() {
                
            while (true) {
                String string;
                if((string = clientSideConnection.receiveMessage())!=null) {
                    System.out.println(string);

                    if(gameState == 2) {
                        
                        int playerToMove;
                        double attack;
                        playerToMove = Integer.parseInt(string.split(" ")[2]);
                        attack = Double.parseDouble(string.split(" ")[3]);
                        System.out.println("Player #" + playerToMove + " hit " + attack + " attack value.");
                        players.get(playerToMove - 1).setCurrentX(Math.min(250 ,players.get(playerToMove - 1).getX() + (attack) * CAR_SPEED));
                        //verifica se o jogador chegou na linha de chegada e encerra o jogo
                        if ((players.get(playerToMove - 1).currentX) >= 250 ){
                            //setSceneFXML("EndGame");
                            //this.app.endGameGUI.show();
                        }
                        
                    } else {
                        break;
                    }
                }
            }
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String text) {
        this.statusLabel = new Label(text);
    }

    public Label getPlayersLabel() {
        return playersLabel;
    }

    public void setPlayersLabel(String text) {
        this.playersLabel = new Label(text);
    }

    public Label getPlayerIDLabel() {
        return playerIDLabel;
    }

    public void setPlayerIDLabel(String text) {
        this.playerIDLabel = new Label(text);
    }

    public Label getServidorIPLabel() {
        return playerIDLabel;
    }

    public void setServidorIPLabel(String text) {
        this.playerIDLabel = new Label(text);
    }

    private List<Car> players;
    private int playerID;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<Car> getPlayers() {
        return players;
    }

    public void setPlayers(List<Car> players) {
        this.players = players;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public ClientSideConnection getClientSideConnection() {
        return clientSideConnection;
    }

    public void setClientSideConnection(ClientSideConnection clientSideConnection) {
        this.clientSideConnection = clientSideConnection;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean isServer) {
        this.isServer = isServer;
    }

    public Thread getListenerLobby() {
        return listenerLobby;
    }

    public void setListenerLobby(Thread listenerLobby) {
        this.listenerLobby = listenerLobby;
    }

    public Thread getListenerUpdateGame() {
        return listenerUpdateGame;
    }

    public void setListenerUpdateGame(Thread listenerUpdateGame) {
        this.listenerUpdateGame = listenerUpdateGame;
    }
    
}