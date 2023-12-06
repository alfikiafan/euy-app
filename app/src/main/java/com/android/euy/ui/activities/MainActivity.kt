package com.android.euy.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.android.euy.databinding.ActivityMainBinding
import com.android.euy.ui.viewmodels.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCurrentUser()?.let {
            startActivity(Intent(this@MainActivity,HomeActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
        }

    }
}