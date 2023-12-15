package com.android.euy.ui.paging

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.android.euy.data.model.CommonResponse
import com.android.euy.data.model.Recipe
import com.android.euy.data.model.RecipeResponse
import com.android.euy.data.services.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExplorePagingSource (private val token: String, private val api: ApiService, private val query: String) : RxPagingSource<Int, Recipe>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Recipe>> {
        val page = params.key ?: 1
        if (query.isEmpty()){
            return api.getRecipes(page,token).subscribeOn(Schedulers.io())
                .map { toLoadResult(it.data.recipes, page) }
                .onErrorReturn { LoadResult.Error(it) }
        }else{
            return api.getRecipesFromName(query,token).subscribeOn(Schedulers.io())
                .map { toLoadResult(it.data.recipes, page) }
                .onErrorReturn { LoadResult.Error(it) }
        }

    }

    private fun toLoadResult(recipeList: List<Recipe>, page: Int): LoadResult<Int, Recipe> {
        Log.v("RESULT",recipeList.size.toString())
//        val filteredList = if (query.isNotEmpty()) {
//            recipeList.filter { recipe ->
//                recipe.name.contains(query, ignoreCase = true)
//            }
//        } else {
//            recipeList
//        }
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