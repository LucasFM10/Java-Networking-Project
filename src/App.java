import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class App extends Application {

    private Scene gameMenu, gameRunning, gameEnd;
    private Pane paneGameRunning, paneGameMenu, paneGameEnd;
    private Stage stage;

    private Label statusLabel;
    private Label playersLabel;

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

    private List<Car> players;
    private int playerID;

    private GameServer gameServer;
    private ClientSideConnection clientSideConnection;

    private int gameState = 0;

    // 0 -> undefined
    // 1 -> server
    // 2 -> client
    private int appStatus = 0;

    private Thread listener, listenerToStartGame;

    public void listernerToStartGame() {

        this.listenerToStartGame = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("WAITING FOR USER TO DECIDE IF HE IS A CLIENT OR A SERVER");
                while(true) {
                    if (App.this.appStatus != 0) break;
                }
                if(App.this.appStatus == 1) {
                    System.out.println("sou um servidor!");
                } else {
                    System.out.println("sou um cliente!");
                }
            }
        });

    }
    
    public void createServer(int porta) {
        this.gameServer = new GameServer(porta);
        this.gameServer.acceptConnections();
    }

    public void connectToServer(String ip, int porta) {

        this.clientSideConnection = new ClientSideConnection(ip, porta);
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                App.this.players = new ArrayList<>();
                while(true) {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            App.this.updatePreGameLabels();
                        }
                        
                    });
                    String string;
                    if((string = clientSideConnection.receiveMessage())!=null) {
                        System.out.println(string);

                        if(gameState == 1) {
                            
                            int playerToMove;
                            double attack;
                            playerToMove = Integer.parseInt(string.split(" ")[2]);
                            attack = Double.parseDouble(string.split(" ")[3]);
                            System.out.println("Player #" + playerToMove + " hit " + attack + " attack value.");
                            players.get(playerToMove - 1).setCurrentX(players.get(playerToMove - 1).getX() + (attack) * CAR_SPEED);
                        }

                        if(string.split(" ")[3].equals("não")) {

                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    
                                    App.this.updateGUIgameRunning();
                                }
                                
                            });
                            gameState = 1;
                        } else if(gameState == 0){

                            for(int i = players.size(); i < Integer.parseInt(string.split(" ")[3]); i++) {
                                players.add(i, new Car());
                            }

                            if(playerID==0){playerID = Integer.parseInt(string.split(" ")[6]);}
                            System.out.println(string);
                        }
                    }
                }
            }
        });
    }   

    @Override
    public void start(Stage primaryStage) {

        App.this.stage = primaryStage;

        App.this.stage.setTitle("Undertale Fight");

        App.this.stage.setScene(new Scene(guiMenu(), 400, 300));
        App.this.stage.show();


        listernerToStartGame();
        listenerToStartGame.start();

       

        // connectToServer();
    }

    private void updatePreGameLabels() {

        if(this.gameServer == null && this.clientSideConnection == null) {return;}

        
        String statusText = appStatus == 1 ? "Sou um servidor e estou " + (this.gameServer.isWaitingConnections() ? "esperando mais players" : "esperando host iniciar o jogo") : "Esperando host iniciar o jogo";
        this.statusLabel.setText("Status do Servidor: " + statusText);
        this.playersLabel.setText("Número de Jogadores: " + players.size());
    }

    private VBox guiPreGameServer() {

        this.statusLabel = new Label("oi");
        this.playersLabel = new Label("oi");

        VBox waitingConec = new VBox(10);
        waitingConec.setPadding(new Insets(20, 20, 20, 20));
        waitingConec.getChildren().addAll(this.statusLabel, this.playersLabel);

        Button btn = new Button("Iniciar Jogo");

        btn.setOnAction(e -> {
            System.out.println("Jogo iniciado");
            this.gameServer.endConnections();
        });

        waitingConec.getChildren().addAll(btn);

        return waitingConec;
    }

    private VBox guiPreGameClient() {

        this.statusLabel = new Label();
        this.playersLabel = new Label();


        VBox waitingConec = new VBox(10);
        waitingConec.setPadding(new Insets(20, 20, 20, 20));
        waitingConec.getChildren().addAll(this.statusLabel, this.playersLabel);

        return waitingConec;
    }

    private VBox guiMenu() {

        VBox vBox = new VBox();

        // Título do jogo
        Text titleText = new Text("Corrida dos Unicórnios");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Botões
        Button conectarButton = new Button("Conectar\nao Servidor");
        conectarButton.setPrefSize(100, 100);
        conectarButton.setWrapText(true);
        conectarButton.setTextAlignment(TextAlignment.CENTER);
        conectarButton.setOnAction(e -> exibirDialogoConectar(e));

        Button criarServidorButton = new Button("Criar\nServidor");
        criarServidorButton.setPrefSize(100, 100);
        criarServidorButton.setWrapText(true);
        criarServidorButton.setTextAlignment(TextAlignment.CENTER);
        criarServidorButton.setOnAction(e -> exibirDialogoCriarServidor(e));

        // Layout dos botões
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(conectarButton, criarServidorButton);

        // Layout principal
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(titleText, buttonBox);

        return vBox;
    }

    public void updateGUIgameRunning() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                App.this.stage.setScene(guiGameRunning());

            }

        });
    }

    public Scene guiGameRunning() {

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
        for(int i = 0; i < players.size(); i++) {
            players.set(i, new Car(new Image("/car" + ((i % 3) + 1) + ".png"), CAR_SPEED, carXPosition, carYPosition + i * CAR_DIST, carWidth, carHeight));
            paneGameRunning.getChildren().addAll(players.get(i));
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
                
                for(int i = 0; i < players.size(); i++) {
                    players.get(i).setX(players.get(i).getCurrentX());
                }
                
            }
        };

        slider.start();

        return gameRunning;
    }

    public void exibirDialogoCriarServidor(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField portaTextField = new TextField();
        portaTextField.setPrefWidth(100); // Definindo a largura preferencial do TextField

        gridPane.add(new Text("Porta:"), 0, 0);
        gridPane.add(portaTextField, 1, 0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Criar Servidor");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String porta = portaTextField.getText().trim();

                if (validarPorta(porta)) {
                    if (verificarPortaDisponivel(Integer.parseInt(porta))) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                App.this.appStatus = 1;
                                App.this.createServer(Integer.parseInt(porta));
                            }
                        });
                        t.start();

                        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(guiPreGameServer(), 500, 500));
                    } else {
                        exibirMensagemErro("A porta não está disponível para uso.");
                    }
                } else {
                    exibirMensagemErro("Digite uma porta válida.");
                    exibirDialogoCriarServidor(e); // Chamada recursiva para exibir o diálogo novamente
                }
            }
        });
    }

    private void exibirDialogoConectar(ActionEvent e) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField ipTextField = new TextField();
        TextField portaTextField = new TextField();

        gridPane.add(new Text("IP:"), 0, 0);
        gridPane.add(ipTextField, 1, 0);
        gridPane.add(new Text("Porta:"), 0, 1);
        gridPane.add(portaTextField, 1, 1);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conectar ao Servidor");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String ip = ipTextField.getText().trim();
                String porta = portaTextField.getText().trim();

                if (validarIP(ip) && validarPorta(porta)) {

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            App.this.appStatus = 2;
                            App.this.connectToServer(ip, Integer.parseInt(porta));
                            listener.start();
                        }
                    });
                    t.start();
                    Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(guiPreGameClient(), 500, 500));

                    
                    System.out.println("Conectar ao servidor: IP=" + ip + ", Porta=" + porta);
                } else {
                    exibirMensagemErro("Digite um IP e uma porta válidos.");
                    exibirDialogoConectar(e); // Chamada recursiva para exibir o diálogo novamente
                }
            }
        });
    }

    private boolean validarIP(String ip) {
        // Utilize uma expressão regular para verificar se o IP é válido
        String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(ipRegex);
    }

    private boolean validarPorta(String porta) {
        // Verificar se a porta é um número válido
        try {
            int portaInt = Integer.parseInt(porta);
            return portaInt >= 0 && portaInt <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean verificarPortaDisponivel(int porta) {
        try {
            // Tenta criar um ServerSocket na porta especificada
            ServerSocket serverSocket = new ServerSocket(porta);
            serverSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}