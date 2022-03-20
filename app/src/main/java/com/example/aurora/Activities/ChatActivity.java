package com.example.aurora.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.Adapters.ChatAdapter;
import com.example.aurora.Model.Chat;
import com.example.aurora.Model.User;
import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    Intent intent;
    String receiverId;

    Toolbar toolbar;
    CircleImageView profilePic;
    ImageView sendMsg;
    TextView name;
    EditText msg;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    DatabaseReference chatReference;
    FirebaseUser firebaseUser;
    String senderId;

    List<Chat> chatList;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        receiverId = intent.getStringExtra("UserId");

        chatList = new ArrayList<>();

        toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        profilePic = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        sendMsg = findViewById(R.id.sendMsg);
        msg = findViewById(R.id.typeMsg);
        recyclerView = findViewById(R.id.chatRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            senderId = firebaseUser.getUid();
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(receiverId);
        chatReference = FirebaseDatabase.getInstance().getReference("Chat");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getUserProfilePic()).into(profilePic);
                name.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_str = msg.getText().toString();
                String chatId = chatReference.push().getKey();

                HashMap<String,Object> chatMap = new HashMap<>();

                chatMap.put("sender",senderId);
                chatMap.put("receiver",receiverId);
                chatMap.put("message",msg_str);
                chatMap.put("chatId",chatId);

                chatReference.child(chatId).setValue(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChatActivity.this, "msg Sent", Toast.LENGTH_SHORT).show();
                            msg.setText("");
                        }
                    }
                });

            }
        });

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (senderId.equals(chat.getSender()) && receiverId.equals(chat.getReceiver())
                    || senderId.equals(chat.getReceiver()) && receiverId.equals(chat.getSender())) {
                        chatList.add(chat);
                    }

                }
                chatAdapter = new ChatAdapter(ChatActivity.this,chatList);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}