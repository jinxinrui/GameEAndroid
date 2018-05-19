package com.example.jxr.gameeandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private Activity context;
    private int resource;
    private List<Post> posts;

    public PostAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        posts = objects;
    }

    @Override
    public View getView(int position, @NonNull View view, @NonNull ViewGroup viewGroup) {
        // check if view already exists
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView mTitle = (TextView) v.findViewById(R.id.post_title);
        TextView mSystem = (TextView) v.findViewById(R.id.post_system);
        TextView mRegion = (TextView) v.findViewById(R.id.post_region);
        TextView mPrice = (TextView) v.findViewById(R.id.post_price);
        ImageView mImage = (ImageView) v.findViewById(R.id.post_image);

        mTitle.setText(posts.get(position).getTitle());
        mSystem.setText(posts.get(position).getSystem());
        mRegion.setText(posts.get(position).getRegion());
        mPrice.setText(posts.get(position).getPrice());
        Glide.with(context).load(posts.get(position).getPic()).into(mImage);


        return v;
    }

    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }
}
