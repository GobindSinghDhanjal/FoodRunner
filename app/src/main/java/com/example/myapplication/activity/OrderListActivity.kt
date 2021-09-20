package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.adapter.FavouriteRecyclerAdapter
import com.example.myapplication.adapter.OrderListAdapter
import com.example.myapplication.database.DishEntity
import com.example.myapplication.database.RestaurantDatabase
import com.example.myapplication.database.RestaurantEntity
import com.example.myapplication.model.Dish

class  OrderListActivity : AppCompatActivity() {

    var dishInfoList = arrayListOf<Dish>()
    lateinit var recyclerAdapter : OrderListAdapter
    lateinit var recyclerOrder : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    var resId : String?="100"
    lateinit var toolbar: Toolbar
    lateinit var imgFavourite : ImageView
    lateinit var progressBarLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var btnProceedToCart : Button

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var goToCart: Button
        var resId: Int? = 0
        var resName: String? = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        recyclerOrder = findViewById(R.id.recyclerOrderList)
        layoutManager = LinearLayoutManager(this@OrderListActivity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgFavourite = findViewById(R.id.imgFavorite)

        progressBarLayout = findViewById(R.id.progressBarLayout)
        progressBar = findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        btnProceedToCart = findViewById(R.id.btnProceedToCart)

        if(intent!=null)
        {
            var resName = intent.getStringExtra("res_name")
            resId = intent.getStringExtra("res_id")
            supportActionBar?.title = resName
        }else{
            Toast.makeText(this@OrderListActivity,"Some Unexpected Error Occured",Toast.LENGTH_SHORT).show()
        }

        if(resId=="100")
        {
            Toast.makeText(this@OrderListActivity,"Some Unexpected Error Occured",Toast.LENGTH_SHORT).show()
        }


        val queue = Volley.newRequestQueue(this@OrderListActivity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${resId}"


        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {

                    val dataObject = it.getJSONObject("data")
                    val success = dataObject.getBoolean("success")

                    if(success)
                    {
                        progressBarLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE

                        val data = dataObject.getJSONArray("data")

                        for(i in 0 until data.length())
                        {
                            val dishJsonObject = data.getJSONObject(i)

                            val dishObject = Dish(
                                    dishJsonObject.getString("id"),
                                    dishJsonObject.getString("name"),
                                    dishJsonObject.getString("cost_for_one"),
                                    dishJsonObject.getString("restaurant_id")
                            )
                            dishInfoList.add(dishObject)

                            val dishEntity = DishEntity(
                                    dishJsonObject.getString("id"),
                                    dishJsonObject.getString("name"),
                                    dishJsonObject.getString("cost_for_one"),
                                    dishJsonObject.getString("restaurant_id")
                            )


                            recyclerAdapter = OrderListAdapter(this@OrderListActivity,dishInfoList,dishEntity)
                            recyclerOrder.adapter = recyclerAdapter
                            recyclerOrder.layoutManager = layoutManager

                            favourites()

                            btnProceedToCart.setOnClickListener {
                                val intent = Intent(this,CartActivity::class.java)
                                startActivity(intent)
                                //finish()
                            }

                        }

                    }else{
                        Toast.makeText(this@OrderListActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()

                    }

                },Response.ErrorListener {

            Toast.makeText(this@OrderListActivity,"Volley Error Occured",Toast.LENGTH_SHORT).show()


        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "ce15cdd6dabe86"
                return headers
            }

        }
        queue.add(jsonObjectRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id!=R.id.home)
        {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun favourites(){

        val listOfFavourites = FavouriteRecyclerAdapter.GetAllFavAsyncTask(this).execute().get()


        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(resId)) {
           imgFavourite.setImageResource(R.drawable.ic_vector_favorite)
        } else {
           imgFavourite.setImageResource(R.drawable.ic_vector_not_favorite)
        }


    }

}