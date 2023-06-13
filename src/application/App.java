package application;


import gamegui.*;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {

    public static final int panelWidth = 600;
    public static final int panelHeight = 200;

    public static final int boxInitPosX = panelWidth / 4;
    public static final int boxInitPosY = panelHeight / 4;
    public static final int BOX_WIDTH = panelWidth / 2;
    public static final int BOX_HEIGHT = 50;
    public static final int SLIDER_SPEED = 4;
    public static final int CAR_SPEED = 40;
    public static final int CAR_DIST = 30;
    public static final int carXPosition = 100;
    public static final int carYPosition = 115;
    public static final int carWidth = 20;
    public static final int carHeight = 30;

    public MenuGUI menuGUI = new MenuGUI(this);
    public LobbyGUI preGameGUI = new LobbyGUI(this);
    public GameRunningGUI gameRunningGUI = new GameRunningGUI(this);
    public EndGameGUI endGameGUI = new EndGameGUI(this);

    private List<Car> players;

    private int playerID;
    public String nickName, ip, porta;

    public Stage stage;
    private Label statusLabel = new Label("");
    private Label playersLabel = new Label("");
    private Label playerIDLabel = new Label("");

    public GameServer gameServer;
    private ClientSideConnection clientSideConnection;
    public int gameState = 0;

    // 0 -> client
    // 1 -> server
    public boolean isServer = false;

    private Thread listenerLobby, listenerUpdateGame;

    public void createServer(int porta) {

        this.gameState = 1;
        System.out.println("Servidor criado!");
        this.gameServer = new GameServer(porta);
        this.gameServer.acceptConnections();
    }

    public void connectToServer(String ip, int porta) {
        
        App.this.setPlayers(new ArrayList<>());

        if(isServer) {
            App.this.preGameGUI.showServerScreen();
            while(this.gameServer == null) System.out.println("esperando");
        } else {
            App.this.preGameGUI.showClientScreen();
        }

        this.gameState = 1;
        this.clientSideConnection = new ClientSideConnection(ip, porta);
        this.clientSideConnection.sendMessage("nickname = " + nickName);
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

        this.stage = primaryStage;

        this.gameState = 0;
        this.menuGUI.showMenu();
        
        this.stage.show();
        

    }

    private void updateLobbyLabels() {

        String statusText = isServer ? ("está esperando por mais players\nno ip " + this.ip + " e na porta " + this.porta) : "está esperando o host iniciar o jogo";
        this.statusLabel.setText("Status: " + this.nickName + " " + statusText);
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

                    if (playerID == 0) {
                        App.this.playerID = Integer.parseInt(string.split(" ")[6]);
                        App.this.getClientSideConnection().playerID = App.this.playerID;
                    }

                    for (int i = players.size(); i < Integer.parseInt(string.split(" ")[3]); i++) {
                        players.add(i, new Car(string.split(" ")[7 + i]));
                        
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
                        players.get(playerToMove - 1).setCurrentX(Math.min(App.panelWidth - App.carXPosition - App.carWidth - 5
                            ,players.get(playerToMove - 1).getX() + (attack) * CAR_SPEED));

                        //verifica se o jogador chegou na linha de chegada e encerra o jogo
                        if ((players.get(playerToMove - 1).getCurrentX()) >= App.panelWidth - App.carXPosition - App.carWidth - 5 ){
                            
                            App.this.endGameGUI.show();
                            App.this.endGameGUI.setFirstPlace(players.get(playerToMove - 1).nickName + "");
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }
}