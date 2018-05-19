package com.example.jxr.gameeandroid;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private PostAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Post> mPostList;

    View vMain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) vMain.findViewById(R.id.postListView);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mPostList = new ArrayList<>();
        mListView.setOnItemClickListener(this);

        downloadImages();

        return vMain;
    }

    public void downloadImages() {
        DatabaseReference databaseReference = mDatabaseRef.child("posts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Post class require default constructor
                    Post post = snapshot.getValue(Post.class);
                    mPostList.add(post);
                }
                // init adapter
                mAdapter = new PostAdapter(getActivity(), R.layout.list_item, mPostList);
                // set adapter for listview
                mListView.setAdapter(mAdapter);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = mPostList.get(position);
        Intent newIntent = new Intent(getContext(), PostDetailActivity.class);
        newIntent.putExtra("selectedPost", post);
        startActivity(newIntent);
    }
}
