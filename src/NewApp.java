import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create first scene
        Button switchButton = new Button("Switch Scene");
        VBox firstPane = new VBox(switchButton);
        Scene firstScene = new Scene(firstPane, 200, 200);

        // Set action on button
        switchButton.setOnAction(e -> {
            switchScene(primaryStage);
        });

        primaryStage.setScene(firstScene);
        primaryStage.show();
    }

    private void switchScene(Stage stage) {
        Platform.runLater(() -> {
            VBox secondPane = new VBox();
            secondPane.getChildren().add(new Button("New Scene Button"));
            Scene secondScene = new Scene(secondPane, 200, 200);
            stage.setScene(secondScene);  // Use the passed stage reference here.
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
