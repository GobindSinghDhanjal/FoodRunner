package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.activity.OrderListActivity
import com.example.myapplication.database.RestaurantDatabase
import com.example.myapplication.database.RestaurantEntity
import com.example.myapplication.model.Restaurant
import com.squareup.picasso.Picasso


class HomePageAdapter(val context: Context, val restaurants: ArrayList<Restaurant>, val restaurantEntity: RestaurantEntity) : RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)

        return HomePageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {

        val res = restaurants.get(position)

        holder.txtRestaurantName.text = res.resName
        holder.txtCostPerPerson.text = ("₹ " + res.cost_for_one + "/per person")
        holder.txtRating.text = res.resRating
        Picasso.get().load(res.resImage).error(R.drawable.food).into(holder.imgRestaurantImage)

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()

        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(res.resId.toString())) {
            holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_favorite)
        } else {
            holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_not_favorite)
        }

        holder.btnFavourite.setOnClickListener {

            val restaurantEntity = RestaurantEntity(
                    res.resId,
                    res.resName,
                    res.resRating,
                    res.cost_for_one,
                    res.resImage
            )

            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async =
                        DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_favorite)
                }
            } else {
                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()

                if (result) {
                    holder.btnFavourite.setBackgroundResource(R.drawable.ic_vector_not_favorite)
                }
            }

        }

        holder.cardLayout.setOnClickListener {

            val intent = Intent(context,OrderListActivity::class.java)
            intent.putExtra("res_id",res.resId)
            intent.putExtra("res_name",res.resName)
            context.startActivity(intent)
        }


    }

    class HomePageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtCostPerPerson: TextView = view.findViewById(R.id.txtCostPerPerson)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        var btnFavourite: AppCompatButton = view.findViewById(R.id.btnFavorite)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val cardLayout : CardView = view.findViewById(R.id.cardLayout)

    }

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        //lateinit var restaurantEntity : RestaurantEntity

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {


            when (mode) {
                1 -> {
                    val res: RestaurantEntity? = db.restaurantDao().getResById(restaurantEntity.res_id.toString())
                    db.close()
                    return res != null
                }

                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

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


