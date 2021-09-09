package com.example.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {


    private final ArrayList<String> mNames = new ArrayList<>();
    private final ArrayList<Integer> mImages = new ArrayList<>();
    private final ArrayList<Integer> mPrice = new ArrayList<>();
    private final ArrayList<Button> mBuyButtons = new ArrayList<>();
    private final ArrayList<Button> mSelectButtons = new ArrayList<>();
    TextView moneyTextView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button btn, backBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.keepSynced(true);
        userID = user.getUid();

        moneyTextView = findViewById(R.id.wallet_coins);
        backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    moneyTextView.setText(String.valueOf(userProfile.getMoneyCount()));
                    AppHolder.getUser().setMoneyCount(userProfile.getMoneyCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button btn = new Button(this);
        btn.setText("BUY");
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        initImageBitmap();
    }

    private void initImageBitmap() {

        addAnItemToTheShop(R.drawable.green_bird, "Green bird", 0);
        addAnItemToTheShop(R.drawable.black_bird, "Black bird", 200);
        addAnItemToTheShop(R.drawable.blue_bird, "Blue bird", 200);
        addAnItemToTheShop(R.drawable.pink_bird, "Pink bird", 200);
        addAnItemToTheShop(R.drawable.red_bird, "Red bird", 200);
        addAnItemToTheShop(R.drawable.black_helicopter, "Black Helicopter", 400);
        addAnItemToTheShop(R.drawable.green_helicopter, "Green Helicopter", 400);
        addAnItemToTheShop(R.drawable.orange_helicopter, "Orange Helicopter", 400);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImages, mPrice, mBuyButtons, mSelectButtons, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addAnItemToTheShop(int image, String name, int price) {
        mImages.add(image);
        mNames.add(name);
        mPrice.add(price);
        mBuyButtons.add(btn);
        mSelectButtons.add(btn);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
