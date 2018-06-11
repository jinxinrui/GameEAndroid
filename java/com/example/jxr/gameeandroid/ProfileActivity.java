package com.example.jxr.gameeandroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mAddress;
    private Button mButton;
    private Uri imgUri;

    private FirebaseAuth mAuth;
    private String address;

    private String userId;
    private static final int REQUEST_CAMERA = 1000;
    private static final int SELECT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = (ImageView) findViewById(R.id.photo_profile);
        mUsername = (EditText) findViewById(R.id.editText_username_profile);
        mEmail = (EditText) findViewById(R.id.editText_email_profile);
        mAddress = (EditText) findViewById(R.id.editText_address_profile);
        mButton = (Button) findViewById(R.id.button_done_profile);

        Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(getApplicationContext()).load(photoUrl).into(mImageView);
        }
        mUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("profiles")
                .child(userId);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot != null && snapshot.getKey().equals("address"))
                        mAddress.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mImageView.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    /**
     * handle the click on imageview and done
     * button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo_profile:
                profileSelection();
                break;

            case R.id.button_done_profile:
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference newRef = database.getReference().child("profiles").child(userId).child("address");
                newRef.setValue(mAddress.getText().toString());
                UserProfileChangeRequest profilUpdataes = new UserProfileChangeRequest.Builder()
                        .setDisplayName(mUsername.getText().toString()).build();
                FirebaseUser user = mAuth.getCurrentUser();
                user.updateProfile(profilUpdataes).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    });
                if (imgUri != null) {
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setTitle("Uploading profile");
                    dialog.show();
                    UserProfileChangeRequest profileUpdates1 = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(imgUri).build();
                    user.updateProfile(profileUpdates1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // dismiss dialog when success
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * display dialog to let users choose
     * take photo or select from gallery.
     */
    private void profileSelection() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        //CHOOSE CAMERA
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if select from gallery
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                mImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ) {
            //SAVE URI FROM CAMERA
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUri = result.getUri();

                mImageView.setImageURI(imgUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
