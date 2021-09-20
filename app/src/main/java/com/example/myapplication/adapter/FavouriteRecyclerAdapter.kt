package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.activity.OrderListActivity
import com.example.myapplication.database.RestaurantDatabase
import com.example.myapplication.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val restaurantList : List<RestaurantEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val txtCostPerPerson : TextView = view.findViewById(R.id.txtCostPerPerson)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
        val btnFavourite : AppCompatButton = view.findViewById(R.id.btnFavorite)
        val imgRestaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val FavCard : CardView = view.findViewById(R.id.FavCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)

        return FavouriteViewHolder(view)

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val restaurant = restaurantList.get(position)

        holder.txtRestaurantName.text = restaurant.resName
        holder.txtRating.text = restaurant.resRating
        holder.txtCostPerPerson.text = ("₹ "+restaurant.costPerPerson+"/per person")
        Picasso.get().load(restaurant.ImageUrl).into(holder.imgRestaurantImage)

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()

        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(restaurant.res_id.toString())) {
            holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_favorite)
        } else {
            holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_not_favorite)
        }

        holder.btnFavourite.setOnClickListener {

            val restaurantEntity = RestaurantEntity(
                    restaurant.res_id,
                    restaurant.resName,
                    restaurant.resRating,
                    restaurant.costPerPerson,
                    restaurant.ImageUrl
            )

            if (!HomePageAdapter.DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async =
                        HomePageAdapter.DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_favorite)
                }
            } else {
                val async = HomePageAdapter.DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()

                if (result) {
                    holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_not_favorite)
                }
            }

        }

        holder.FavCard.setOnClickListener {
            val intent = Intent(context, OrderListActivity::class.java)
            intent.putExtra("res_id",restaurant.res_id)
            intent.putExtra("res_name",restaurant.resName)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class GetAllFavAsyncTask(
            context: Context
    ) :
            AsyncTask<Void, Void, List<String>>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {

            val list = db.restaurantDao().getAllRes()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.res_id.toString())
            }
            return listOfIds
        }
    }

}