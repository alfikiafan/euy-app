package com.android.euy.data.repositories

import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.OurChoicesResponse
import com.android.euy.data.model.Recipe
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.model.UserViewRecipeRequest
import com.android.euy.data.model.UserViewRecipeResponse
import com.android.euy.data.services.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val api: ApiService) {

    fun getRecipeFromIngredients(recipes: String,token:String): Single<CommonResponse<RecipeResponse>> {
        return api.getRecipesFromIngredients(recipes,token).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread()))
    }

    fun getRecipeFromName(recipes: String,token:String): Single<CommonResponse<RecipeResponse>> {
        return api.getRecipesFromName(recipes,token).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread()))
    }

    fun getOurChoices(token:String): Single<CommonResponse<OurChoicesResponse>> {
        return api.getOurChoices(token).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread()))
    }

    fun postViewedRecipe(req : UserViewRecipeRequest, token: String): Single<UserViewRecipeResponse>{
        return api.postViewedRecipe(token,req).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread()))
    }

}