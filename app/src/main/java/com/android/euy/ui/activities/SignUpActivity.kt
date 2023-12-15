package com.android.euy.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.android.euy.R
import com.android.euy.data.model.AuthResult
import com.android.euy.databinding.ActivitySignUpBinding
import com.android.euy.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailPassword.txtHaveAccount1.text = "Sudah punya akun?"
        binding.emailPassword.txtHaveAccount2.text = "Masuk sekarang"
        binding.emailPassword.btnSignManual.text = "Sign up"
        binding.emailPassword.btnSignGoogle.text = "SignUp dengan Google"

        binding.emailPassword.txtHaveAccount2.setOnClickListener {
            finish()
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }

        binding.emailPassword.btnSignGoogle.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            signInWithGoogle()
        }

        binding.emailPassword.btnSignManual.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.emailPassword.edtTxtEmail.text.toString()
            val password = binding.emailPassword.edtTxtPassword.text.toString()
            val nama = binding.edtTxtNama.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@SignUpActivity,"Email or password can't be empty",Toast.LENGTH_SHORT).show()
            }else{
                if (binding.emailPassword.edtTxtEmail.error== null
                    && binding.emailPassword.edtTxtPassword.error == null && binding.edtTxtNama.error==null){
                    viewModel.registerWithEmailAndPassword(email, password)
                        .observe(this) { result ->
                            when (result) {
                                is AuthResult.Success -> {
                                    // Handle successful sign-in
                                    binding.progressBar.visibility = View.GONE
                                    viewModel.addUserToDB(email,password,nama).addOnSuccessListener {
                                        startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
                                    }
                                }

                                is AuthResult.Error -> {
                                    // Handle sign-in error
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this@SignUpActivity.applicationContext,result.exception?.localizedMessage,Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }

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
                                    binding.progressBar.visibility = View.GONE
                                    startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                                }

                                is AuthResult.Error -> {
                                    // Handle sign-in error
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this@SignUpActivity.applicationContext,result.exception?.localizedMessage,
                                        Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
            } catch (e: ApiException) {
                // Handle exception
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@SignUpActivity.applicationContext,e.localizedMessage,
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}