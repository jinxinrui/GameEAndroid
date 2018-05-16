package com.example.jxr.gameeandroid;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


/**
 *
 */
public class MainFragment extends Fragment {

    private ListView mListView;
    private PostAdapter mAdapter;
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> mImageURLs;
    private FirebaseStorage mStorageRef = FirebaseStorage.getInstance();
    private ArrayList<Bitmap> mImageList;
    private ArrayList<Post> mPostList;

    View vMain;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) vMain.findViewById(R.id.postListView);
        mImageList = new ArrayList<>();
        mImageURLs = new ArrayList<>();
        mPostList = new ArrayList<>();
        mAdapter = new PostAdapter(getContext(), mPostList, mImageList);
        mListView.setAdapter(mAdapter);
        downloadImages();

        return vMain;
    }

//    public void onStart() {
//        super.onStart();
//        downloadImages();
//    }

    public void downloadImages() {
        DatabaseReference databaseReference = mDatabaseRef.child("posts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String url = snapshot.child("pic").getValue(String.class);
                    String condition = snapshot.child("condition").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);
                    String region = snapshot.child("region").getValue(String.class);
                    String system = snapshot.child("system").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    String user = snapshot.child("user").getValue(String.class);
                    Post newPost = new Post(condition, date, url, price, region, system, title, user);
                    mPostList.add(newPost);

                    if (!mImageURLs.contains(url) && url != null) {
                        mImageURLs.add(url);
                        mStorageRef.getReferenceFromUrl(url).getBytes(5 * 1024 *
                                1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                mImageList.add(BitmapFactory
                                        .decodeByteArray(bytes, 0, bytes.length));
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
