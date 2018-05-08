package com.example.jxr.gameeandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSaveButton;

    private FirebaseAuth mAuth;

    private ConstraintLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");

        mLayout = findViewById(R.id.registerLayout);

        mUsername = (EditText) findViewById(R.id.registerUsername);
        mEmail = (EditText) findViewById(R.id.registerEmail);
        mPassword = (EditText) findViewById(R.id.registerPassword);

        mSaveButton = (Button) findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        String email = mEmail.getText().toString();
        final String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if (email.length() > 0 && password.length() > 0 && username.length() > 0) {
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnFailureListener(this,
//                            new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    createSnackbar(e.getMessage());
//                                }
//                            });
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //createSnackbar("Registration complete");
                                Toast.makeText(getApplicationContext(),"Registration Complete", Toast.LENGTH_LONG).show();

                                // add username to profile
                                UserProfileChangeRequest profilUpdataes = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.updateProfile(profilUpdataes).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        createSnackbar(e.getMessage());
                                    }
                                });
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(this,
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            createSnackbar(e.getMessage());
                        }
                    });

        } else if (email.trim().isEmpty()) {
            createSnackbar("Please enter an email");
        } else if (username.trim().isEmpty()) {
            createSnackbar("Please enter a username");
        } else {
            createSnackbar("Please enter your password");
        }
    }

    public void createSnackbar(String message) {
        Snackbar.make(mLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
