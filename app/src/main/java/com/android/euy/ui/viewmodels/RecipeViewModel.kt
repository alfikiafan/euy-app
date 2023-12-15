package com.android.euy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.LoginSSORequest
import com.android.euy.data.model.OurChoicesResponse
import com.android.euy.data.model.Recipe
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.repositories.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _recipeList = MutableLiveData<CommonResponse<RecipeResponse>>()
    val recipeList: LiveData<CommonResponse<RecipeResponse>> = _recipeList

    private val _searchedList = MutableLiveData<CommonResponse<RecipeResponse>>()
    val searchedList: LiveData<CommonResponse<RecipeResponse>> = _searchedList

    private val _ourChoicesList = MutableLiveData<CommonResponse<OurChoicesResponse>>()
    val ourChoicesList: LiveData<CommonResponse<OurChoicesResponse>> = _ourChoicesList

    fun getRecipesFromIngredients(listBahan : String,token:String){
        compositeDisposable.add(
            repository.getRecipeFromIngredients(listBahan,token).subscribe(
                {
                    Log.e("success", it.toString())
                    _recipeList.postValue(it)
                },
                {
                    Log.e("error", it.message.toString())
                }
            )
        )
    }

    fun getRecipesFromName(nama : String,token:String){
        compositeDisposable.add(
            repository.getRecipeFromName(nama,token).subscribe(
                {
                    Log.e("success", it.toString())
                    _searchedList.postValue(it)
                },
                {
                    Log.e("error", it.message.toString())
                }
            )
        )
    }


    fun getOurChoices(token:String){
        compositeDisposable.add(
            repository.getOurChoices(token).subscribe(
                {
                    Log.e("success", it.toString())
                    _ourChoicesList.postValue(it)
                },
                {
                    Log.e("error", it.message.toString())
                }
            )
        )
    }
}