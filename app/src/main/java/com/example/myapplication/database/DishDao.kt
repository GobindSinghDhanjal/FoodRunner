package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DishDao {

    @Insert
    fun insertDish(dishEntity: DishEntity)

    @Delete
    fun deleteDish(dishEntity: DishEntity)

    @Query("SELECT * FROM dish")
    fun getAllDish() : List<DishEntity>

    @Query("SELECT * FROM dish where dish_id = :dishId")
    fun getDishById(dishId : String) : DishEntity

    @Query("DELETE FROM dish WHERE res_id = :resId")
    fun deleteDishes(resId: String)

/*
    @Query("SELECT cost_for_one FROM dish where dish_id =:dishId")
    fun totalPrice(dishId: String) : DishEntity*/
}