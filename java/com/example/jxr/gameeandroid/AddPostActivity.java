package com.example.jxr.gameeandroid;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri imgUri;
    private Button mBrowseButton;
    private Button mUploadButton;
    private Button mPostButton;
    private ImageView mImageView;
    private EditText mTitle;
    private Spinner mSystemSpinner;
    private Spinner mRegionSpinner;
    private EditText mPriceText;
    private CheckBox mScrachesBox;
    private CheckBox mCaseBox;
    private String url = "";

    private String userId;

    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("image");

        // get user id.
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mImageView = (ImageView) findViewById(R.id.addPostPhoto);
        mTitle = (EditText) findViewById(R.id.addTitleText);
        mPriceText = (EditText) findViewById(R.id.addPriceText);
        mScrachesBox = (CheckBox) findViewById(R.id.checkBox1);
        mCaseBox = (CheckBox) findViewById(R.id.checkBox2);
        mSystemSpinner = (Spinner) findViewById(R.id.systemSpinner);
        mRegionSpinner = (Spinner) findViewById(R.id.regionSpinner);

        mBrowseButton = (Button) findViewById(R.id.browseButton);
        mUploadButton = (Button) findViewById(R.id.uploadButton);
        mPostButton = (Button) findViewById(R.id.postButton);


        mBrowseButton.setOnClickListener(this);
        mUploadButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browseButton:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE);
                break;
            case R.id.uploadButton:
                if (imgUri != null) {
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setTitle("Upload Image");
                    dialog.show();


                    StorageReference ref = mStorageRef.child(userId).child("image/" + System.currentTimeMillis() + "."
                            + getImageExt(imgUri));
                    // store image to the storage
                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // dismiss dialog when success
                            dialog.dismiss();
                            // Display error toast message
                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();

                            // save image info to firebase database
                            // save url info
                            url = taskSnapshot.getDownloadUrl().toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // dismiss dialog when error
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // show upload progress bar
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        }
                    });
                }
                break;

            case R.id.postButton:
                // Validataion
                if (mTitle.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title is not filled", Toast.LENGTH_LONG).show();
                } else if (mPriceText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Price is not filled", Toast.LENGTH_LONG).show();
                } else {
                    // upload to firebase
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference newRef = database.getReference().child("posts").push();
                    newRef.child("user").setValue(userId);
                    newRef.child("title").setValue(mTitle.getText().toString());
                    newRef.child("price").setValue(mPriceText.getText().toString());
                    if (mScrachesBox.isChecked()) {
                        newRef.child("condition").setValue("new");
                    } else {
                        newRef.child("condition").setValue("used");
                    }
                    if (mCaseBox.isChecked()) {
                        newRef.child("case").setValue("true");
                    } else {
                        newRef.child("case").setValue("false");
                    }
                    newRef.child("system").setValue(mSystemSpinner.getSelectedItem().toString());
                    newRef.child("region").setValue(mRegionSpinner.getSelectedItem().toString());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String currentDate = dateFormat.format(date);
                    newRef.child("date").setValue(currentDate);
                    newRef.child("pic").setValue(url);

                    Toast.makeText(getApplicationContext(), "Post Complete", Toast.LENGTH_LONG).show();

                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                mImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
