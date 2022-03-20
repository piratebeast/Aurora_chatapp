package com.example.aurora.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profile_img;
    public ImageView more;
    public ImageView image;
    public TextView name;
    public TextView time;
    public TextView text;
    DatabaseReference databaseReference;


    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        profile_img = itemView.findViewById(R.id.profile_image);
        more = itemView.findViewById(R.id.more);
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        time = itemView.findViewById(R.id.time);
        text = itemView.findViewById(R.id.text);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

    }
    public void getPostUserProfile (String UserID){
        databaseReference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getUserProfilePic()).into(profile_img);
                name.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
