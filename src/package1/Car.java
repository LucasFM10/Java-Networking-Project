package package1;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car extends ImageView {

    double speed;
    
    public Car() {

    }

    public Car(Image image, double speed, double positionX, double positionY, double width, double height) {
        this.setImage(image);
        this.speed = speed;
        this.setX(positionX);
        this.setY(positionY);
        this.setFitWidth(width);
        this.setFitHeight(height);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}