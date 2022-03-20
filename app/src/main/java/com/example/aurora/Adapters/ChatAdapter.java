package com.example.aurora.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Model.Chat;
import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.example.aurora.ViewHolder.ChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    Context context;
    List<Chat> chatList;
    FirebaseUser firebaseUser;
    String currentUserId;

    final int sender = 1;
    final int receiver = 2;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == sender) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
            return new ChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        holder.msgTv.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (currentUserId.equals(chatList.get(position).getSender())) {
            return sender;
        } else {
            return receiver;
        }
    }
}
