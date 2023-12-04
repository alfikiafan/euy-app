package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: Button = findViewById(R.id.login)
        loginButton.setOnClickListener {
            val intentSignIn = Intent(this@MainActivity, SignIn::class.java)
            startActivity(intentSignIn)
        }

        val daftarButton: Button = findViewById(R.id.daftar)
        daftarButton.setOnClickListener {
            val intentSignUp = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intentSignUp)
        }
    }
}