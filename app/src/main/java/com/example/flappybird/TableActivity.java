package com.example.flappybird;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Collections;


public class TableActivity extends Activity {

    TableLayout tableLayout;
    Button backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_table);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        tableLayout = findViewById(R.id.tableLayout);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<User> list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    list.add(snapshot.getValue(User.class));
                }
                Collections.sort(list);
                for (int i = 0; i < list.size(); i++) {
                    addDataIntoTheTable(list,i);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addDataIntoTheTable(ArrayList<User> list , int i ){
        TableRow newRow = new TableRow(getApplicationContext());
        newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView index = new TextView(getApplicationContext());
        index.setText("" + (i + 1));
        index.setTextColor(ContextCompat.getColor(this, R.color.blue));
        index.setTextSize(25f);
        index.setGravity(Gravity.CENTER);

        TextView username = new TextView(getApplicationContext());
        username.setText(list.get(i).getUsername()+"");
        username.setTextColor(ContextCompat.getColor(this, R.color.blue));
        username.setTextSize(25f);
        username.setGravity(Gravity.CENTER);


        TextView score = new TextView(getApplicationContext());
        score.setText(list.get(i).getBestScore() + "");
        score.setTextColor(ContextCompat.getColor(this, R.color.blue));
        score.setTextSize(25f);
        score.setGravity(Gravity.CENTER);

        newRow.addView(index, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f)));
        newRow.addView(username, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2.5f)));
        newRow.addView(score, (new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2.1f)));
        tableLayout.addView(newRow);
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


