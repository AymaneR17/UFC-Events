package com.example.ufcproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ufcproject.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

ActivityChatBinding binding;
String receiverId;
String receiverRoom, senderRoom;
DatabaseReference databaseReferenceSender, databaseReferenceReceiver;
EditText messageEd;

MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      receiverId=  getIntent().getStringExtra("id");
      messageEd= findViewById(R.id.messageEd);

      senderRoom = FirebaseAuth.getInstance().getUid()+receiverId;
      receiverRoom = receiverId+FirebaseAuth.getInstance().getUid();

      messageAdapter= new MessageAdapter(this);
      binding.recyclerViewChat.setAdapter(messageAdapter);
      binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

      databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
      databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

      databaseReferenceSender.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              messageAdapter.clear();
              for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.add(message);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
      });
      binding.sendMessage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String message = binding.messageEd.getText().toString();
              if (message.trim().length()>0){sendMessage(message);
                  messageEd.setText("") ;}
          }
      });
    }
    private void sendMessage(String message){
        String messageId= UUID.randomUUID().toString();
        Message messageModel = new Message(messageId, FirebaseAuth.getInstance().getUid(),message);
        messageAdapter.add(messageModel);

        databaseReferenceSender.child(messageId).setValue(messageModel);
        databaseReferenceReceiver.child(messageId).setValue(messageModel);
    }
}