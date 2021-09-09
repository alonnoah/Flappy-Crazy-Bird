package com.example.flappybird;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    Context context;
    MediaPlayer move, score, crash, jump;
    boolean soundMode ;


    public SoundPlayer(Context context) {
        this.context = context;
        move = MediaPlayer.create(context, R.raw.swoosh);
        crash = MediaPlayer.create(context, R.raw.hit);
        jump = MediaPlayer.create(context, R.raw.wing);
        score = MediaPlayer.create(context, R.raw.ping);
        soundMode = true;
    }

    public void playMove() {
        if (move != null && soundMode ) {
            move.start();
        }
    }

    public void playScore() {
        if (score != null && soundMode )
            score.start();
    }

    public void playCrash() {
        if (crash != null && soundMode )
            crash.start();
    }

    public void platJump() {
        if (jump != null && soundMode )
            jump.start();
    }

    public void setSoundMode(boolean soundMode) {
        this.soundMode = soundMode;
    }
}
