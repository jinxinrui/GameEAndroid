package com.example.jxr.gameeandroid;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * fragment to display user profile info
 */

public class MyProfileFragment extends Fragment {
    View vMain;

    private ImageView mImageView;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mAddress;
    private Button mButton;

    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_myprofile, container, false);

        getActivity().setTitle("My Profile");

        mImageView = (ImageView) vMain.findViewById(R.id.imageView_profile);
        mUsername = (TextView) vMain.findViewById(R.id.textview_username_profile);
        mEmail = (TextView) vMain.findViewById(R.id.textview_email_profile);
        mAddress = (TextView) vMain.findViewById(R.id.textview_address_profile);
        mButton = (Button) vMain.findViewById(R.id.button_edit_profile);

        mUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Uri photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(getContext()).load(photoUrl).into(mImageView);
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();

        // hide floating button
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        return vMain;
    }
}
