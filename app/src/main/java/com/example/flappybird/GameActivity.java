package com.example.flappybird;


import android.app.Dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    GamePlay gamePlay;
    Menu myMenu;
    MenuItem coin, soundMode, pause;
    static boolean volume;
    Dialog dialog;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        invalidateOptionsMenu();
        Objects.requireNonNull(getSupportActionBar()).setTitle(AppHolder.getUser().getUsername());
        AppHolder.gameActivityContext = this;
        gamePlay = new GamePlay(this);
        setContentView(gamePlay);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        myMenu = menu;
        coin = menu.findItem(R.id.coins);
        soundMode = menu.findItem(R.id.soundMode);
        pause =menu.findItem(R.id.pause);

        coin.setVisible(false);
        if (volume) {
            AppHolder.getSoundPlay().setSoundMode(false);
            soundMode.setIcon(R.drawable.volume_off);
        } else {
            AppHolder.getSoundPlay().setSoundMode(true);
            soundMode.setIcon(R.drawable.volume_on);
        }
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.soundMode:
                volume = !volume;
                item.setChecked(volume);
                if (volume) {
                    AppHolder.getSoundPlay().setSoundMode(false);
                    soundMode.setIcon(R.drawable.volume_off);
                } else {
                    AppHolder.getSoundPlay().setSoundMode(true);
                    soundMode.setIcon(R.drawable.volume_on);
                }
                break;
            case R.id.pause:
                GameManager.pauseVelocity = 0;
                pauseDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnGame(View view) {
        GameManager.pauseVelocity = 1;
        dialog.dismiss();
    }

    public void pauseDialog() {
        dialog = new Dialog(GameActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pause_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (!AppHolder.gameOver) {
            GameManager.pauseVelocity = 0;
            pauseDialog();
        }
    }


}
