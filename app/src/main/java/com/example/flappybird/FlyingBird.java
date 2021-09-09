package com.example.flappybird;

public class FlyingBird {
    private int birdX;
    private int birdY;
    private int currentFrame;
    private int velocity;
    public static int maximumFrame;
    public FlyingBird() {
        birdX=AppHolder.SCREEN_WIDTH_X/2-AppHolder.getBitmapControl().getBirdWidth()/2;
        birdY=AppHolder.SCREEN_HEIGHT_Y/2-AppHolder.getBitmapControl().getBirdHeight()/2;
        currentFrame=0;
        maximumFrame=2;

    }

    public int getX() {
        return birdX;
    }

    public void setX(int birdX) {
        this.birdX = birdX;
    }

    public int getY() {
        return birdY;
    }

    public void setY(int birdY) {
        this.birdY = birdY;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
