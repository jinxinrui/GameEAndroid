package com.example.jxr.gameeandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.jxr.gameeandroid.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ChatFragment extends Fragment implements ValueEventListener {

    private String mUsername;
    private String mUserId;
    private String mChannelName;
    private String mChannelId;

    private ArrayAdapter<Message> mMessageList;

    private EditText mSendEditText;
    private RecyclerView mAdapter;

    private DatabaseReference mDatabaseRef;

    View vMain;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_channel_list, container, false);



        return vMain;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
