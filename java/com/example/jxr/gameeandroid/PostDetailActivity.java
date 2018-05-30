package com.example.jxr.gameeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jxr.gameeandroid.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Post mPost;
    private String currentUserId;
    private String currentUsername;
    private String ownerId;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // init ui items
        TextView mTitle = (TextView) findViewById(R.id.detail_title);
        TextView mSystem = (TextView) findViewById(R.id.detail_sys);
        TextView mRegion = (TextView) findViewById(R.id.detail_region);
        TextView mPrice = (TextView) findViewById(R.id.detail_price);
        TextView mCondition = (TextView) findViewById(R.id.detail_condition);
        TextView mDescription = (TextView) findViewById(R.id.detail_descr);
        ImageView mImage = (ImageView) findViewById(R.id.detail_img);
        final TextView mOwner = (TextView) findViewById(R.id.detail_owner);
        Button chatButton = (Button) findViewById(R.id.detail_chat_btn);

        // get Post object from main fragment
        Intent intent = getIntent();
        mPost = intent.getParcelableExtra("selectedPost");

        mTitle.setText(mPost.getTitle());
        mSystem.setText(mPost.getSystem());
        mRegion.setText(mPost.getRegion());
        mPrice.setText(mPost.getPrice());
        mCondition.setText(mPost.getCondition());
        mDescription.setText(mPost.getDescription());
        mOwner.setText(mPost.getUsername());
        Glide.with(this).load(mPost.getPic()).into(mImage);

        ownerId = mPost.getUser();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        chatButton.setOnClickListener(this);

        if (currentUserId.equals(ownerId)) {
            chatButton.setVisibility(View.INVISIBLE);
        }
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        // Verify if already chat with this person before
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("profiles")
                .child(currentUserId)
                .child("channels");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            boolean userExist = false;
            String channelId = "";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(ownerId)) {
                        userExist = true;
                        channelId = snapshot.child("channelId").getValue().toString();
                    }
                }
                // if channel already exists, pass the channel id to the next activity
                if (userExist) {
                    Intent newIntent1 = new Intent(getApplicationContext(), ChatActivity.class);
                    newIntent1.putExtra("channelId", channelId);
                    newIntent1.putExtra("otherUsername", mPost.getUsername());
                    startActivity(newIntent1);
                }
                // if channel does not exist, create a new channel and pass the channel id to next activity
                else {
                    // generate new channel id
                    String channelId = FirebaseDatabase.getInstance().getReference().child("channels").push()
                            .getKey();
                    DatabaseReference channel = FirebaseDatabase.getInstance().getReference()
                            .child("channels").child(channelId);
                    channel.child(currentUserId).setValue(mPost.getUsername());
                    channel.child(mPost.getUser()).setValue(currentUsername);

                    // current user profile
                    DatabaseReference profile1 = FirebaseDatabase.getInstance().getReference()
                            .child("profiles")
                            .child(currentUserId)
                            .child("channels");
                    profile1.child(mPost.getUser()).child("channelId").setValue(channelId);
                    profile1.child(mPost.getUser())
                            .child("otherUsername")
                            .setValue(mPost.getUsername());

                    // other user profile
                    DatabaseReference profile2 = FirebaseDatabase.getInstance().getReference()
                            .child("profiles")
                            .child(ownerId)
                            .child("channels");
                    profile2.child(currentUserId).child("channelId").setValue(channelId);
                    profile2.child(currentUserId).child("otherUsername").setValue(currentUsername);
                    Intent newIntent1 = new Intent(getApplicationContext(), ChatActivity.class);
                    newIntent1.putExtra("channelId", channelId);
                    newIntent1.putExtra("otherUsername", mPost.getUsername());
                    startActivity(newIntent1);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

