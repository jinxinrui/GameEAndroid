package com.example.jxr.gameeandroid;



import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.jxr.gameeandroid.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private ListView mListView;
    private PostAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Post> mPostList;
    private SearchView mSearchView;

    View vMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) vMain.findViewById(R.id.postListView);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mPostList = new ArrayList<>();
        mListView.setOnItemClickListener(this);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();

        // show floating button
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }

        return vMain;
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadImages();
    }

    public void downloadImages() {
        DatabaseReference databaseReference = mDatabaseRef.child("posts");

        ValueEventListener downloadListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPostList.clear();
                // fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Post class require default constructor
                    Post post = snapshot.getValue(Post.class);
                    post.setPostId(snapshot.getKey()); // used to identify and delete the selected post
                    mPostList.add(post);
                }
                // reverse the post list
                // so the list can display the newest post at the top of ListView
                Collections.reverse(mPostList);
                // init adapter
                mAdapter = new PostAdapter(getActivity(), R.layout.list_item, mPostList);
                // set adapter for listview
                mListView.setAdapter(mAdapter);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(downloadListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = mAdapter.getItem(position); // get the position in filtered list in adapter
        Intent newIntent = new Intent(getContext(), PostDetailActivity.class);
        newIntent.putExtra("selectedPost", post);
        startActivity(newIntent);
    }

    // create menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(manager.
                getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }
}
