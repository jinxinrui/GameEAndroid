package com.example.jxr.gameeandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, View.OnClickListener {

    private Button mLogInButton;
    private Button mRegisterButton;
    private EditText mUsername;
    private EditText mPassword;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Game Exchange");


        mUsername = (EditText) findViewById(R.id.inputUsername);
        mPassword = (EditText) findViewById(R.id.inputPassword);

        mLogInButton = (Button) findViewById(R.id.loginButton);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    @Override
    public void onClick(View v) {

    }
}
