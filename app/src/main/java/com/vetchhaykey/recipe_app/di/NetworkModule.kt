package com.vetchhaykey.recipe_app.di

import android.content.Context
import androidx.room.Room
import com.vetchhaykey.recipe_app.data.local.FavoriteDao
import com.vetchhaykey.recipe_app.data.local.FavoriteDatabase
import com.vetchhaykey.recipe_app.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://meal-db-sandy.vercel.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase =
        Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite_db"
        ).build()

    @Provides
    fun provideFavoriteDao(db: FavoriteDatabase): FavoriteDao = db.favoriteDao()
}
