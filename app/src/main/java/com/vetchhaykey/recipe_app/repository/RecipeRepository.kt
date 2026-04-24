package com.vetchhaykey.recipe_app.repository

import android.util.Log
import com.vetchhaykey.recipe_app.data.local.FavoriteDao
import com.vetchhaykey.recipe_app.data.local.FavoriteEntity
import com.vetchhaykey.recipe_app.model.Category
import com.vetchhaykey.recipe_app.model.Meal
import com.vetchhaykey.recipe_app.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val api: ApiService,
    private val dao: FavoriteDao
) {
    private val dbId = "d443dd4e-ac1d-4d5e-9885-cf05682f0ab9"
    private val TAG = "RecipeRepository"

    suspend fun getMeals(): List<Meal> {
        return try {
            val response = api.getMeals(dbId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e(TAG, "❌ HTTP Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ API CALL FAILED!", e)
            emptyList()
        }
    }

    suspend fun getCategories(): List<Category> {
        return try {
            val response = api.getCategories(dbId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e(TAG, "❌ HTTP Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ API CALL FAILED!", e)
            emptyList()
        }
    }

    suspend fun getMealsByCategory(category: String): List<Meal> {
        val allMeals = getMeals()
        val filtered = allMeals.filter { it.category.equals(category, ignoreCase = true) }
        Log.d(TAG, "🔍 Filtered ${filtered.size} meals for category: $category")
        return filtered
    }

    fun getFavorites(): Flow<List<FavoriteEntity>> = dao.getAllFavorites()
    suspend fun addFavorite(meal: FavoriteEntity) = dao.addFavorite(meal)
    suspend fun removeFavorite(meal: FavoriteEntity) = dao.removeFavorite(meal)
}
