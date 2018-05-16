package com.example.jxr.gameeandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {
    private Context mCurrentContext;
    private ArrayList<Post> mPostList;
    private ArrayList<Post> mFilteredList;
    private ArrayList<Bitmap> mImageList;
    private PostFilter mFilter;

    public PostAdapter(Context con, ArrayList<Post> posts, ArrayList<Bitmap> images) {
        mCurrentContext = con;
        mPostList = posts;
        mImageList = images;
        mFilteredList = mPostList;
    }

    @Override
    public int getCount() {
        return mPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // check if view already exists
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mCurrentContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // create a list item based on layout definition
            view = inflater.inflate(R.layout.list_item, null);
        }

        // Assign values to the TextViews using Post Object
        TextView titleView = (TextView) view.findViewById(R.id.post_title);
        TextView systemView = (TextView) view.findViewById(R.id.post_system);
        TextView regionView = (TextView) view.findViewById(R.id.post_region);
        TextView priceView = (TextView) view.findViewById(R.id.post_price);
        ImageView imageView = (ImageView) view.findViewById(R.id.post_image);

        titleView.setText(mPostList.get(position).getTitle());
        systemView.setText(mPostList.get(position).getSystem());
        regionView.setText(mPostList.get(position).getRegion());
        priceView.setText(mPostList.get(position).getPrice());
        if (mImageList.size() >= position + 1){
            if (mImageList.get(position) != null) {
                imageView.setImageBitmap(mImageList.get(position));
            }
        }



        return view;
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
