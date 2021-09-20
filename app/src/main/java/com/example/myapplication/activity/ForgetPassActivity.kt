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
import java.lang.Exception

class ForgetPassActivity : AppCompatActivity() {

    lateinit var etMobileForget: EditText
    lateinit var etEmailForget: EditText
    lateinit var btnNextForget: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        btnNextForget = findViewById(R.id.btnNextForget)

        btnNextForget.setOnClickListener {

            etMobileForget = findViewById(R.id.etMobileForget)
            etEmailForget = findViewById(R.id.etEmailForget)

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileForget.text.toString())
            jsonParams.put("email", etEmailForget.text.toString())

            val jsonRequest = object : JsonObjectRequest(Method.POST, url, jsonParams,
                    Response.Listener {

                        try {
                            val dataObject = it.getJSONObject("data")
                            val success = dataObject.getBoolean("success")

                            if (success) {
                                Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this,OtpActivity::class.java)
                                intent.putExtra("mobile_number",etMobileForget.text.toString())
                                startActivity(intent)
                                finish()
                            } else {
                                val errorMessage = dataObject.getString("errorMessage")
                                Toast.makeText(this,errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this, "Volley Error Occured", Toast.LENGTH_SHORT).show()
                        }


                    }, Response.ErrorListener {


            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "ce15cdd6dabe86"

                    return headers
                }
                /*    val intent = Intent(this, EmptyActivity::class.java)
            intent.putExtra("MobileForget",etMobileForget.text.toString())
            intent.putExtra("EmailForget",etEmailForget.text.toString())
            intent.putExtra("ForgetPass",true)
            startActivity(intent)
            finish()*/

            }

            queue.add(jsonRequest)

        }
    }
}