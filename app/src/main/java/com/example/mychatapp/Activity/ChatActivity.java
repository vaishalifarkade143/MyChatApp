package com.example.mychatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.mychatapp.Adapter.MessageAdapter;

import com.example.mychatapp.databinding.ActivityChatBinding;
import com.example.mychatapp.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
ActivityChatBinding binding;
//37.
    MessageAdapter messageAdapter;
    ArrayList<Message> messages;
    String senderRoom,receiverRoom;

    //38*
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //38**
        database = FirebaseDatabase.getInstance();
        //37*
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this,messages);
        //40.
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChat.setAdapter(messageAdapter);


        //29.gettting the value
        String name = getIntent().getStringExtra("name");
        String receiveruid = getIntent().getStringExtra("uid");
        //37**
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiveruid;
        receiverRoom = receiveruid + senderUid;

        //39. show message on Recyclerview
        database.getReference().child("chats")
                .child(senderRoom)
                        .child("messages")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messages.clear();//to clear messages ArrayList
                                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                                        {
                                            Message message = snapshot1.getValue(Message.class);//typecasting
                                            message.setMessageId(snapshot1.getKey());
                                            messages.add(message);
                                        }
                                        messageAdapter.notifyDataSetChanged();//show message
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


        //38.
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = binding.msgBox.getText().toString();
                Date date = new Date();
                Message message = new Message(messageText,senderUid,date.getTime());

               binding.msgBox.setText("");//to clear textField

                //after sending message textview must be clean// .push() create node to send data
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //coppied
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });


            }
        });


        //set name on action bar
        getSupportActionBar().setTitle(name);
        //to show  back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //30 . to go on back actiovity on click of back button on actionbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}