package com.example.jxr.gameeandroid;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.jxr.gameeandroid.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ValueEventListener {
    private String mUsername;
    private String mUserId;
    private String mChannelName;
    private String mChannelId;

    private ArrayList<Message> mMessageList;

    private EditText mSendEditText;
    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Intent intent = getIntent();
        mChannelId = intent.getStringExtra("channelId");
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String otherUsername = intent.getStringExtra("otherUsername");
        setTitle("You are talking to " + otherUsername);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("channels")
                .child(mChannelId).child("messages");

        mMessageList = new ArrayList<>();
        mAdapter = new MessageListAdapter(mUserId, mMessageList);

        mSendEditText = findViewById(R.id.messageEditText);
        mRecyclerView = findViewById(R.id.messagesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mSendEditText.getText().toString();
                if (!message.isEmpty()) {
                    DatabaseReference tempRef = mDatabaseRef.push();
                    tempRef.child("senderId").setValue(mUserId);
                    tempRef.child("senderName").setValue(mUsername);
                    tempRef.child("text").setValue(message);
                }
                mSendEditText.setText("");
            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mMessageList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Message tempMessage = new Message();
            tempMessage.mSenderName = (String) snapshot.child("senderName").getValue();
            tempMessage.mSenderID = (String) snapshot.child("senderId").getValue();
            tempMessage.mMessage = (String) snapshot.child("text").getValue();
            mMessageList.add(tempMessage);
        }
        mAdapter.notifyDataSetChanged();

        // automatically scroll to the bottom of the recyclerview when data added
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        }, 1);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabaseRef.addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabaseRef.removeEventListener(this);
    }

    public void goBackToChat() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("goToChatFragment", "ok");
        startActivity(intent);
    }

    // set back button on the action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                goBackToChat();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
