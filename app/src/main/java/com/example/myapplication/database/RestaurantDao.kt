package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {



    @Insert
    fun insertRestaurant(restaurantEntity : RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurant")
    fun getAllRes() : List<RestaurantEntity>

    @Query("SELECT * FROM restaurant where res_id = :resId")
    fun getResById(resId : String) : RestaurantEntity

}