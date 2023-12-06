package com.android.euy.data.model

import com.google.firebase.auth.FirebaseUser

sealed class AuthResult {
    data class Success(val user: FirebaseUser?) : AuthResult()
    data class Error(val exception: Exception?) : AuthResult()
}
