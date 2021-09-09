package com.example.flappybird;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class AppHolder {
    static BitmapControl bitmapControl;
    static GameManager gameManager;
    static int SCREEN_WIDTH_X;
    static int SCREEN_HEIGHT_Y;
    static int gravityPull;
    static int JUMP_VELOCITY;
    static int tubeGap;
    static int tube_numbers;
    static int tubeVelocity;
    static int minimumTubeCollection_y;
    static int maximumTubeCollection_y;
    static int tubeDistance;
    static User user = new User();

    static int shieldVelocity;
    static int shieldDistance;

    static int bombVelocity;
    static int bombDistance;
    static int moneyVelocity;
    static int moneyDistance;

    static Context gameActivityContext;
    static SoundPlayer soundPlay;

    static boolean guestMode;
    static boolean gameOver;

    public static void assign(Context context) {

        mapScreenSize(context);
        bitmapControl = new BitmapControl(context.getResources());
        holdGameVariables();
        gameManager = new GameManager();
        soundPlay = new SoundPlayer(context);
        guestMode = false;
        gameOver = false;
    }

    public static SoundPlayer getSoundPlay() {
        return soundPlay;
    }

    public static void holdGameVariables() {

        AppHolder.gravityPull = 5;
        AppHolder.JUMP_VELOCITY = -40;

        //****************Tubes variables****************//
        AppHolder.tubeGap = 650;
        AppHolder.tube_numbers = 2;
        AppHolder.tubeVelocity = 19;
        AppHolder.minimumTubeCollection_y = (int) (AppHolder.tubeGap / 2.0);
        AppHolder.maximumTubeCollection_y = AppHolder.SCREEN_HEIGHT_Y - AppHolder.minimumTubeCollection_y - AppHolder.tubeGap;
        AppHolder.tubeDistance = AppHolder.SCREEN_WIDTH_X;
        //***********************************************//

        //****************Shield variables****************//
        AppHolder.shieldVelocity = AppHolder.tubeVelocity;
        AppHolder.shieldDistance = AppHolder.SCREEN_WIDTH_X;
        //************************************************//

        //****************Bomb variables****************//
        AppHolder.bombDistance = AppHolder.SCREEN_WIDTH_X * 6;
        AppHolder.bombVelocity = AppHolder.tubeVelocity * 2;
        //**********************************************//

        //****************Money variables****************//
        AppHolder.moneyDistance = AppHolder.SCREEN_WIDTH_X * 3;
        AppHolder.moneyVelocity = AppHolder.tubeVelocity;
        //***********************************************//
    }

    public static BitmapControl getBitmapControl() {
        return bitmapControl;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static void mapScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        AppHolder.SCREEN_WIDTH_X = width;
        AppHolder.SCREEN_HEIGHT_Y = height;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        AppHolder.user.setUsername(u.getUsername());
        AppHolder.user.setEmail(u.getEmail());
        AppHolder.user.setBestScore(u.getBestScore());
        AppHolder.user.setMoneyCount(u.getMoneyCount());
        AppHolder.user.setAvatarArrayList(u.getAvatarArrayList());
        AppHolder.user.setCurrentAvatar(u.getCurrentAvatar());

    }


}
