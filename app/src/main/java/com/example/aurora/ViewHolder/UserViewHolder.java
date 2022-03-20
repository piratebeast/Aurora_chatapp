package com.example.aurora.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profile_img;
    public ImageView sendImg;
    public TextView name;
    public TextView email;
    public TextView phone;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_img = itemView.findViewById(R.id.profile_image);
        name = itemView.findViewById(R.id.name);
        email = itemView.findViewById(R.id.email);
        phone = itemView.findViewById(R.id.phone);
        sendImg = itemView.findViewById(R.id.sendImg);

    }
}
