package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var Masuk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEditText = findViewById(R.id.emailEdit)
        passwordEditText = findViewById(R.id.password)
        Masuk = findViewById(R.id.daftar)


        val daftarButton: TextView = findViewById(R.id.buatakunbaru)
        daftarButton.setOnClickListener {
            val intentSignUp = Intent(this@SignIn, SignUp::class.java)
            startActivity(intentSignUp)
        }

        val masukButton: Button = findViewById(R.id.daftar)
        masukButton.setOnClickListener {
            val intent = Intent(this, MenuUtama::class.java)
            startActivity(intent)
        }


//        Masuk.setOnClickListener {
//            val email = emailEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            val requestQueue = Volley.newRequestQueue(this)
//            val url = "https://594e-158-140-167-20.ngrok-free.app/aplikasi_euy/login.php"
//
//            val stringRequest = object : StringRequest(
//                Request.Method.POST, url,
//                Response.Listener<String> { response ->
//                    try {
//                        val jsonObject = JSONObject(response)
//                        if (jsonObject.getBoolean("success")) {
//                            // Login berhasil
//                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, MenuUtama::class.java)
////                            startActivity(intent)
//                            finish()
//                        } else {
//                            // Login gagal
//                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                },
//                Response.ErrorListener { error ->
//                    Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
//                }
//            ) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["email_pengguna"] = email
//                    params["password"] = password
//                    return params
//                }
//            }
//
//            requestQueue.add(stringRequest)
//        }

    }
}