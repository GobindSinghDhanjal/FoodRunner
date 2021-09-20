package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DishEntity

class CartAdapter(val context: Context , val itemList: List<DishEntity>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var TotalPrice : Int = 0

    class CartViewHolder(view : View): RecyclerView.ViewHolder(view) {

        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishPrice : TextView = view.findViewById(R.id.txtDishPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_single_row,parent,false)

        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val dish = itemList[position]

        holder.txtDishName.text = dish.dishName
        holder.txtDishPrice.text = "Rs. ${dish.costForOne}"

        TotalPrice = TotalPrice+dish.costForOne.toInt()

/*        sharedPreferences = context.getSharedPreferences((R.string.preference_total_price).toString(),Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("total",TotalPrice).apply()
        val get = sharedPreferences.getInt("total",0)
        println("Total Shared adapter $get")*/


    }

    override fun getItemCount(): Int {
       return itemList.size
    }

}