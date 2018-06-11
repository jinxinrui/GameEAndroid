package com.example.jxr.gameeandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jxr.gameeandroid.model.Channel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * display the list of chat rooms that
 * belongs to the current logged-on user
 */

public class ChannelListFragment extends Fragment implements AdapterView.OnItemClickListener, ValueEventListener {

    private String mUserName;
    private ArrayList<Channel> mChannelList;
    ArrayAdapter<Channel> mAdapter;

    private ListView mChannelListView;

    private DatabaseReference mDatabaseRef;


    View vMain;

    // display menu in fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Chat Room");

        vMain = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mChannelListView = (ListView) vMain.findViewById(R.id.channelListView);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("profiles").child(currentUserId).child("channels");

        mChannelList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mChannelList);
        mChannelListView.setAdapter(mAdapter);
        mChannelListView.setOnItemClickListener(this);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();

        // hide floating button
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }



        return vMain;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Channel currentChannel = mChannelList.get(position);
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("channelId", currentChannel.mChannelID);
        // channel name equals to otherUsername
        intent.putExtra("otherUsername", currentChannel.mChannelName);
        startActivity(intent);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mChannelList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Channel tempChannel = new Channel();
            tempChannel.mChannelID = snapshot.child("channelId").getValue().toString();
            tempChannel.mChannelName = snapshot.child("otherUsername").getValue().toString();
            mChannelList.add(tempChannel);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void onStart() {
        super.onStart();
        if (mDatabaseRef != null){
            mDatabaseRef.addValueEventListener(this);
        }

    }
    public void onStop() {
        super.onStop();
        if (mDatabaseRef != null){
            mDatabaseRef.removeEventListener(this);
        }
    }
}
