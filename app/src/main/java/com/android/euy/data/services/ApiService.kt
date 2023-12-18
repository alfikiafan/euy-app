package com.android.euy.data.services

import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.LoginSSORequest
import com.android.euy.data.model.LoginSSOResponse
import com.android.euy.data.model.OurChoicesResponse
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.model.UserViewRecipeRequest
import com.android.euy.data.model.UserViewRecipeResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/sso")
    fun login(@Body request: LoginSSORequest): Single<LoginSSOResponse>

    @POST("recipes/viewed")
    fun postViewedRecipe(@Header("X-Access-Token") token: String, @Body request: UserViewRecipeRequest): Single<UserViewRecipeResponse>

    @GET("recipes")
    fun getRecipesFromIngredients(@Query("ingredients") username: String, @Header("X-Access-Token") token: String)
    : Single<CommonResponse<RecipeResponse>>

    @GET("recipes")
    fun getRecipesFromName(@Query("search") username: String, @Header("X-Access-Token") token: String)
            : Single<CommonResponse<RecipeResponse>>

    @GET("recipes/our-choices")
    fun getOurChoices(@Header("X-Access-Token") token: String) : Single<CommonResponse<OurChoicesResponse>>

    @GET("recipes")
    fun getRecipes(@Query("pages") page: Int,@Header("X-Access-Token") token: String): Single<CommonResponse<RecipeResponse>>


}