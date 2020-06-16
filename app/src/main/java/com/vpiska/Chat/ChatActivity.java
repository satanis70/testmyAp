package com.vpiska.Chat;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.vpiska.R;

import java.util.Date;


public class ChatActivity extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter,adapter2;
    Button buttonSend;
    DatabaseReference storageRefUser, storageChatId, storageChatUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String userName;
    String UserAutorAd;
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



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.send_massage_Edit_text);
                storageChatId.push()
                        .setValue(new ChatMessage(input.getText().toString(),UserAutorAd));

                storageChatUser.push()
                        .setValue(new ChatMessage(input.getText().toString(),user.getUid()));

            }
        });
        displayChat();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayChat() {
        ListView listMessages = findViewById(R.id.listView);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.item_chat, storageChatId) {

            @Override
            protected void populateView(View v, ChatMessage  model, int position) {
                TextView textMessage, autor, timeMessage;
                textMessage = v.findViewById(R.id.tvMessage);
                autor = v.findViewById(R.id.tvUser);
                timeMessage = v.findViewById(R.id.tvTime);
                textMessage.setText(model.getTextMessage());
                autor.setText(user.getUid());

                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);
    }
}
