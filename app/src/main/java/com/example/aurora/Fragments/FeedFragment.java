package com.example.aurora.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aurora.Activities.AddPostActivity;
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

public class FeedFragment extends Fragment {
    CircleImageView profile_img;
    AppCompatButton addPost_btn;
    RecyclerView postRecycler;

    FirebaseUser firebaseUser;
    DatabaseReference databaseRefpro;
    String currentUser;
    DatabaseReference databaseReference;

    PostAdapter postAdapter;

    List<Post> postList;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        profile_img = view.findViewById(R.id.profile_image);
        addPost_btn = view.findViewById(R.id.addPost_btn);
        postRecycler = view.findViewById(R.id.postRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecycler.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            currentUser = firebaseUser.getUid();
            databaseRefpro = FirebaseDatabase.getInstance().getReference("User").child(currentUser);
        }

        databaseRefpro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getUserProfilePic()).into(profile_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(getActivity(),postList);
                postRecycler.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}