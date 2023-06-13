package gamegui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car extends ImageView {

    double speed;
    double currentX, currentY;
    public String nickName;
    
    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public Car(String nickName) {
        this.nickName = nickName;
    }

    public Car(Image image, double speed, double positionX, double positionY, double width, double height, String nickName) {
        this.setImage(image);
        this.speed = speed;
        this.setX(positionX);
        this.setY(positionY);
        this.setFitWidth(width);
        this.setFitHeight(height);
        this.currentX = positionX;
        this.currentY = positionX;
        this.nickName = nickName;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
