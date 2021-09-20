package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.FaqAdapter


class FaqFragment : Fragment() {

    lateinit var recyclerFaq : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FaqAdapter

    var quesList = arrayListOf<String>("Q1. What are Food Hygiene Ratings?","Q2. Who runs the Hygiene Ratings initiative?")
    var ansList = arrayListOf<String>("Ans. The Hygiene Ratings system will help consumers choose where to eat from by giving them information about the hygiene standards. It isn’t easy to judge hygiene standards by appearances alone, so the rating gives you some idea of what’s going on in the kitchen, or behind closed doors.","Ans. We run the Hygiene Ratings initiative in partnership with third-party auditors from accredited & reputable companies.")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faq, container, false)


        quesList.add("Q3. What do the different Food Hygiene Ratings mean?")
        ansList.add("Ans. Excellent - The highest expected level of hygiene practices\n" +
                "\n" +
                "Very good - Very good hygiene practices. Minor areas need to be addressed.\n" +
                "\n" +
                "Good - Good general standard of hygiene. Some non-critical areas need attention.\n" +
                "\n" +
                "Unsatisfactory or Poor - A business with major hygiene issues. Needs to go through an improvement plan.")

        quesList.add("Q4. What kinds of restaurants can get a Hygiene Rating?")
        ansList.add("Ans. Any restaurant that has a kitchen area where food is prepared can apply for a Hygiene Rating. This may include:\n" +
                "\n" +
                "Fine Dining\n" +
                "\n" +
                "Lounge / Pub / Bar/ Micro brewery/ Cocktail bar / Club\n" +
                "\n" +
                "Casual dining\n" +
                "\n" +
                "Cafe\n" +
                "\n" +
                "Delivery Only\n" +
                "\n" +
                "Bakery / Beverage Shop / Food truck / Kiosk / Dessert parlor")



        recyclerFaq = view.findViewById(R.id.recyclerFaq)
        layoutManager = LinearLayoutManager(activity)


        recyclerAdapter = FaqAdapter(activity as Context,quesList,ansList)
        recyclerFaq.adapter = recyclerAdapter
        recyclerFaq.layoutManager = layoutManager

        recyclerFaq.addItemDecoration(
                DividerItemDecoration(
                        recyclerFaq.context,
                        (layoutManager as LinearLayoutManager).orientation
                )
        )


        return view
    }

}