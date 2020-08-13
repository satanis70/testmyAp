package com.vpiska.SqlLiteCity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class DatabaseCity extends RoomDatabase {
    public abstract CityDao cityDao();
}
