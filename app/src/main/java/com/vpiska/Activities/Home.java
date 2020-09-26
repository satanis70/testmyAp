package com.vpiska.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.room.Room;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.vpiska.Chat.UserMessagesActivity;
import com.vpiska.R;
import com.vpiska.SqlLiteCity.City;
import com.vpiska.SqlLiteCity.DatabaseCity;
import com.vpiska.adModel.CustomAdapterRecycler;
import com.vpiska.adModel.ModelAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
    String itemCity;
    public static DatabaseCity databaseCity;
    String cityfromDb;
    private Boolean mIsSpinnerFirstCall = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"RestrictedApi", "CheckResult"})
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageRefUser = firebaseDatabase.getReference("Users").child(user.getUid());
        recyclerViewAd = findViewById(R.id.add_recycler_view);
        recyclerViewAd.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAd.setLayoutManager(layoutManager);

        City city = new City();
        databaseCity = Room.databaseBuilder(getApplicationContext(), DatabaseCity.class, "cityDb").build();
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
        spinner = new Spinner(Home.this);
        spinner = (Spinner) navigationView.getMenu().findItem(R.id.city).getActionView();

        //spinner.setSelection(0, false);


        storageRef.child("images/" + user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
            ava = findViewById(R.id.ava);
            Picasso.get().load(uri)
                    .into(ava);
        }).addOnFailureListener(exception -> {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "При загрузке фотографии произошла ошибка!", Toast.LENGTH_SHORT);
            toast.show();
            //проверить есть ли на сервере фотография или нет
            Intent intent = new Intent(Home.this, Upload_image.class);
            startActivity(intent);
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCity = parent.getItemAtPosition(position).toString();
                if(!mIsSpinnerFirstCall) {
                    Observable.just(databaseCity)
                            .subscribeOn(Schedulers.io())
                            .subscribe((DatabaseCity databaseCity1) -> {
                                databaseCity1.cityDao().update(city);
                            });

                    //Добавляем в Room
                    AsyncTask.execute(() -> {
                        city.setId(0);
                        city.setCityName(itemCity);
                        databaseCity.cityDao().addCity(city);
                        showData();
                    });
                }

                mIsSpinnerFirstCall = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseCity.cityDao().getCity().firstElement()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<City> cities) -> {
                    if (cities.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Выберите город", Toast.LENGTH_SHORT).show();
                    } else {
                        showData();
                    }
                });

        LogOut();


    }


    @SuppressLint("CheckResult")
    private void showData() {
                databaseCity.cityDao().getCity()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<City> cities) -> {
                    cityfromDb = cities.get(0).getCityName();
                    Toast.makeText(this, cityfromDb, Toast.LENGTH_SHORT).show();
                    //spinner.setSelection(getIndex(spinner, cityfromDb));
                    try {
                        firebaseFirestore.collection("AddAd")
                                .document(cityfromDb)
                                .collection(cityfromDb).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                modelAdList.clear();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    ModelAd modelAd = new ModelAd(documentSnapshot.getString("Name_Ads"),
                                            documentSnapshot.getString("Adres"),
                                            documentSnapshot.getString("adImage"),
                                            documentSnapshot.getString("More_Details"),
                                            documentSnapshot.getString("Phone"),
                                            String.valueOf(documentSnapshot.get("Time")),
                                            documentSnapshot.getString("User"));
                                    modelAdList.add(modelAd);
                                }
                                customAdapterRecycler = new CustomAdapterRecycler(Home.this, modelAdList);
                                recyclerViewAd.setAdapter(customAdapterRecycler);
                                customAdapterRecycler.notifyDataSetChanged();


                            }

                        });

                    } catch (Exception e) {
                        Toast.makeText(Home.this, "Для вашего города объявлений нет", Toast.LENGTH_SHORT).show();
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

    public void LogOut() {
        textViewLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, MainActivity.class));
        });
    }


    public void onBackPressed() {
        // super.onBackPressed();

    }

    private int getIndex(Spinner spinner, String City) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(City)) {
                index = i;
            }
        }
        return index;
    }


}
