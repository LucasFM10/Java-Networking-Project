package application;


import gamegui.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controllers.MenuSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    private MenuGUI menuGUI = new MenuGUI(this);
    private PreGameGUI preGameGUI = new PreGameGUI(this);
    private GameRunningGUI gameRunningGUI = new GameRunningGUI(this);

    private Scene gameMenu, gameRunning, gameEnd;
    private Pane paneGameRunning, paneGameMenu, paneGameEnd;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String textLabel) {
        this.statusLabel = new Label(textLabel);
    }

    public Label getPlayersLabel() {
        return playersLabel;
    }

    public MenuGUI getMenuGUI() {
        return menuGUI;
    }

    public void setMenuGUI(MenuGUI menuGUI) {
        this.menuGUI = menuGUI;
    }

    public PreGameGUI getPreGameGUI() {
        return preGameGUI;
    }

    public void setPreGameGUI(PreGameGUI preGameGUI) {
        this.preGameGUI = preGameGUI;
    }

    public Scene getGameMenu() {
        return gameMenu;
    }

    public void setGameMenu(Scene gameMenu) {
        this.gameMenu = gameMenu;
    }

    public Scene getGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(Scene gameRunning) {
        this.gameRunning = gameRunning;
    }

    public Scene getGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(Scene gameEnd) {
        this.gameEnd = gameEnd;
    }

    public Pane getPaneGameRunning() {
        return paneGameRunning;
    }

    public void setPaneGameRunning(Pane paneGameRunning) {
        this.paneGameRunning = paneGameRunning;
    }

    public Pane getPaneGameMenu() {
        return paneGameMenu;
    }

    public void setPaneGameMenu(Pane paneGameMenu) {
        this.paneGameMenu = paneGameMenu;
    }

    public Pane getPaneGameEnd() {
        return paneGameEnd;
    }

    public void setPaneGameEnd(Pane paneGameEnd) {
        this.paneGameEnd = paneGameEnd;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setPlayersLabel(Label playersLabel) {
        this.playersLabel = playersLabel;
    }

    public static int getPanelwidth() {
        return panelWidth;
    }

    public static int getPanelheight() {
        return panelHeight;
    }

    public static int getBoxinitposx() {
        return boxInitPosX;
    }

    public static int getBoxinitposy() {
        return boxInitPosY;
    }

    public static int getBoxWidth() {
        return BOX_WIDTH;
    }

    public static int getBoxHeight() {
        return BOX_HEIGHT;
    }

    public static int getSliderSpeed() {
        return SLIDER_SPEED;
    }

    public static int getCarSpeed() {
        return CAR_SPEED;
    }

    public static int getCarDist() {
        return CAR_DIST;
    }

    public static int getCarXPosition() {
        return carXPosition;
    }

    public static void setCarXPosition(int carXPosition) {
        App.carXPosition = carXPosition;
    }

    public static int getCarYPosition() {
        return carYPosition;
    }

    public static void setCarYPosition(int carYPosition) {
        App.carYPosition = carYPosition;
    }

    public static int getCarWidth() {
        return carWidth;
    }

    public static void setCarWidth(int carWidth) {
        App.carWidth = carWidth;
    }

    public static int getCarHeight() {
        return carHeight;
    }

    public static void setCarHeight(int carHeight) {
        App.carHeight = carHeight;
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

    public Thread getlistenerLobby() {
        return listenerLobby;
    }

    public void setlistenerLobby(Thread listenerLobby) {
        this.listenerLobby = listenerLobby;
    }

    public Thread getlistenerUpdateGame() {
        return listenerUpdateGame;
    }

    public void setlistenerUpdateGame(Thread listenerUpdateGame) {
        this.listenerUpdateGame = listenerUpdateGame;
    }

    public void setPlayersLabel(String textLabel) {
        this.playersLabel = new Label(textLabel);
    }

    private Label playersLabel, statusLabel, playerIDLabel;

    public Label getPlayerIDLabel() {
        return playerIDLabel;
    }

    public void setPlayerIDLabel(String string) {
        this.playerIDLabel = new Label(string);
    }

    public static final int panelWidth = 300;
    public static final int panelHeight = 200;

    public static final int boxInitPosX = panelWidth / 4;
    public static final int boxInitPosY = panelHeight / 4;
    public static final int BOX_WIDTH = panelWidth / 2;
    public static final int BOX_HEIGHT = 50;
    public static final int SLIDER_SPEED = 4;
    public static final int CAR_SPEED = 40;
    public static final int CAR_DIST = 30;
    public static int carXPosition = 50;
    public static int carYPosition = 115;
    public static int carWidth = 20;
    public static int carHeight = 30;

    private List<Car> players;
    private int playerID;

    private GameServer gameServer;
    private ClientSideConnection clientSideConnection;

    private int gameState = 0;

    // 0 -> undefined
    // 1 -> server
    // 2 -> client
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

        if(isServer) {
            this.preGameGUI.showServerScreen();
        } else {
            this.preGameGUI.showClientScreen();
        }
        this.gameState = 1;
        this.clientSideConnection = new ClientSideConnection(ip, porta);
        this.listenerLobby = new Thread(new listenerLobbyRunner());
        this.listenerUpdateGame = new Thread(new listenerUpdateGameRunner());
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
        // this.menuGUI.showMenu();

        

        App.this.setScene("/scenes/MenuScene.fxml");
        this.stage.show();
        

    }

    public void setScene(String sceneName) {
        try {
            Parent root;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));
            root = fxmlLoader.load();

            MenuSceneController controller = fxmlLoader.getController();
            controller.setApp(this);

            Scene scene = new Scene(root);

            this.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void updatePreGameLabels() {

        String statusText = isServer ? ""
                + (this.gameServer.isWaitingConnections() ? "Esperando mais players" : "Esperando host iniciar o jogo")
                : "Esperando host iniciar o jogo";
        this.statusLabel.setText("Status: " + statusText);
        this.playersLabel.setText("Número de Jogadores: " + this.players.size());
        this.playerIDLabel.setText("Player: #" + this.playerID);
    }

    private class listenerLobbyRunner implements Runnable {

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
                                App.this.updatePreGameLabels();
                            }
                            
                        });
                    }
                }
            }

            App.this.gameRunningGUI.show();
            App.this.listenerUpdateGame.start();
        }
    }

    private class listenerUpdateGameRunner implements Runnable {

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
                        players.get(playerToMove - 1).setCurrentX(players.get(playerToMove - 1).getX() + (attack) * CAR_SPEED);
                    } else {
                        break;
                    }
                }
                
            }

            App.this.gameRunningGUI.show();
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}