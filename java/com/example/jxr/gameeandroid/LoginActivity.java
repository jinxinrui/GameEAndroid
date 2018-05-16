package com.example.jxr.gameeandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, View.OnClickListener {

    private Button mLogInButton;
    private Button mRegisterButton;
    private EditText mUsername;
    private EditText mPassword;
    private ConstraintLayout mLayout;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setTitle("Game Exchange");

        mLayout = findViewById(R.id.loginLayout);

        mUsername = (EditText) findViewById(R.id.loginUsername);
        mPassword = (EditText) findViewById(R.id.loginPassword);

        mLogInButton = (Button) findViewById(R.id.loginButton);
        mLogInButton.setOnClickListener(this);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mRegisterButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (username.length() > 0 && password.length() > 0) {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnFailureListener(this,
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            createSnackbar(e.getMessage());
                                        }
                                    });
                } else {
                    createSnackbar("Please enter an email and password");
                }
                break;
            case R.id.registerButton:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void createSnackbar(String message) {
        Snackbar.make(mLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
