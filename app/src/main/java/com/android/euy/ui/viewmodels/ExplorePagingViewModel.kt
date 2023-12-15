package com.android.euy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.MessageResponse
import com.android.euy.data.model.Recipe
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.repositories.ExplorePagingRepository
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ExplorePagingViewModel @Inject constructor(private val repository: ExplorePagingRepository): ViewModel() {
    val compositeDisposable = CompositeDisposable()
    private val _recipeList = MutableLiveData<PagingData<Recipe>>()
    private val recipeList: LiveData<PagingData<Recipe>> = _recipeList



    @OptIn(ExperimentalCoroutinesApi::class)
    fun getExplorePaging(token: String,query: String): LiveData<PagingData<Recipe>> {
        compositeDisposable.add(repository.getPagingStories(token,query).cachedIn(viewModelScope).subscribe(
            {
                _recipeList.postValue(it)
            },
            {
                val error = it as HttpException
                val gson = GsonBuilder().create()
                try {
                    val errorResponse = gson.fromJson(error.response()?.errorBody()!!.string(), MessageResponse::class.java)
                    Log.e("error", errorResponse.message)
                } catch (_: IOException) {
                }
            })
        )
        return recipeList
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}