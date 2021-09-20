package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.util.SessionManager
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var txtSignUp: TextView
    lateinit var txtForgetPssword: TextView
    lateinit var sharedpreferences: SharedPreferences
    lateinit var btnLogin: Button
    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //sharedpreferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)
        sessionManager = SessionManager(this)
        sharedpreferences = this.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)

        val isLoggedIn = sharedpreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtSignUp = findViewById(R.id.txtSignUp)
        txtForgetPssword = findViewById(R.id.txtForgetPassword)
        btnLogin = findViewById(R.id.btnLogIn)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)

        txtSignUp.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtForgetPssword.setOnClickListener {

            val intent2 = Intent(this, ForgetPassActivity::class.java)
            startActivity(intent2)
        }


        btnLogin.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNumber.text.toString())
            jsonParams.put("password", etPassword.text.toString())

            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,

                    Response.Listener {

                        try {
                            val dataObject = it.getJSONObject("data")
                            val success = dataObject.getBoolean("success")
                            println("Register $dataObject $success")
                            if (success) {
                                Toast.makeText(this, "Login Successully", Toast.LENGTH_SHORT).show()

                                val response = dataObject.getJSONObject("data")
                                sharedpreferences.edit()
                                        .putString("user_id", response.getString("user_id")).apply()
                                sharedpreferences.edit()
                                        .putString("user_name", response.getString("name")).apply()
                                sharedpreferences.edit()
                                        .putString(
                                                "user_mobile_number",
                                                response.getString("mobile_number")
                                        )
                                        .apply()
                                sharedpreferences.edit()
                                        .putString("user_address", response.getString("address"))
                                        .apply()
                                sharedpreferences.edit()
                                        .putString("user_email", response.getString("email")).apply()
                                sessionManager.setLogin(true)
                                val intent = Intent(this, MainActivity::class.java)
                                savePreferences()
                                startActivity(intent)
                                finish()
                            } else {
                                val errorMessage = dataObject.getString("errorMessage")
                                Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(this, "Some Error Occured2", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {

                Toast.makeText(this,"Volley Error Occured",Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "ce15cdd6dabe86"

                    return headers
                }


            }

            queue.add(jsonRequest)

        }

    }
    fun savePreferences() {
        sharedpreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}