package com.example.aurora.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profile_image;
    public TextView msgTv;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_image = itemView.findViewById(R.id.profile_image);
        msgTv = itemView.findViewById(R.id.msgTv);



    }
}
