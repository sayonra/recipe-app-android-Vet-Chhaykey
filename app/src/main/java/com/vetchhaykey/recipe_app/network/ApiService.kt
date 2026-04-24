package com.vetchhaykey.recipe_app.network

import com.vetchhaykey.recipe_app.model.Category
import com.vetchhaykey.recipe_app.model.Meal
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("meals")
    suspend fun getMeals(@Header("X-DB-NAME") dbName: String): retrofit2.Response<List<Meal>>

    @GET("categories")
    suspend fun getCategories(@Header("X-DB-NAME") dbName: String): retrofit2.Response<List<Category>>
}
