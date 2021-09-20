package com.example.myapplication.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.database.DishDatabase
import com.example.myapplication.database.DishEntity
import com.example.myapplication.model.Dish
import com.example.myapplication.model.Restaurant
import com.example.myapplication.model.Total


class OrderListAdapter(val context: Context, val itemList: ArrayList<Dish>, val dishEntity: DishEntity) : RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {


    companion object {
        var isCartEmpty = true
    }

    class OrderListViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val txtSerialNo : TextView = view.findViewById(R.id.txtSerialNo)
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishPrice : TextView = view.findViewById(R.id.txtDishPrice)
        val btnAdd : Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_order_list_single_row,parent,false)
        return OrderListViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {

        val dish = itemList[position]
        val serialNo = position+1
        holder.txtSerialNo.text = serialNo.toString()
        holder.txtDishName.text = dish.name
        holder.txtDishPrice.text = "Rs. ${dish.costForOne}"

        val listOfDish = GetAllDishAsyncTask(context).execute().get()

        if (listOfDish.isNotEmpty() && listOfDish.contains(dish.id.toString())) {
            holder.btnAdd.text = "Remove"
            val favColor = ContextCompat.getColor(context,R.color.yellow)
            holder.btnAdd.setBackgroundColor(favColor)
        } else {
            holder.btnAdd.text = "Add"
            val nofavColor = ContextCompat.getColor(context,R.color.orangePrimary)
            holder.btnAdd.setBackgroundColor(nofavColor)
        }

        holder.btnAdd.setOnClickListener {


            val dishEntity = DishEntity(
                    dish.id,
                    dish.name,
                    dish.costForOne,
                    dish.resId
            )


            if (!DBAsyncTask(context, dishEntity, 1).execute().get()) {
                val async =
                        DBAsyncTask(context, dishEntity, 2).execute()
                val result = async.get()

                if (result) {
                    holder.btnAdd.text = "Remove"
                    val favColor = ContextCompat.getColor(context,R.color.yellow)
                    holder.btnAdd.setBackgroundColor(favColor)

                }
            } else {
                val async = DBAsyncTask(context, dishEntity, 3).execute()
                val result = async.get()

                if (result) {
                    holder.btnAdd.text = "Add"
                    val nofavColor = ContextCompat.getColor(context,R.color.orangePrimary)
                    holder.btnAdd.setBackgroundColor(nofavColor)

                }
            }

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class DBAsyncTask(val context: Context, val dishEntity: DishEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val dish: DishEntity? = db.dishDao().getDishById(dishEntity.dish_id.toString())
                    db.close()
                    return dish != null
                }

                2 -> {
                    db.dishDao().insertDish(dishEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.dishDao().deleteDish(dishEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
    class GetAllDishAsyncTask(
            context: Context
    ) :
            AsyncTask<Void, Void, List<String>>() {

        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {

            val list = db.dishDao().getAllDish()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.dish_id.toString())
            }
            return listOfIds
        }
    }

}