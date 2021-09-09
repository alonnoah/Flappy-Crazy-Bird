package com.example.flappybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class GameOverActivity extends AppCompatActivity {
    Button restartBtn, btnRecords;
    int scoreCount, scoreBest;
    TextView textScore, textBest;
    boolean isOverBecauseOfBomb;
    LottieAnimationView lottieAnimationView;
    LinearLayout linearLayoutFireAnimation;
    Menu myMenu;
    MenuItem soundMode, pause, coinsAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().setTitle(AppHolder.getUser().getUsername());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        isOverBecauseOfBomb = false;
        btnRecords = findViewById(R.id.btnRecords);
        restartBtn = findViewById(R.id.btnRestart);
        textScore = findViewById(R.id.scoreDisplay);
        linearLayoutFireAnimation = findViewById(R.id.linearLayoutFireAnimation);

        textBest = findViewById(R.id.BestDisplay);
        lottieAnimationView = findViewById(R.id.fireAnimation);
        isOverBecauseOfBomb = getIntent().getExtras().getBoolean("isOverBecauseOfBomb");

        if (isOverBecauseOfBomb) {
            linearLayoutFireAnimation.bringToFront();
            linearLayoutFireAnimation.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.VISIBLE);
        }

        scoreCount = getIntent().getExtras().getInt("score");
        if (AppHolder.guestMode) {
            getSupportActionBar().hide();
            btnRecords.setVisibility(View.INVISIBLE);
            SharedPreferences preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
            scoreBest = preferences.getInt("scoreBest", 0);
            SharedPreferences.Editor editor = preferences.edit();
            if (scoreCount > scoreBest) {
                scoreBest = scoreCount;
                editor.putInt("scoreBest", scoreBest).apply();
            }
        } else {
            scoreBest = AppHolder.user.getBestScore();
            viewData();
        }
        textScore.setText("" + scoreCount);
        textBest.setText("" + scoreBest);

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    public void viewData() {
        btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameOverActivity.this, TableActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        myMenu = menu;

        pause = menu.findItem(R.id.pause);
        soundMode = menu.findItem(R.id.soundMode);
        coinsAmount = menu.findItem(R.id.coinsAmount);

        coinsAmount.setTitleCondensed(AppHolder.getUser().getMoneyCount() + "");

        pause.setVisible(false);
        soundMode.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}