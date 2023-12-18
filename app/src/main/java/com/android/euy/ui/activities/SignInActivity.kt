package com.android.euy.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.android.euy.R
import com.android.euy.data.model.AuthResult
import com.android.euy.databinding.ActivitySignInBinding
import com.android.euy.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("EUYPref",Context.MODE_PRIVATE)

        binding.emailPassword.btnSignManual.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.emailPassword.edtTxtEmail.text.toString()
            val password = binding.emailPassword.edtTxtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@SignInActivity,"Email or password can't be empty",Toast.LENGTH_SHORT).show()
            }else{
                if (binding.emailPassword.edtTxtEmail.error== null && binding.emailPassword.edtTxtPassword.error == null){
                    viewModel.signInWithEmailAndPassword(email, password)
                        .observe(this) { result ->
                            when (result) {
                                is AuthResult.Success -> {
                                    // Handle successful sign-in
                                    result.user?.let {
                                        viewModel.getUserFromDB(email).addOnSuccessListener {
                                            for (document in it) {
                                                val name = document.getString("name")
                                                if (name != null) {
                                                    viewModel.signInWithSSO(result.user.uid,"email",email,name)
                                                }
                                                break
                                            }
                                        }
//
                                    }
//                                    binding.progressBar.visibility = View.GONE
//                                    startActivity(Intent(this@SignInActivity,HomeActivity::class.java))
                                }

                                is AuthResult.Error -> {
                                    // Handle sign-in error
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this@SignInActivity.applicationContext,result.exception?.localizedMessage,Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
            }
        }
        binding.emailPassword.txtHaveAccount2.setOnClickListener {
            finish()
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        binding.emailPassword.btnSignGoogle.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            signInWithGoogle()
        }

        viewModel.sso.observe(this) {
            if (it != null) {
                sharedPreferences.edit().putString("token", it.accessToken).apply()
                sharedPreferences.edit().putString("userId", it.data.sso.userId).apply()
                binding.progressBar.visibility = View.GONE
                startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
            } else {
                Toast.makeText(this@SignInActivity.applicationContext,"Error fetching token",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            binding.progressBar.visibility = View.GONE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                binding.progressBar.visibility = View.VISIBLE
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken:String ->
                    viewModel.signInWithGoogle(idToken)
                        .observe(this) { result ->
                            when (result) {
                                is AuthResult.Success -> {
                                    // Handle successful sign-in
                                    result.user?.let {
                                        viewModel.signInWithSSO(result.user.uid,"email",
                                            result.user.email.toString(), result.user.displayName.toString()
                                        )
                                    }
//                                    binding.progressBar.visibility = View.GONE
//                                    startActivity(Intent(this@SignInActivity,HomeActivity::class.java))
                                }

                                is AuthResult.Error -> {
                                    // Handle sign-in error
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this@SignInActivity.applicationContext,result.exception?.localizedMessage,Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
            } catch (e: ApiException) {
                // Handle exception
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@SignInActivity.applicationContext,e.localizedMessage,
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}