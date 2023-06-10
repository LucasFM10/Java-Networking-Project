package application;

import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AppCopy extends Application {

    private Scene gameMenu, gameRunning, gameEnd;
    private Pane paneGameRunning, paneGameMenu, paneGameEnd;

    private static final int panelWidth = 300;
    private static final int panelHeight = 200;

    private static final int boxInitPosX = panelWidth / 4;
    private static final int boxInitPosY = panelHeight / 4;
    private static final int BOX_WIDTH = panelWidth / 2;
    private static final int BOX_HEIGHT = 50;
    private static final int SLIDER_SPEED = 4;
    public static final int CAR_SPEED = 40;
    public static final int CAR_DIST = 30;
    private static int carXPosition = 50;
    private static int carYPosition = 115;
    private static int carWidth = 20;
    private static int carHeight = 30;

    private Car players[];
    private int numPlayers, playerID;

    private ClientSideConnection clientSideConnection;

    private int gameState = 0;

    private Thread listener;
    public void createServer(int porta) {
        System.out.println("Escreva quantos players são!");
        Scanner scan = new Scanner(System.in);
        int nP = scan.nextInt();
        scan.close();
        GameServer gameServer = new GameServer(porta);
        gameServer.acceptConnections();
    }

    public void connectToServer(String ip, int porta) {

        this.clientSideConnection = new ClientSideConnection(ip, porta);
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                    while(true){
                        String string;
                        if((string = clientSideConnection.receiveMessage())!=null) {
                        System.out.println(string);
                        int playerToMove;
                        double attack;
                        playerToMove = Integer.parseInt(string.split(" ")[2]);
                        attack = Double.parseDouble(string.split(" ")[3]);
                        System.out.println("Player #" + playerToMove + " hit " + attack + " attack value.");
                        players[playerToMove - 1].setCurrentX(players[playerToMove - 1].getX() + (attack) * CAR_SPEED);
                        }
                    }
                
            }
        });
    }

    public void connectToServer() {

        this.clientSideConnection = new ClientSideConnection("localhost", 8888);
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                    while(true){
                        String string;
                        if((string = clientSideConnection.receiveMessage())!=null) {
                        System.out.println(string);
                        int playerToMove;
                        double attack;
                        playerToMove = Integer.parseInt(string.split(" ")[2]);
                        attack = Double.parseDouble(string.split(" ")[3]);
                        System.out.println("Player #" + playerToMove + " hit " + attack + " attack value.");
                        players[playerToMove - 1].setCurrentX(players[playerToMove - 1].getX() + (attack) * CAR_SPEED);
                        }
                    }
                
            }
        });

        // esperando cliente conectar no servidor
        while(true) {
            String string;
            if ((string = clientSideConnection.receiveMessage()) != null) {
                numPlayers = Integer.parseInt(string.split(" ")[3]);
                playerID = Integer.parseInt(string.split(" ")[6]);
                System.out.println(string);
                gameState = 1;
                break;
            }
        }

        // iniciando listener para receber mensagens do servidor
        listener.start();
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Undertale Fight");
        
        // Creating gameRunning scene
        paneGameRunning = new Pane();
        gameRunning = new Scene(paneGameRunning, panelWidth, panelHeight);

        // Create the menu scene
        paneGameMenu = new Pane();
        gameMenu = new Scene(paneGameMenu, 100, 100);
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> primaryStage.setScene(gameRunning));
        startButton.setMinWidth(100);
        paneGameMenu.getChildren().addAll(startButton);

        // Create the end scene
        Button endButton = new Button("Menu");
        paneGameEnd = new Pane();
        gameEnd = new Scene(paneGameEnd, 100, 100);
        endButton.setOnAction(e -> {
            primaryStage.setScene(gameMenu);
            for(int i = 0; i < numPlayers; i++) {
                players[i].setX(0);
            }
        });
        paneGameEnd.getChildren().addAll(endButton);

        connectToServer();

        // Creating gameRunning scene
        paneGameRunning = new Pane();
        gameRunning = new Scene(paneGameRunning, panelWidth, panelHeight);

        // Draw start line
        Rectangle startLine = new Rectangle(carXPosition, panelHeight - carYPosition + 20, 10, BOX_HEIGHT);
        startLine.setFill(Color.BLUE);
        startLine.setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(startLine);

        // Draw finish line
        Rectangle finish = new Rectangle(panelWidth - carXPosition - 10, panelHeight - carYPosition + 20, 10, BOX_HEIGHT);
        finish.setFill(Color.RED);
        finish.setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(finish);

        // Draw players
        players = new Car[numPlayers];
        for(int i = 0; i < numPlayers; i++) {
            players[i] = new Car(new Image("/car" + ((i % 3) + 1) + ".png"), CAR_SPEED, carXPosition, carYPosition + i * CAR_DIST, carWidth, carHeight);
            paneGameRunning.getChildren().addAll(players[i]);
        }

        Stop[] stops = new Stop[] { new Stop(0, Color.RED), new Stop(0.75, Color.YELLOW), new Stop(1, Color.GREEN)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0.5, 0, true, CycleMethod.REFLECT, stops);

        // Draw the box
        Box box = new Box(boxInitPosX, boxInitPosY, BOX_WIDTH, BOX_HEIGHT, SLIDER_SPEED);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.getSlider().setStroke(Color.BLACK);
        box.setFill(lg1);
        paneGameRunning.getChildren().addAll(box, box.getSlider());

        gameRunning.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getText().equals("x")) {
                    // print the attack value based on the position of the slider
                    double attackValue = Math.abs(((double) box.getSlider().getEndX() - boxInitPosX) - BOX_WIDTH / 2) / (BOX_WIDTH / 2);
                    // System.out.println("Attack power: " + (1 - attackValue));

                    // move the player based on the attack value
                    // players[playerID].setX(players[playerID].getX() + (0.5 - attackValue) * CAR_SPEED);
                    clientSideConnection.sendMessage(playerID + " " + (0.5 - attackValue));
                }
                
            }
        });

        // Animation to move the slider
        AnimationTimer slider = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate <= 17_000_000) {
                    return;
                }
                lastUpdate = now ;
                if (box.isMovingRight()) {
                    box.setSliderPosX(box.getSliderPosX() + SLIDER_SPEED);
                    if (box.getSlider().getEndX() >= BOX_WIDTH + boxInitPosX) {
                        box.setMovingRight(false);
                    }
                } else {
                    box.setSliderPosX(box.getSliderPosX()- SLIDER_SPEED);
                    if (box.getSlider().getEndX() <= + boxInitPosX) {
                        box.setMovingRight(true);
                    }
                }
                
                for(int i = 0; i < numPlayers; i++) {
                    players[i].setX(players[i].getCurrentX());
                }
                
            }
        };

        slider.start();

        primaryStage.setScene(gameRunning);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}