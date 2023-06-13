package gamegui;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

public class Box extends Rectangle{
    
    private boolean movingRight = true;
    private Line slider;
    double speed, sliderPosX;

    public Line getSlider() {
        return slider;
    }

    public void setSlider(Line slider) {
        this.slider = slider;
    }

    public double getSliderPosX() {
        return sliderPosX;
    }

    public void setSliderPosX(double sliderPosX) {
        this.sliderPosX = sliderPosX;
        this.getSlider().setStartX(sliderPosX);
        this.getSlider().setEndX(sliderPosX);
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Box(int initX, int initY, int width, int height, int speed) {
        super(initX, initY, width, height);
        this.speed = speed;
        this.sliderPosX = initX + width/2;
        this.slider = new Line(this.sliderPosX, initY, this.sliderPosX, initY + height);
    }
}
