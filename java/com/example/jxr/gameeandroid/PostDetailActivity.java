package com.example.jxr.gameeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        setTitle("Detail");

        // init ui items
        TextView mTitle = (TextView) findViewById(R.id.detail_title);
        TextView mSystem = (TextView) findViewById(R.id.detail_sys);
        TextView mRegion = (TextView) findViewById(R.id.detail_region);
        TextView mPrice = (TextView) findViewById(R.id.detail_price);
        TextView mCondition = (TextView) findViewById(R.id.detail_condition);
        TextView mDescription = (TextView) findViewById(R.id.detail_descr);
        ImageView mImage = (ImageView) findViewById(R.id.detail_img);
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
        Glide.with(this).load(mPost.getPic()).into(mImage);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
