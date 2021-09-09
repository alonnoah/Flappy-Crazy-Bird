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
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button btnRecords, btnLogout, shopBtn;
    Menu myMenu;
    static MenuItem  soundMode, pause, coinsAmount;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
        boolean guestMode = preferences.getBoolean("guestMode", false);
        AppHolder.assign(this.getApplicationContext());
        btnRecords = findViewById(R.id.btnRecords);
        shopBtn = findViewById(R.id.shopBtn);

        if (guestMode) {
            AppHolder.guestMode = true;
            getSupportActionBar().setTitle("");
            getSupportActionBar().hide();
            btnRecords.setVisibility(View.GONE);
            shopBtn.setVisibility(View.GONE);
            AppHolder.getBitmapControl().initCurrentCharacter("Green bird");
        } else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.keepSynced(true);
                userID = user.getUid();
                reference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile != null) {
                            AppHolder.setUser(userProfile);
                            Objects.requireNonNull(getSupportActionBar()).setTitle(userProfile.getUsername());
                            AppHolder.getBitmapControl().initCurrentCharacter(userProfile.getCurrentAvatar());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


        btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableActivity.class);
                startActivity(intent);
            }
        });

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        btnLogout = findViewById(R.id.logoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
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

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
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