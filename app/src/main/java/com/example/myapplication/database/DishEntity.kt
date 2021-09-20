package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dish")
data class DishEntity (
        @PrimaryKey val dish_id:String,
        @ColumnInfo (name = "dish_name") val dishName:String,
        @ColumnInfo (name = "cost_for_one") val costForOne: String,
        @ColumnInfo (name = "res_id") val resId:String
        )