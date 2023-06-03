import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {

    private Scene gameRunning;
    private Pane paneGameRunning;

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

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Undertale Fight");

        this.clientSideConnection = new ClientSideConnection();

        while(true) {
            String string;
            if ((string = clientSideConnection.receiveMessage()) != null) {
                numPlayers = Integer.parseInt(string.substring(28, 29));
                playerID = Integer.parseInt(string.substring(40, 41));
                System.out.println(string);
                gameState = 1;
                break;
            }
        }

        // Thread t = new Thread(new Runnable() {
        //     @Override
        //     public void run() {
        //         if(gameState == 1) {
        //             System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
        //             while(true) {
        //                 System.out.println("OOOOOOOOOOOOOOOOO");
        //                 String string = clientSideConnection.receiveMessage();
        //                 int playerToMove;
        //                 double attack;
        //                 playerToMove = Integer.parseInt(string.substring(13, 14));
        //                 attack = Double.parseDouble(string.substring(15));
        //                 System.out.println("Player #" + playerToMove + " hit " + attack + " attack value.");
        //                 players[playerToMove].setX(players[playerToMove].getX() + (0.5 - attack) * CAR_SPEED);
        //             }
        //         }
        //     }
        // });
        // t.start();

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
            players[i] = new Car(new Image("/car" + (i + 1) + ".png"), CAR_SPEED, carXPosition, carYPosition + i * CAR_DIST, carWidth, carHeight);
            paneGameRunning.getChildren().addAll(players[i]);
        }

        // Draw the box
        Box box = new Box(boxInitPosX, boxInitPosY, BOX_WIDTH, BOX_HEIGHT, SLIDER_SPEED);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.getSlider().setStroke(Color.BLACK);
        paneGameRunning.getChildren().addAll(box, box.getSlider());

        gameRunning.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getText().equals("x")) {
                    // print the attack value based on the position of the slider
                    double attackValue = Math.abs(((double) box.getSlider().getEndX() - boxInitPosX) - BOX_WIDTH / 2) / (BOX_WIDTH / 2);
                    // System.out.println("Attack power: " + (1 - attackValue));

                    // move the player1 based on the attack value
                    // players[playerID].setX(players[playerID].getX() + (0.5 - attackValue) * CAR_SPEED);
                    clientSideConnection.sendMessage(playerID + " " + (1 - attackValue));
                }
                
            }
        });

        // Animation to move the slider
        new AnimationTimer() {
            @Override
            public void handle(long now) {
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
                
            }
        }.start();

        primaryStage.setScene(gameRunning);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}