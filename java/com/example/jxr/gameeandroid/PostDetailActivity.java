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

public class PostDetailActivity extends AppCompatActivity {

    private Post mPost;
    private View vMain;

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
        TextView mOwner = (TextView) findViewById(R.id.detail_owner);
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


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("otherUser", mPost.getUser());
                // set FragmentClass arguments
                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
            }
        });


    }

}

