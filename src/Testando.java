import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Testando extends Application{

    @Override
    public void start(Stage arg0) throws Exception {
        App app = new App();
        Pane guiMenuPane = new GUIMenu(app);
        arg0.setScene(new Scene(guiMenuPane, 400,300));
        
        arg0.setTitle("Undertale Fight");
        arg0.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
