package com.vpiska.chat_open_adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vpiska.Chat.ChatMessage;
import com.vpiska.Chat.UserMessagesActivity;
import com.vpiska.R;
import com.vpiska.UserChatModel.UserChatsModel;

import java.util.ArrayList;
import java.util.List;

public class Chat_Open extends AppCompatActivity {
    UserChatsModel userChatsModel;
    RecyclerView recyclerView;
    List<UserChatsModel> modelAdList = new ArrayList<>();
    DatabaseReference storageChatId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference storageRefUserthis, storageRefUser2, storageFriends;
    FirebaseUser user;
    String autor;
    RecyclerView.LayoutManager layoutManager;
    String textMessage, timeMessage;
    Button button_send;
    EditText editText_send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_open);
        recyclerView = findViewById(R.id.recycler_view_open_chat);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storageChatId = firebaseDatabase.getReference("UserChat");
        autor = getIntent().getStringExtra("User");
        user = FirebaseAuth.getInstance().getCurrentUser();
        button_send = findViewById(R.id.send_message_button_open);
        storageRefUserthis = firebaseDatabase.getReference("UserChat").child(user.getUid());
        storageRefUser2 = firebaseDatabase.getReference("UserChat").child(autor);
        storageFriends = firebaseDatabase.getReference("UserChat").child("Friends");
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.send_massage_chat_open);
                String text = input.getText().toString();
                if (TextUtils.isEmpty(text)){
                    Toast.makeText(Chat_Open.this, "Введите сообщение", Toast.LENGTH_SHORT).show();
                } else {
                    storageRefUserthis.child(autor).push()
                            .setValue(new ChatMessage(input.getText().toString(), user.getUid(), autor));

                    storageRefUser2.child(user.getUid()).push()
                            .setValue(new ChatMessage(input.getText().toString(), user.getUid(), autor));

                    input.setText("");

                }
            }
        });


        readMessage();
    }


    public void readMessage(){

        storageChatId.child(user.getUid()).child(autor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot message: dataSnapshot.getChildren()){
                    timeMessage = (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)", Long.parseLong(message.child("timeMessage").getValue().toString()));
                    textMessage = message.child("textMessage").getValue().toString();
                    userChatsModel = new UserChatsModel(message.child("autor").getValue().toString(),"dsd",timeMessage,textMessage,"");
                    modelAdList.add(userChatsModel);

                }


                User_chat_open_adapter user_chat_open_adapter = new User_chat_open_adapter(Chat_Open.this, modelAdList);
                recyclerView.setAdapter(user_chat_open_adapter);
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Chat_Open.this, UserMessagesActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
