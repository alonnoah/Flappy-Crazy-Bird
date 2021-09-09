package com.example.flappybird;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword;
    private Button register, guestMode;
    private Button signIn;
    CheckBox remember;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    String email;
    String password;
    Boolean checked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();

        preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
        editor = preferences.edit();

        mAuth = FirebaseAuth.getInstance();

        guestMode = findViewById(R.id.guestMode);
        guestMode.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = findViewById(R.id.signInBtn);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.passEditText);

        progressBar = findViewById(R.id.progressBar);
        remember = findViewById(R.id.rememberMe);

        String checkbox = preferences.getString("remember", "false");
        if (checkbox.equals("true")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (checkbox.equals("false")) {
            editor.putString("remember", "false");
            editor.apply();
        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    checked = true;

                } else {
                    SharedPreferences preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    checked = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.signInBtn:
                editor.putBoolean("guestMode", false);
                editor.apply();
                userLogin();
                break;
            case R.id.guestMode:
                editor.putBoolean("guestMode", true);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void userLogin() {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        if (!checked) {
            showCustomDialog();
        } else {
            userVerify();
        }
    }


    public void showCustomDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        Button answerYes, answerNo;
        answerYes = dialog.findViewById(R.id.answerYes);
        answerNo = dialog.findViewById(R.id.answerNo);

        answerYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVerify();
            }
        });
        answerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                editTextEmail.setText(email);
                editTextPassword.setText(password);
            }
        });
        dialog.show();
    }

    public void userVerify() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! check your details ", Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = getSharedPreferences("myStoragePreference", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
                progressBar.setVisibility(View.GONE);
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
