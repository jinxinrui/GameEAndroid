package com.example.jxr.gameeandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jxr.gameeandroid.model.Message;

import java.util.ArrayList;

/**
 * adapter to organize the chat messages
 * between pairs of users
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private String mUserID;
    private ArrayList<Message> mMessageList;

    public MessageListAdapter(String userID, ArrayList<Message> messageList) {
        super();
        mUserID = userID;
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_self,
                            parent, false);
            return new SentMessageHolder(view);
        }
        else if(viewType == VIEW_TYPE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_other,
                            parent, false);
            return new RecievedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        Message currentMessage = mMessageList.get(position);
        int viewType = holder.getItemViewType();
        if(viewType == VIEW_TYPE_SENT) {
            ((SentMessageHolder)holder).bind(currentMessage);
        }
        else if (viewType == VIEW_TYPE_RECEIVED) {
            ((RecievedMessageHolder)holder).bind(currentMessage);
        }
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(mMessageList.get(position).mSenderID.equals(mUserID))
            return VIEW_TYPE_SENT;
        else
            return VIEW_TYPE_RECEIVED;
    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView mMessageTextView;
        public SentMessageHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.messageTextView);
        }
        public void bind(Message currentMessage) {
            mMessageTextView.setText(currentMessage.mMessage);
        }
    }

    private class RecievedMessageHolder extends RecyclerView.ViewHolder {
        private TextView mMessageTextView;
        private TextView mNameTextView;
        public RecievedMessageHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.messageOtherTextView);
            mNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
        public void bind(Message currentMessage) {
            mMessageTextView.setText(currentMessage.mMessage);
            mNameTextView.setText(currentMessage.mSenderName);
        }
    }
}
