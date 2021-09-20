package com.example.myapplication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.volley.toolbox.Volley
import com.example.myapplication.R

class ProfileFragment : Fragment() {

    lateinit var profileName : TextView
    lateinit var profileNumber : TextView
    lateinit var profileEmail : TextView
    lateinit var profileAddress : TextView
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPrefs = (activity as FragmentActivity).getSharedPreferences("FoodApp", Context.MODE_PRIVATE)
        profileName = view.findViewById(R.id.txtProfileName)
        profileNumber = view.findViewById(R.id.txtProfileMobile)
        profileAddress = view.findViewById(R.id.txtAddress)
        profileEmail = view.findViewById(R.id.txtEmail)
        profileName.text = sharedPrefs.getString("user_name", null)
        val phoneText = "+91-${sharedPrefs.getString("user_mobile_number", null)}"
        profileNumber.text = phoneText
        profileEmail.text = sharedPrefs.getString("user_email", null)
        val address = sharedPrefs.getString("user_address", null)
        profileAddress.text = address

        return view
    }

}