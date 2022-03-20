package com.example.aurora.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aurora.Adapters.PostAdapter;
import com.example.aurora.Model.Post;
import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    ImageView coverPic,edit;
    CircleImageView profile_img;
    TextView name, phone, email;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,postDatabaseReference;
    String currentUserId;

    RecyclerView recycler;
    List<Post> postList;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
        }

        postDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        coverPic = findViewById(R.id.coverPic);
        profile_img = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        edit = findViewById(R.id.edit);
        recycler = findViewById(R.id.recycler);

        postList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ProfileEditActivity.class);
                intent.putExtra("UserId",currentUserId);
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    name.setText(user.getUserName());
                    phone.setText(user.getUserPhone());
                    email.setText(user.getUserEmail());

                    Picasso.get()
                            .load(user.getUserCoverPic())
                            .placeholder(R.drawable.cover)
                            .into(coverPic);

                    Picasso.get()
                            .load(user.getUserProfilePic())
                            .placeholder(R.drawable.profile)
                            .into(profile_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (currentUserId.equals(post.getCreatorId())){
                        postList.add(post);
                    }
                }
                postAdapter = new PostAdapter(ProfileActivity.this,postList);
                recycler.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}