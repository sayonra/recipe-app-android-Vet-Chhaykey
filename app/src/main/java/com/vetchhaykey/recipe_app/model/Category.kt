package com.vetchhaykey.recipe_app.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,           // Check what your API uses
    @SerializedName("image") val image: String,         // Check what your API uses
    @SerializedName("description") val description: String? = null
)