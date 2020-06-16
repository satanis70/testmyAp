package com.vpiska;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.vpiska.Chat.UserMessagesActivity;
import com.vpiska.adModel.CustomAdapterRecycler;
import com.vpiska.adModel.ModelAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TextView textViewLogOut;
    StorageReference storageRef;
    DatabaseReference storageRefUser;
    FirebaseUser user;
    ImageView ava;
    FirebaseStorage storage;
    Spinner spinner;
    TextView nameUserText;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerViewAd;
    List<ModelAd> modelAdList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterRecycler customAdapterRecycler;
    ModelAd modelAd;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        textViewLogOut = findViewById(R.id.logout);
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        recyclerViewAd = findViewById(R.id.add_recycler_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        View header = navigationView.getHeaderView(0);
        nameUserText = (TextView) header.findViewById(R.id.nameUserText);
        //nameUserText.setText(storageRef.child("Users").child(user.getUid()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageRefUser = firebaseDatabase.getReference("Users").child(user.getUid());
        recyclerViewAd = findViewById(R.id.add_recycler_view);
        recyclerViewAd.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAd.setLayoutManager(layoutManager);
        storageRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                nameUserText.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        modelAd = new ModelAd();
        showData();



        spinner = (Spinner) navigationView.getMenu().findItem(R.id.city).getActionView();
        storageRef.child("images/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ava = findViewById(R.id.ava);
                Picasso.get().load(uri)
                        .into(ava);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "При загрузке фотографии произошла ошибка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        LogOut();



    }

    private void showData() {

         String timestamp;

        firebaseFirestore.collection("AddAd").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult()){

                    ModelAd modelAd = new ModelAd(documentSnapshot.getString("Name_Ads"), documentSnapshot.getString("Adres"),
                            documentSnapshot.getString("adImage"),documentSnapshot.getString("More_Details"),
                            documentSnapshot.getString("Phone"), String.valueOf(documentSnapshot.get("Time")),documentSnapshot.getString("User"));
                    modelAdList.add(modelAd);

                }

                customAdapterRecycler = new CustomAdapterRecycler(Home.this, modelAdList);
                recyclerViewAd.setAdapter(customAdapterRecycler);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addАd:
                Intent intent = new Intent(Home.this, AddAd.class);
                startActivity(intent);
                break;
            case R.id.messeges:
                Intent intent1 = new Intent(Home.this, UserMessagesActivity.class);
                startActivity(intent1);
                break;
        }
        return false;
    }

    public void LogOut(){
        textViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, MainActivity.class));
            }
        });
    }

    public void onBackPressed() {
        // super.onBackPressed();

    }




}
