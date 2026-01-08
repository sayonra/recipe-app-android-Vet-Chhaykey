package com.dum_sileang.recipe_app.network

import com.dum_sileang.recipe_app.model.Category
import com.dum_sileang.recipe_app.model.Meal
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("meals")
    suspend fun getMeals(@Header("X-DB-NAME") dbName: String): retrofit2.Response<List<Meal>>

    @GET("categories")
    suspend fun getCategories(@Header("X-DB-NAME") dbName: String): retrofit2.Response<List<Category>>
}