package com.vpiska.Chat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vpiska.Activities.Home;
import com.vpiska.R;


public class ChatActivity extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter,adapter2;
    Button buttonSend;
    DatabaseReference storageRefUser, storageChatId, storageChatUser, storageFriends, storageResfUserName;
    FirebaseDatabase firebaseDatabase;
    StorageReference StorageRefImage;
    FirebaseUser user;
    String userName;

    String UserAutorAd;
    FirebaseStorage storage;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        buttonSend = findViewById(R.id.send_message_buton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserAutorAd = getIntent().getStringExtra("User");
        storageRefUser = firebaseDatabase.getReference("Users").child(user.getUid());
        storageChatId = firebaseDatabase.getReference("UserChat").child(user.getUid());
        storageChatUser = firebaseDatabase.getReference("UserChat").child(UserAutorAd);
        storageFriends = firebaseDatabase.getReference("UserChat").child("Friends");
        storageResfUserName = firebaseDatabase.getReference("Users");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.send_massage_Edit_text);
                String text = input.getText().toString();
                if (TextUtils.isEmpty(text)){
                    Toast.makeText(ChatActivity.this, "Введите сообщение", Toast.LENGTH_SHORT).show();
                } else {
                    storageChatId.child(UserAutorAd).push()
                            .setValue(new ChatMessage(input.getText().toString(), user.getUid(), UserAutorAd));
                    storageFriends.child(UserAutorAd).child(user.getUid()).setValue(user.getUid());


                    storageChatUser.child(user.getUid()).push()
                            .setValue(new ChatMessage(input.getText().toString(), user.getUid(), UserAutorAd));
                    storageFriends.child(user.getUid()).child(UserAutorAd).setValue(UserAutorAd);
                    input.setText("");
                    Intent intent = new Intent(ChatActivity.this, Home.class);
                    startActivity(intent);
                }

            }
        });
        displayChat();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayChat() {
        ListView listMessages = findViewById(R.id.listView);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.item_chat, storageChatId.orderByChild("autor").equalTo(UserAutorAd)) {

            @Override
            protected void populateView(View v, ChatMessage  model, int position) {
                final TextView textMessage, autor, timeMessage;
                textMessage = v.findViewById(R.id.tvMessage);
                autor = v.findViewById(R.id.tvUser);
                timeMessage = v.findViewById(R.id.tvTime);
                textMessage.setText(model.getTextMessage());
                storageResfUserName.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        autor.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);
    }
}
