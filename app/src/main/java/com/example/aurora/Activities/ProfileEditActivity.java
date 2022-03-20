package com.example.aurora.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {
    ImageView coverPic, back_btn;
    CircleImageView profile_img;
    TextView name, phone, email;
    AppCompatButton update_btn;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    String currentUserId;
    String coverPicUrl,profilePicUrl;

    String email_str,phone_str,name_str;

    Intent intent;
    Uri uri;
    Uri ur; //test
    StorageReference storageReference;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        intent = getIntent();
        currentUserId = intent.getStringExtra("UserId");

        if (currentUserId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
            storageReference = FirebaseStorage.getInstance().getReference("Upload").child(currentUserId);
        }

        dialog = new ProgressDialog(this);
        dialog.setTitle("please wait...");
        dialog.setMessage("Updating your profile");

        coverPic = findViewById(R.id.coverPic);
        profile_img = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        back_btn = findViewById(R.id.back_btn);
        update_btn = findViewById(R.id.update_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_str = name.getText().toString();
                phone_str = phone.getText().toString();
                email_str = email.getText().toString();

                StorageReference storageRef = storageReference.child("coverPic_" + name_str);
                StorageReference storageRe  = storageReference.child("profilePic_"+ name_str);

                dialog.show();
                if (uri != null) {
                    storageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()){
                                dialog.dismiss();
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        coverPicUrl = String.valueOf(uri);

                                        HashMap<String, Object> updateMap = new HashMap<>();

                                        updateMap.put("UserName", name_str);
                                        updateMap.put("UserPhone", phone_str);
                                        updateMap.put("UserEmail", email_str);
                                        updateMap.put("UserCoverPic", coverPicUrl);
                                        //updateMap.put("UserProfilePic", profilePicUrl);

                                        databaseReference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileEditActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                    }
                                });
                            }

                        }
                    });
                    storageRe.putFile(ur).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()){
                                dialog.dismiss();

                                storageRe.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profilePicUrl = String.valueOf(uri);

                                        HashMap<String, Object> updateMap = new HashMap<>();

                                        updateMap.put("UserName", name_str);
                                        updateMap.put("UserPhone", phone_str);
                                        updateMap.put("UserEmail", email_str);
                                        //updateMap.put("UserCoverPic", coverPicUrl);
                                        updateMap.put("UserProfilePic", profilePicUrl);

                                        databaseReference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileEditActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                    }
                                });
                            }

                        }
                    });

                } else {
                    HashMap<String, Object> updateMap = new HashMap<>();

                    updateMap.put("UserName", name_str);
                    updateMap.put("UserPhone", phone_str);
                    updateMap.put("UserEmail", email_str);
                    /*updateMap.put("UserCoverPic", coverPicUrl);
                    updateMap.put("UserProfilePic", profilePicUrl);*/

                    databaseReference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Toast.makeText(ProfileEditActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });

        coverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 111);
            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 222);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    coverPic.setImageURI(uri);
                }
            }
        } else if (requestCode == 222) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ur = data.getData();
                    profile_img.setImageURI(uri);
                }
            }
        }
    }

}