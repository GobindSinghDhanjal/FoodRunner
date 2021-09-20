package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

class EmptyActivity : AppCompatActivity() {

    lateinit var txtEmptyText : TextView
    var MobileNumber: String? = null
    var Password: String? = null
    var LoginTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        txtEmptyText = findViewById(R.id.txtEmptyText)




        MobileNumber = intent.getStringExtra("MobileNumber")
        Password = intent.getStringExtra("Password")
        LoginTrue = intent.getBooleanExtra("LoginTrue", false)
        val RegisterTrue = intent.getBooleanExtra("RegisterTrue", false)
        val Forgetpass = intent.getBooleanExtra("ForgetPass",false)

        if (LoginTrue) {
            Toast.makeText(
                this@EmptyActivity,
                "Welcome",
                Toast.LENGTH_LONG
            ).show()

            txtEmptyText.text = "Mobile Number : $MobileNumber\nPassword : $Password"
        }
        else if (RegisterTrue)
        {
            txtEmptyText.text =
                "Name : " + intent.getStringExtra("Name") + "\nEmail : " + intent.getStringExtra("Email") + "\nPhone : " + intent.getStringExtra("Phone") + "\nDelivery Address : " + intent.getStringExtra("DeliveryAdd")
        }
        else if(Forgetpass)
        {
            txtEmptyText.text = "Mobile : ${intent.getStringExtra("MobileForget")} \nEmail : ${intent.getStringExtra("EmailForget")}"
        }
        else
        {
            txtEmptyText.text = "No Value"
        }

    }

}