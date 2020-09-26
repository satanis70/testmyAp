package com.vpiska.SqlLiteCity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCity(City city);

    @Query("SELECT * FROM cityes")
    Flowable<List<City>> getCity();

    @Update
    void update(City city);
}
