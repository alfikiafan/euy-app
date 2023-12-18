package com.android.euy.ui.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.android.euy.data.model.Recipe
import com.android.euy.data.services.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExplorePagingSource (private val token: String, private val api: ApiService, private val query: String) : RxPagingSource<Int, Recipe>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Recipe>> {
        val page = params.key ?: 1

        val apiCall = if (query.isEmpty()) {
            api.getRecipes(page, token)
        } else {
            api.getRecipesFromName(query, token)
        }

        return apiCall
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // observe on the main thread
            .map { toLoadResult(it.data.recipes, page) }
            .onErrorReturn { LoadResult.Error(it) }

    }

    private fun toLoadResult(recipeList: List<Recipe>, page: Int): LoadResult<Int, Recipe> {
        return LoadResult.Page(
            data = recipeList,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (recipeList.isEmpty()) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition
    }

}