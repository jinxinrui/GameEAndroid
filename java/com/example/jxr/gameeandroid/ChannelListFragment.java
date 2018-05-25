package com.example.jxr.gameeandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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

public class ChannelListFragment extends Fragment implements AdapterView.OnItemClickListener, ValueEventListener {

    private String mUserName;
    private ArrayList<Channel> mChannelList;
    ArrayAdapter<Channel> mAdapter;

    private ListView mChannelListView;

    private DatabaseReference mDatabaseRef;

    View vMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mChannelListView = (ListView) vMain.findViewById(R.id.channelListView);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().toString();

        //mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("channels").child(currentUser);
        mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference().child("channels");

        mChannelList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mChannelList);
        mChannelListView.setAdapter(mAdapter);
        return vMain;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Channel currentChannel = mChannelList.get(position);
        Fragment fragment = new ChatFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mChannelList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Channel tempChannel = new Channel();
            tempChannel.mChannelID = snapshot.getKey();
            tempChannel.mChannelName = (String)snapshot.child("name").getValue();
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
