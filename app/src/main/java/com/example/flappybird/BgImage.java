package com.example.flappybird;

public class BgImage {
    private int image_X_background;
    private int image_Y_background;
    private final int backgroundSpeed;

    public BgImage() {
        image_X_background = 0;
        image_Y_background = 0;
        backgroundSpeed = 4;
    }

    public int getX() {
        return image_X_background;
    }

    public void setX(int image_X_background) {
        this.image_X_background = image_X_background;
    }

    public int getY() {
        return image_Y_background;
    }

    public void setY(int image_Y_background) {
        this.image_Y_background = image_Y_background;
    }

    public int getVelocity() {
        return backgroundSpeed;
    }
}
