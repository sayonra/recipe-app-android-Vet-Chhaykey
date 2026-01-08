package com.dum_sileang.recipe_app.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("id") val id: String,
    @SerializedName("meal") val name: String,                    // Changed from "strMeal"
    @SerializedName("category") val category: String,            // Changed from "strCategory"
    @SerializedName("area") val area: String? = null,           // Same
    @SerializedName("instructions") val instructions: String? = null, // Changed from "strInstructions"
    @SerializedName("mealThumb") val image: String,             // Changed from "strMealThumb"
    @SerializedName("tags") val tags: String? = null,           // Changed from "strTags"
    @SerializedName("youtube") val youtube: String? = null,     // Changed from "strYoutube"
    @SerializedName("ingredients") val ingredients: List<Ingredient>? = emptyList() // Added ingredients
)

data class Ingredient(
    @SerializedName("ingredient") val name: String,
    @SerializedName("measure") val measure: String
)