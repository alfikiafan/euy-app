package com.android.euy.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.euy.data.model.AuthResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth
    val db = Firebase.firestore

    fun signInWithGoogle(idToken: String): LiveData<AuthResult> {
        val resultLiveData = MutableLiveData<AuthResult>()

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.postValue(AuthResult.Success(auth.currentUser))
                } else {
                    resultLiveData.postValue(AuthResult.Error(task.exception))
                }
            }

        return resultLiveData
    }

    fun signInWithEmailAndPassword(email: String, password: String): LiveData<AuthResult> {
        val resultLiveData = MutableLiveData<AuthResult>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.postValue(AuthResult.Success(auth.currentUser))
                } else {
                    resultLiveData.postValue(AuthResult.Error(task.exception))
                }
            }

        return resultLiveData
    }

    fun registerWithEmailAndPassword(email: String, password: String): LiveData<AuthResult> {
        val resultLiveData = MutableLiveData<AuthResult>()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.postValue(AuthResult.Success(auth.currentUser))
                } else {
                    resultLiveData.postValue(AuthResult.Error(task.exception))
                }
            }

        return resultLiveData
    }

    fun getCurrentUser (): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut(){
        auth.signOut()
    }

    fun getUserFromDB(email:String): Task<QuerySnapshot> {
        return db.collection("users").whereEqualTo("email",email)
            .get()
    }
    fun addUserToDB(email: String, password: String, name: String): Task<DocumentReference> {
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )
        return db.collection("users")
            .add(user)

    }
}