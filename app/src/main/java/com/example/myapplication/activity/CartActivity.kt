 package com.example.myapplication.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.adapter.CartAdapter
import com.example.myapplication.adapter.OrderListAdapter
import com.example.myapplication.database.DishDatabase
import com.example.myapplication.database.DishEntity
import com.example.myapplication.database.RestaurantDatabase
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

 class CartActivity : AppCompatActivity() {

    lateinit var recyclerAdapter : CartAdapter
    lateinit var recyclerCart : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var toolbar: Toolbar
    var dbDishList = listOf<DishEntity>()
    lateinit var btnPlaceOrder : Button
    var totalsum = 0
    lateinit var orderConfirm : RelativeLayout
    lateinit var btnDone : Button


    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbar = findViewById(R.id.toolbar)
        setToolbar()

        val gson=Gson()
        val foodItems=gson.toJson(dbDishList)


        val bundle = intent.getBundleExtra("data")
        val resId = bundle?.getInt("resId", 0) as Int
        val resName = bundle.getString("resName", "") as String

        btnDone = findViewById(R.id.btnDone)

        val listOfDish = OrderListAdapter.GetAllDishAsyncTask(this@CartActivity).execute().get()

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        orderConfirm = findViewById(R.id.orderConfirm)
        orderConfirm.visibility = View.GONE

        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this@CartActivity)



        dbDishList = RetrieveDish(this).execute().get()
        print("The list is $dbDishList")

        val itemList = dbDishList
        for (i in 0 until  itemList.size)
        {
            val dish = itemList[i]

            if (listOfDish.isNotEmpty() && listOfDish.contains(dish.dish_id.toString())) {
                totalsum = totalsum+dish.costForOne.toInt()
            }
        }

        val queue = Volley.newRequestQueue(this@CartActivity as Context)
        val url = "http://13.235.250.119/v2/place_order/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put(
            "user_id",
            this@CartActivity.getSharedPreferences("FoodApp", Context.MODE_PRIVATE).getString(
                "user_id",
                null
            ) as String
        )
        jsonParams.put("restaurant_id", OrderListActivity.resId?.toString() as String)
        var sum = 0
        for (i in 0 until dbDishList.size) {
            sum += dbDishList[i].costForOne as Int
        }
        jsonParams.put("total_cost", sum.toString())
        val foodArray = JSONArray()
        for (i in 0 until dbDishList.size) {
            val foodId = JSONObject()
            foodId.put("food_item_id", dbDishList[i].dish_id)
            foodArray.put(i, foodId)
        }
        jsonParams.put("food", foodArray)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonParams,
            Response.Listener {

                              try{
                              val data = it.getJSONObject("data")
                              val success = data.getBoolean("success")

                              if(success){

                                  val clearCart =
                                      ClearDBAsync(applicationContext, resId.toString()).execute().get()
                                  OrderListAdapter.isCartEmpty = true

                                  val dialog = Dialog(
                                      this@CartActivity,
                                      android.R.style.Theme_Black_NoTitleBar_Fullscreen
                                  )
                                  dialog.setContentView(orderConfirm)
                                  dialog.show()
                                  dialog.setCancelable(false)
                                  val btnOk = dialog.findViewById<Button>(R.id.btnDone)
                                  btnOk.setOnClickListener {
                                      dialog.dismiss()
                                      startActivity(Intent(this@CartActivity, MainActivity::class.java))
                                      ActivityCompat.finishAffinity(this@CartActivity)
                                  }

                              }else {
                                  Toast.makeText(this@CartActivity, "Some Error occurred", Toast.LENGTH_SHORT)
                                      .show()
                              }}
                              catch (e:Exception){
                                  e.printStackTrace()
                              }
                              },
            Response.ErrorListener {

                Toast.makeText(this@CartActivity, it.message, Toast.LENGTH_SHORT).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"]="application/json"
                headers["token"]="ce15cdd6dabe86"

                return headers

            }

        }
        queue.add(jsonObjectRequest)


        val totalPrice = totalsum.toString()
        btnPlaceOrder.text = "Place Order (Total Rs. $totalPrice)"


        if(dbDishList!=null && this!=null)
        {
            recyclerAdapter = CartAdapter(this,dbDishList)
            recyclerCart.adapter = recyclerAdapter
            recyclerCart.layoutManager = layoutManager
        }

        btnPlaceOrder.setOnClickListener {
            btnPlaceOrder.visibility = View.GONE
            orderConfirm.visibility = View.VISIBLE

        }

        btnDone.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }





    }

    fun setToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }  //SettingToolbar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id!=R.id.home)
        {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }  //ToolbarBack


    class RetrieveDish(val context: Context): AsyncTask<Void, Void, List<DishEntity>>()
    {
        override fun doInBackground(vararg params: Void?): List<DishEntity> {
            val db = Room.databaseBuilder(context, DishDatabase::class.java,"dish-db").build()

            return  db.dishDao().getAllDish()
        }

    }

     class ClearDBAsync(context: Context, val resId: String) : AsyncTask<Void, Void, Boolean>() {
         val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
         override fun doInBackground(vararg params: Void?): Boolean {
             db.dishDao().deleteDishes(resId)
             db.close()
             return true
         }

     }

}