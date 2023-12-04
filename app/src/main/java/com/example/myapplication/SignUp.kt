package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class SignUp : AppCompatActivity() {

    private lateinit var namaEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var daftarButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        namaEditText = findViewById(R.id.namaEdit)
        emailEditText = findViewById(R.id.emailEdit)
        passwordEditText = findViewById(R.id.password)
        daftarButton = findViewById(R.id.daftar)

        daftarButton.setOnClickListener {
            val nama = namaEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val requestQueue = Volley.newRequestQueue(this)
            val url = "https://594e-158-140-167-20.ngrok-free.app/aplikasi_euy/register.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean("success")) {

                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SignIn::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nama_pengguna"] = nama
                    params["email_pengguna"] = email
                    params["password"] = password
                    return params
                }
            }

            requestQueue.add(stringRequest)
        }

    }
}