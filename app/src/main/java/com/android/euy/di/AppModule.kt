package com.android.euy.di

import com.android.euy.data.repositories.ExplorePagingRepository
import com.android.euy.data.repositories.RecipeRepository
import com.android.euy.data.services.ApiService
import com.android.euy.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://euy.zavelin.com/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(apiService: ApiService): RecipeRepository {
        return RecipeRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideExplorePagingRepository(api: ApiService): ExplorePagingRepository {
        return ExplorePagingRepository(api)
    }
}