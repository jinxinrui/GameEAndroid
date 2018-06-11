package com.example.jxr.gameeandroid;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jxr.gameeandroid.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * adaptor to organize all the posts
 */
public class PostAdapter extends ArrayAdapter<Post> implements Filterable {
    private Activity context;
    private int resource;
    private List<Post> postList;
    private List<Post> mFilteredList;

    private PostFilter mFilter;

    public PostAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        postList = objects;
        // for searching
        mFilteredList = postList;
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

        mTitle.setText("Title: " + mFilteredList.get(position).getTitle());
        mSystem.setText("System: " + mFilteredList.get(position).getSystem());
        mRegion.setText("Region: " + mFilteredList.get(position).getRegion());
        mPrice.setText("Price: " + mFilteredList.get(position).getPrice());
        Glide.with(context).load(mFilteredList.get(position).getPic()).into(mImage);


        return v;
    }

    /**
     * private class to filter the list of posts
     */
    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Post> tempList = new ArrayList<>();
                for (Post post : postList) {
                    if (post.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            post.getSystem().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            post.getRegion().toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(post);
                }
                results.count = tempList.size();
                results.values = tempList;
            } else {
              results.count = postList.size();
              results.values = postList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredList = (ArrayList<Post>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new PostFilter();
        }
        return mFilter;
    }

    // ensure that the index not exceed the bound
    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    // return the OnItemClick item
    @Override
    public Post getItem(int i) {
        return mFilteredList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

}
