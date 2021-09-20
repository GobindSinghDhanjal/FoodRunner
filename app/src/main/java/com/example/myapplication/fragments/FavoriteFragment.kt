package com.example.myapplication.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.adapter.FavouriteRecyclerAdapter
import com.example.myapplication.database.RestaurantDatabase
import com.example.myapplication.database.RestaurantEntity


class FavoriteFragment : Fragment() {

    lateinit var recyclerFavourite : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    var dbResList = listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        layoutManager = GridLayoutManager(activity as Context,2)

        dbResList = RetrieveFavourites(activity as Context).execute().get()

        if(dbResList!=null && activity!=null)
        {
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbResList)

            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity>>()
    {
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"res-db").build()

            return  db.restaurantDao().getAllRes()
        }

    }

}