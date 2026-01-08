package com.dum_sileang.recipe_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dum_sileang.recipe_app.ui.viewmodel.RecipeViewModel

@Composable
fun DetailScreen(
    mealId: String,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    // Collect the meal state from ViewModel
    val meal by viewModel.selectedMeal.collectAsState() // Changed from mealState to selectedMeal
    val isFavorite by viewModel.isFavoriteState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load meal when screen composable or mealId changes
    LaunchedEffect(mealId) {
        viewModel.loadMealDetail(mealId) // Changed from loadMeals to loadMealDetail
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (meal == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Meal not found")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = meal!!.image,
                contentDescription = meal!!.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = meal!!.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: ${meal!!.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meal!!.instructions ?: "No instructions available",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFavorite) {
                        viewModel.removeFromFavorites(meal!!) // Fixed ambiguity
                    } else {
                        viewModel.addToFavorites(meal!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
            }
        }
    }
}

