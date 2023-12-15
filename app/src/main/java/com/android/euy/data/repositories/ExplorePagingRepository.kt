package com.android.euy.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.Recipe
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.services.ApiService
import com.android.euy.ui.paging.ExplorePagingSource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExplorePagingRepository @Inject constructor(private val api: ApiService) {
    private fun authorization(token: String) = "Bearer " + token

    fun getPagingStories(token: String, query: String) : Flowable<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5, maxSize = 20,
                enablePlaceholders = false),
            pagingSourceFactory = { ExplorePagingSource(token,api,query) }
        ).flowable
    }


}