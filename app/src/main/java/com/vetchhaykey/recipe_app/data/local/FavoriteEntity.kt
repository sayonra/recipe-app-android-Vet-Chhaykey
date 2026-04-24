package com.vetchhaykey.recipe_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val image: String,
    val category: String,
    val instructions: String = "",
    val area: String = ""
)
