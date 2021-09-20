package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    lateinit var btnBackRegister : Button
    lateinit var btnRegister : Button
    lateinit var etName : EditText
    lateinit var etEmail : EditText
    lateinit var etPhone : EditText
    lateinit var etDeliveryAdd : EditText
    lateinit var etPasswordRegister : EditText
    lateinit var etConfirmPasswordRegister : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnBackRegister = findViewById(R.id.btnBackRegister)
        btnRegister = findViewById(R.id.btnRegister)


        btnBackRegister.setOnClickListener {

            val back = Intent(this, LoginActivity::class.java)
            startActivity(back)
            finish()

        }



        btnRegister.setOnClickListener {

            etName = findViewById(R.id.etName)
            etPhone = findViewById(R.id.etPhone)
            etDeliveryAdd = findViewById(R.id.etDeliveryAdd)
            etPasswordRegister = findViewById(R.id.etPasswordRegister)
            etConfirmPasswordRegister = findViewById(R.id.etConfirmPasswordRegister)
            etEmail = findViewById(R.id.etEmail)



            if((etPasswordRegister.text.toString()==etConfirmPasswordRegister.text.toString()))
            {

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("name",etName.text.toString())
                jsonParams.put("mobile_number",etPhone.text.toString())
                jsonParams.put("password",etPasswordRegister.text.toString())
                jsonParams.put("address",etDeliveryAdd.text.toString())
                jsonParams.put("email",etEmail.text.toString())


                val jsonRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonParams,
                        Response.Listener {

                                          try {
                                              val dataObject = it.getJSONObject("data")
                                              val success = dataObject.getBoolean("success")
                                              println("Register $dataObject $success")
                                              if(success)
                                              {
                                                  println("Register Done")
                                              }else{
                                                  val errorMessage = dataObject.getString("errorMessage")
                                                  Toast.makeText(this,"$errorMessage",Toast.LENGTH_SHORT).show()
                                              }
                                          }catch (e: Exception)
                                          {
                                              Toast.makeText(this,"Some Error Occured2",Toast.LENGTH_SHORT).show()
                                          }

                        },
                        Response.ErrorListener {

                            Toast.makeText(this,"Volley Error Occured",Toast.LENGTH_SHORT).show()
                        }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "ce15cdd6dabe86"

                        return headers
                    }


                }

                queue.add(jsonRequest)

               /* val intentRegister = Intent(this, EmptyActivity::class.java)
                startActivity(intentRegister)
                finish()*/
            }
            else
            {
                Toast.makeText(this,"Password Doesn't match",Toast.LENGTH_LONG).show()
            }

        }

    }
}