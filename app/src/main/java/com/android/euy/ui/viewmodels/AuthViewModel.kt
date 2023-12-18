package com.android.euy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.euy.data.model.AuthResult
import com.android.euy.data.model.LoginSSORequest
import com.android.euy.data.model.LoginSSOResponse
import com.android.euy.data.repositories.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {
    private val auth = Firebase.auth
    val db = Firebase.firestore
    private val compositeDisposable = CompositeDisposable()
    private val _sso = MutableLiveData<LoginSSOResponse>()
    val sso: LiveData<LoginSSOResponse> = _sso

    fun signInWithSSO(uid: String, provider: String, email: String, name: String){
        compositeDisposable.add(
            repository.login(LoginSSORequest(uid,provider,email,name)).subscribe(
                {
                    Log.e("success", it.toString())
                    _sso.postValue(it)
                },
                {
                    Log.e("error", it.message.toString())
                }
            )
        )
    }

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


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}