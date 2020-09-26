package com.vpiska.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vpiska.R;
import com.vpiska.SqlLiteCity.DatabaseCity;
import com.vpiska.adModel.CustomAdapterRecycler;

public class MyAdActivity extends AppCompatActivity {

    RecyclerView recyclerViewMyAd;
    CustomAdapterRecycler customAdapterRecycler;
    public static DatabaseCity databaseCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ad_activity);
        recyclerViewMyAd = findViewById(R.id.recycler_myAd);
        recyclerViewMyAd.setAdapter(customAdapterRecycler);
        databaseCity = Room.databaseBuilder(getApplicationContext(), DatabaseCity.class, "cityDb").build();

    }
}
