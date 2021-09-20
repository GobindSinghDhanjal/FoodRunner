package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class FaqAdapter(val context : Context, val quesList : ArrayList<String>,val ansList : ArrayList<String>):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    class FaqViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtQues : TextView = view.findViewById(R.id.txtQues)
        val txtAns : TextView = view.findViewById(R.id.txtAns)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_faq_single_row,parent,false)

        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {

        val ques = quesList[position]
        val ans = ansList[position]

        holder.txtQues.text = ques
        holder.txtAns.text = ans

    }

    override fun getItemCount(): Int {
      return quesList.size
    }

}