package com.vetchhaykey.recipe_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.vetchhaykey.recipe_app.model.Meal
import com.vetchhaykey.recipe_app.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null) {
            viewModel.loadMealsByCategory(selectedCategory!!)
        } else {
            viewModel.loadMeals()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        viewModel.loadMeals()
    }

    Column(Modifier.padding(16.dp)) {
        // Debug info
        DebugInfoView(error, isLoading, meals.size, categories.size)

        // Categories Filter Row
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Show categories loading or error
        when {
            isLoading && categories.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null && categories.isEmpty() -> {
                Text(
                    text = "Failed to load categories: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Add "All" filter option
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All") }
                        )
                    }

                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category.name,
                            onClick = {
                                selectedCategory = if (selectedCategory == category.name) {
                                    null
                                } else {
                                    category.name
                                }
                            },
                            label = { Text(category.name) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Meals List Section
        Text(
            text = if (selectedCategory != null) "$selectedCategory Meals" else "All Meals",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Show meals loading, error, or content
        when {
            isLoading && meals.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading meals...")
                    }
                }
            }
            error != null && meals.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❌ Error", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error ?: "Unknown error")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (selectedCategory != null) {
                                viewModel.loadMealsByCategory(selectedCategory!!)
                            } else {
                                viewModel.loadMeals()
                            }
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
            meals.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No meals found", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Check your API connection and GUID")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (selectedCategory != null) {
                                viewModel.loadMealsByCategory(selectedCategory!!)
                            } else {
                                viewModel.loadMeals()
                            }
                        }) {
                            Text("Retry Loading")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meals) { meal ->
                        MealCard(
                            meal = meal,
                            onMealClick = {
                                navController.navigate("detail/${meal.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DebugInfoView(
    error: String?,
    isLoading: Boolean,
    mealsCount: Int,
    categoriesCount: Int
) {
    if (error != null || isLoading) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (error != null) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Debug Info",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading: $isLoading")
                Text("Meals loaded: $mealsCount")
                Text("Categories loaded: $categoriesCount")
                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Error: $it", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun MealCard(meal: Meal, onMealClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMealClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = meal.image,
                contentDescription = meal.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = meal.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
