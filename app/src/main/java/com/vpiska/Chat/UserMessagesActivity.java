package com.vpiska.Chat;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.vpiska.Home;
import com.vpiska.R;
import com.vpiska.UserChatModel.UserChatsModel;
import com.vpiska.UserChatModel.UserMassagesRecyclerAdapter;
import com.vpiska.adModel.CustomAdapterRecycler;
import com.vpiska.adModel.ModelAd;

import java.util.ArrayList;
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
        storageChatId = firebaseDatabase.getReference("UserChat").child(user.getUid());
        storageRefUser = firebaseDatabase.getReference("Users");
        displayChat();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayChat() {
        final int autorNumber;

       storageChatId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    storageRef.child("images/" + postSnapshot.child("autor").getValue()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            final String time = (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)", Long.parseLong(postSnapshot.child("timeMessage").getValue().toString()));
                            storageRefUser.child(postSnapshot.child("autor").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int a = dataSnapshot.getValue(String.class).length();
                                    String autor = dataSnapshot.getValue(String.class);
                                    userChatsModel = new UserChatsModel(autor, uri.toString(), time, postSnapshot.child("textMessage").getValue().toString());
                                    modelAdList.add(userChatsModel);
                                    UserMassagesRecyclerAdapter userMassagesRecyclerAdapter = new UserMassagesRecyclerAdapter(UserMessagesActivity.this, modelAdList);
                                    recyclerView.setAdapter(userMassagesRecyclerAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Произошла ошибка!", Toast.LENGTH_SHORT);
                            toast.show();
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
