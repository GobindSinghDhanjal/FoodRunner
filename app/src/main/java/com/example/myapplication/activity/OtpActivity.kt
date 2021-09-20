package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {

    lateinit var etOtp : EditText
    lateinit var etNewPass : EditText
    lateinit var etNewPassConfirm : EditText
    lateinit var btnSubmit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val mobileNumber = intent.getStringExtra("mobile_number")


        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            etOtp = findViewById(R.id.etOtp)
            etNewPass = findViewById(R.id.etNewPass)
            etNewPassConfirm = findViewById(R.id.etNewPassConfirm)

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number",mobileNumber.toString())
            jsonParams.put("password",etNewPass.text.toString())
            jsonParams.put("otp",etNewPass.text.toString())

            val jsonRequest = object : JsonObjectRequest(Method.POST,url,jsonParams,

                Response.Listener {

                    try {

                        val dataObject = it.getJSONObject("data")
                        val success = dataObject.getBoolean("success")

                        if(success)
                        {
                            Toast.makeText(this,"Password Changed Successfully",Toast.LENGTH_SHORT).show()

                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val errorMessage = dataObject.getString("errorMessage")
                            Toast.makeText(this,"$errorMessage",Toast.LENGTH_SHORT).show()
                        }

                    }catch (e: Exception){
                        Toast.makeText(this,"Some Error Occured 23",Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {
                    Toast.makeText(this,"Volley Error Occured",Toast.LENGTH_SHORT).show()
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-Type"]="application/json"
                    headers["token"] = "ce15cdd6dabe86"

                    return headers
                }

            }
            queue.add(jsonRequest)
        }

    }
}