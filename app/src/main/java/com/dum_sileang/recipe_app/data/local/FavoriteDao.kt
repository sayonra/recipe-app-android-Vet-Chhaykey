package com.dum_sileang.recipe_app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(meal: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(meal: FavoriteEntity)

    @Query("SELECT * FROM favorites WHERE id = :mealId")
    suspend fun getFavoriteById(mealId: String): FavoriteEntity?
}