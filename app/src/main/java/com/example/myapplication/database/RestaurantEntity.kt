package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantEntity (

        @PrimaryKey var res_id:String,

        @ColumnInfo (name= "res_name")
    val resName:String,

        @ColumnInfo (name = "rating")
    val resRating:String,

        @ColumnInfo(name = "cost_per_person")
    val costPerPerson: String,

        @ColumnInfo(name = "image_url")
    val ImageUrl: String

)