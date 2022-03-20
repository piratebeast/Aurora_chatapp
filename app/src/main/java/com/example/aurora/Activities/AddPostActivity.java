package com.example.aurora.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aurora.Model.Post;
import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPostActivity extends AppCompatActivity {
    CircleImageView profile_image;
    AppCompatButton addPost_btn;
    ImageView pickImg, previewImg;
    EditText statusText;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,databaseRefpro;
    StorageReference storageReference;
    String currentUser,time,postImg,creatorId;

    Uri postImgUri;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            currentUser = firebaseUser.getUid();
            databaseRefpro = FirebaseDatabase.getInstance().getReference("User").child(currentUser);
        }

        if (firebaseUser != null) {
            creatorId = firebaseUser.getUid();
        }

        profile_image = findViewById(R.id.profile_image);
        addPost_btn = findViewById(R.id.addPost_btn);
        pickImg = findViewById(R.id.pickImg);
        previewImg = findViewById(R.id.previouImg);
        statusText = findViewById(R.id.statusText);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("PostImg").child(creatorId);


        databaseRefpro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getUserProfilePic()).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });

        addPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = statusText.getText().toString();
                StorageReference storageRefer = storageReference.child("Img_"+System.currentTimeMillis());

                if (postImgUri != null) {
                    storageRefer.putFile(postImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRefer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        postImg = String.valueOf(uri);

                                        String postId = databaseReference.push().getKey();

                                        Post post = new Post(text,postImg,postId,creatorId,time);

                                        databaseReference.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(AddPostActivity.this, "Post add successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    String postId = databaseReference.push().getKey();

                    Post post = new Post(text,"",postId,creatorId,time);

                    databaseReference.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddPostActivity.this, "Post add successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });

        statusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    addPost_btn.setVisibility(View.VISIBLE);
                }else if (postImgUri == null) {
                    addPost_btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            postImgUri = data.getData();
            previewImg.setVisibility(View.VISIBLE);
            previewImg.setImageURI(postImgUri);

            addPost_btn.setVisibility(View.VISIBLE);
        }
    }
}