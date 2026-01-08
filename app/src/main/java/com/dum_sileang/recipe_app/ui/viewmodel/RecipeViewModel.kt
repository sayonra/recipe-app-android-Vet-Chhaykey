package com.dum_sileang.recipe_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dum_sileang.recipe_app.data.local.FavoriteEntity
import com.dum_sileang.recipe_app.model.Category
import com.dum_sileang.recipe_app.model.Meal
import com.dum_sileang.recipe_app.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repo: RecipeRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _selectedMeal = MutableStateFlow<Meal?>(null)
    val selectedMeal: StateFlow<Meal?> = _selectedMeal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isFavoriteState = MutableStateFlow(false)
    val isFavoriteState: StateFlow<Boolean> = _isFavoriteState.asStateFlow()

    val favorites = repo.getFavorites()

    fun loadMeals() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            _meals.value = repo.getMeals()
            if (_meals.value.isEmpty()) {
                _error.value = "No meals found. Check your GUID and API connection."
            }
        } catch (e: Exception) {
            _error.value = "Failed to load meals: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadCategories() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
//            _categories.value = repo.getCategories()
            if (_categories.value.isEmpty()) {
                _error.value = "No categories found. Check your GUID and API connection."
            }
        } catch (e: Exception) {
            _error.value = "Failed to load categories: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadMealsByCategory(category: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            _meals.value = repo.getMealsByCategory(category)
        } catch (e: Exception) {
            _error.value = "Failed to load meals by category: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun loadMealDetail(mealId: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val mealFromCache = _meals.value.find { it.id == mealId }
            if (mealFromCache != null) {
                _selectedMeal.value = mealFromCache
            } else {
                // If not in cache, load all meals and try again
                val allMeals = repo.getMeals()
                _selectedMeal.value = allMeals.find { it.id == mealId }
                if (_selectedMeal.value == null) {
                    _error.value = "Meal not found"
                }
            }
            checkFavoriteStatus(mealId)
        } catch (e: Exception) {
            _error.value = "Failed to load meal details: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun checkFavoriteStatus(mealId: String) {
        val favList = repo.getFavorites().first()
        _isFavoriteState.value = favList.any { it.id == mealId }
    }

    fun addToFavorites(meal: Meal) = viewModelScope.launch {
        try {
            val fav = FavoriteEntity(
                id = meal.id,
                name = meal.name,
                image = meal.image,
                category = meal.category,
                instructions = meal.instructions ?: "",
                area = meal.area ?: ""
            )
            repo.addFavorite(fav)
            _isFavoriteState.value = true
        } catch (e: Exception) {
            _error.value = "Failed to add to favorites: ${e.message}"
        }
    }

    fun removeFromFavorites(meal: Meal) = viewModelScope.launch {
        try {
            val fav = FavoriteEntity(
                id = meal.id,
                name = meal.name,
                image = meal.image,
                category = meal.category,
                instructions = meal.instructions ?: "",
                area = meal.area ?: ""
            )
            repo.removeFavorite(fav)
            _isFavoriteState.value = false
        } catch (e: Exception) {
            _error.value = "Failed to remove from favorites: ${e.message}"
        }
    }

    fun removeFromFavorites(favorite: FavoriteEntity) = viewModelScope.launch {
        repo.removeFavorite(favorite)
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSelectedMeal() {
        _selectedMeal.value = null
        _isFavoriteState.value = false
    }
}