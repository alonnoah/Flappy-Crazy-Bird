package com.example.flappybird;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, register;
    private EditText editTextUserName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private final ArrayList<String> avatarArrayList =new ArrayList<>();
    private FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.textV);
        banner.setOnClickListener(this);

        register = findViewById(R.id.registerBtn);
        register.setOnClickListener(this);

        editTextEmail = findViewById(R.id.emailEditText);
        editTextUserName = findViewById(R.id.userNameEditText);
        editTextPassword = findViewById(R.id.passEditText);

        progressBar = findViewById(R.id.progressBar);

        preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textV:
                startActivity(new Intent(this, LoginActivity.class));
            case R.id.registerBtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String userName = editTextUserName.getText().toString().trim();

        if (userName.isEmpty()) {
            editTextUserName.setError("UserName is required!");
            editTextUserName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user= new User(userName,email);
                            user.setCurrentAvatar("Green bird");
                            avatarArrayList.add("Green bird");
                            user.setAvatarArrayList(avatarArrayList);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        editor.putBoolean("guestMode", false);
                                        editor.apply();
                                        AppHolder.setUser(user);
                                        Toast.makeText(RegisterActivity.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Failed to register! try again! ",Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);


                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Failed to register! try again! ",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    }
                });
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
