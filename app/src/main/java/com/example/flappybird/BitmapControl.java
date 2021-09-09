package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapControl {
    Bitmap background;

    Bitmap[] selectedFlyingBird;
    Bitmap[] blackFlyingBird;
    Bitmap[] blueFlyingBird;
    Bitmap[] greenFlyingBird;
    Bitmap[] redFlyingBird;
    Bitmap[] pinkFlyingBird;
    Bitmap[] blackHelicopter;
    Bitmap[] greenHelicopter;
    Bitmap[] orangeHelicopter;

    Bitmap upTube;
    Bitmap downTube;
    Bitmap shield;
    Bitmap bomb;
    Bitmap money;
    Bitmap circleShield;


    public BitmapControl(Resources res) {
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = imageScale(background);


        selectedFlyingBird = new Bitmap[3];
        initBitmaps(res);

        initCurrentCharacter(AppHolder.getUser().getCurrentAvatar());

        upTube = BitmapFactory.decodeResource(res, R.drawable.up_tube);
        downTube = BitmapFactory.decodeResource(res, R.drawable.down_tube);

        shield = BitmapFactory.decodeResource(res, R.drawable.shield);
        bomb = BitmapFactory.decodeResource(res, R.drawable.bomb);
        money = BitmapFactory.decodeResource(res, R.drawable.money);

        circleShield = BitmapFactory.decodeResource(res, R.drawable.circle_shield);

    }

    public Bitmap getUpTube() {
        return upTube;
    }

    public Bitmap getDownTube() {
        return downTube;
    }

    public int getTubeWidth() {
        return upTube.getWidth();
    }

    public int getTubeHeight() {
        return upTube.getHeight();
    }

    public Bitmap getBird(int frame) {
        return selectedFlyingBird[frame];
    }

    public int getBirdWidth() {
        return selectedFlyingBird[0].getWidth();
    }

    public int getBirdHeight() {
        return selectedFlyingBird[0].getHeight();
    }

    public Bitmap getBackground() {
        return background;
    }

    public int getBackgroundHeight() {
        return background.getHeight();
    }

    public int getBackgroundWidth() {
        return background.getWidth();
    }

    public int getShieldHeight() {
        return shield.getHeight();
    }

    public int getShieldWidth() {
        return shield.getWidth();
    }

    public int getBombWidth() {
        return bomb.getWidth();
    }

    public int getMoneyWidth() {
        return money.getWidth();
    }

    public Bitmap getShield() {
        return shield;
    }

    public Bitmap getBomb() {
        return bomb;
    }

    public Bitmap getMoney() {
        return money;
    }

    public Bitmap getCircleShield() {
        return circleShield;
    }

    public int getCircleShieldHeight() {
        return circleShield.getHeight();
    }

    public int getCircleShieldWidth() {
        return circleShield.getWidth();
    }

    public Bitmap imageScale(Bitmap bitmap) {
        float width_heightRatio = getBackgroundWidth() / getBackgroundHeight(); // screen proportion
        int bgScaleWidth = (int) width_heightRatio * AppHolder.SCREEN_WIDTH_X;
        return Bitmap.createScaledBitmap(bitmap, bgScaleWidth, AppHolder.SCREEN_HEIGHT_Y, false);
    }

    void initBitmaps(Resources res) {
        blackFlyingBird = new Bitmap[3];
        blueFlyingBird = new Bitmap[3];
        greenFlyingBird = new Bitmap[3];
        redFlyingBird = new Bitmap[3];
        pinkFlyingBird = new Bitmap[3];
        blackHelicopter = new Bitmap[3];
        greenHelicopter = new Bitmap[3];
        orangeHelicopter = new Bitmap[3];

        initCharacter(blackFlyingBird, R.drawable.black_bird1, R.drawable.black_bird2, R.drawable.black_bird3, res);
        initCharacter(blueFlyingBird, R.drawable.blue_bird1, R.drawable.blue_bird2, R.drawable.blue_bird3, res);
        initCharacter(greenFlyingBird, R.drawable.green_bird1, R.drawable.green_bird2, R.drawable.green_bird2, res);
        initCharacter(pinkFlyingBird, R.drawable.pink_bird1, R.drawable.pink_bird2, R.drawable.pink_bird3, res);
        initCharacter(redFlyingBird, R.drawable.red_bird1, R.drawable.red_bird2, R.drawable.red_bird3, res);
        initCharacter(blackHelicopter, R.drawable.black_helicopter1, R.drawable.black_helicopter2, R.drawable.black_helicopter3, res);
        initCharacter(greenHelicopter, R.drawable.green_helicopter1, R.drawable.green_helicopter2, R.drawable.green_helicopter3, res);
        initCharacter(orangeHelicopter, R.drawable.orange_helicopter1, R.drawable.orange_helicopter2, R.drawable.orange_helicopter3, res);

    }

    public void setSelectedFlyingBird(Bitmap[] selectedFlyingBird) {
        this.selectedFlyingBird = selectedFlyingBird;
    }

    public void initCharacter(Bitmap[] bitmaps, int image1, int image2, int image3, Resources res) {
        bitmaps[0] = BitmapFactory.decodeResource(res, image1);
        bitmaps[1] = BitmapFactory.decodeResource(res, image2);
        bitmaps[2] = BitmapFactory.decodeResource(res, image3);
    }

    public void setSelectedFlyingBird(int id) {
        switch (id) {
            case 0:
                setSelectedFlyingBird(greenFlyingBird);
                break;
            case 1:
                setSelectedFlyingBird(blackFlyingBird);
                break;
            case 2:
                setSelectedFlyingBird(blueFlyingBird);
                break;
            case 3:
                setSelectedFlyingBird(pinkFlyingBird);
                break;
            case 4:
                setSelectedFlyingBird(redFlyingBird);
                break;
            case 5:
                setSelectedFlyingBird(blackHelicopter);
                break;
            case 6:
                setSelectedFlyingBird(greenHelicopter);
                break;
            case 7:
                setSelectedFlyingBird(orangeHelicopter);
                break;
        }
    }

    public void initCurrentCharacter(String currentAvatar) {
        if (currentAvatar != null) {
            switch (currentAvatar) {
                case "Green bird":
                    setSelectedFlyingBird(greenFlyingBird);
                    break;
                case "Black bird":
                    setSelectedFlyingBird(blackFlyingBird);
                    break;
                case "Blue bird":
                    setSelectedFlyingBird(blueFlyingBird);
                    break;
                case "Pink bird":
                    setSelectedFlyingBird(pinkFlyingBird);
                    break;
                case "Red bird":
                    setSelectedFlyingBird(redFlyingBird);
                    break;
                case "Black Helicopter":
                    setSelectedFlyingBird(blackHelicopter);
                    break;
                case "Green Helicopter":
                    setSelectedFlyingBird(greenHelicopter);
                    break;
                case "Orange Helicopter":
                    setSelectedFlyingBird(orangeHelicopter);
                    break;
            }
        }else{
            setSelectedFlyingBird(greenFlyingBird);
        }
    }

}
