package com.example.myapplication.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.adapter.HomePageAdapter
import com.example.myapplication.database.RestaurantDatabase
import com.example.myapplication.database.RestaurantEntity
import com.example.myapplication.model.Restaurant
import kotlin.collections.HashMap

class HomePageFragment : Fragment() {

    lateinit var recyclerHomePage : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter : HomePageAdapter
    var RestaurantInfoList = arrayListOf<Restaurant>()
    lateinit var progressBarLayout : RelativeLayout
    lateinit var progressBar : ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result"

        recyclerHomePage = view.findViewById(R.id.recyclerHomePage)
        layoutManager = LinearLayoutManager(activity)

        progressBarLayout = view.findViewById(R.id.progressBarLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE


        val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {

               // println("Response is A")
                val dataObject = it.getJSONObject("data")
               // println("Response is B")
                val success = dataObject.getBoolean("success")
                //println("Response is C")

                try{

                    if (success) {

                        progressBarLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE

                        val data = dataObject.getJSONArray("data")

                        for (i in 0 until data.length()) {
                            val restaurantJsonObject = data.getJSONObject(i)

                             val restaurantObject = Restaurant(
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("cost_for_one"),
                                restaurantJsonObject.getString("image_url")
                            )
                            RestaurantInfoList.add(restaurantObject)

                            val restaurantEntity = RestaurantEntity(
                                    restaurantJsonObject.getString("id"),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    restaurantJsonObject.getString("cost_for_one"),
                                    restaurantJsonObject.getString("image_url")
                            )

                            recyclerAdapter = HomePageAdapter(activity as Context, RestaurantInfoList,restaurantEntity)
                            recyclerHomePage.adapter = recyclerAdapter
                            recyclerHomePage.layoutManager = layoutManager


                        }
                    } else {
                        Toast.makeText(activity as Context, "Some error Occured", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: Exception)
                {
                    Toast.makeText(activity as Context, "Some error Occured(Catch)", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {

                if(activity!=null)
                {
                    Toast.makeText(activity as Context,"Volley Error Occured",Toast.LENGTH_SHORT).show()
                }

            }) {

            override fun getHeaders(): MutableMap<String, String> {

                val headers = HashMap<String,String>()

                headers["Content-Type"] = "application/json"
                headers["token"] = "ce15cdd6dabe86"

                return headers
            }
        }

        queue.add(jsonObjectRequest)

        return view
    }

}