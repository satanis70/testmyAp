package com.vpiska.Chat;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vpiska.R;
import com.vpiska.UserChatModel.UserChatsModel;
import com.vpiska.UserChatModel.UserMassagesRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserMessagesActivity extends AppCompatActivity {
    DatabaseReference storageChatId;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    DatabaseReference storageRefUser;
    FirebaseStorage storage;
    StorageReference storageRef;
    UserChatsModel userChatsModel;
    UserMassagesRecyclerAdapter userMassagesRecyclerAdapter;
    List<UserChatsModel> modelAdList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Uri imageUri;
    String textMessage, timeMessage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messeges_activity);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recycler_messeges_user);
        recyclerView.setHasFixedSize(true);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        storageChatId = firebaseDatabase.getReference("UserChat");
        storageRefUser = firebaseDatabase.getReference("Users");
        displayChat();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayChat() {


        storageChatId.child("Friends").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    storageRefUser.child(postSnapshot.getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                            storageRef.child("images/" + postSnapshot.getValue()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    storageChatId.child(user.getUid()).child(postSnapshot.getValue().toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                for (final DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()){
                                                    timeMessage = (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)", Long.parseLong(dataSnapshot3.child("timeMessage").getValue().toString()));
                                                    textMessage = dataSnapshot3.child("textMessage").getValue().toString();
                                                }
                                            userChatsModel = new UserChatsModel(dataSnapshot1.getValue().toString(), uri.toString(), timeMessage, textMessage, postSnapshot.getValue().toString());
                                            modelAdList.add(userChatsModel);
                                            Collections.sort(modelAdList, new Comparator<UserChatsModel>() {
                                                @Override
                                                public int compare(UserChatsModel o1, UserChatsModel o2) {
                                                    return o2.getTime().compareTo(o1.getTime());
                                                }
                                            });
                                            UserMassagesRecyclerAdapter userMassagesRecyclerAdapter = new UserMassagesRecyclerAdapter(UserMessagesActivity.this, modelAdList);
                                            recyclerView.setAdapter(userMassagesRecyclerAdapter);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
