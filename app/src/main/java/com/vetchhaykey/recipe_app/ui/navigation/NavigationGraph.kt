package com.vetchhaykey.recipe_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vetchhaykey.recipe_app.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("explore") { ExploreScreen(navController) }
        composable("favorites") { FavoriteScreen(navController) }
        composable("detail/{mealId}") {
            DetailScreen(mealId = it.arguments?.getString("mealId") ?: "")
        }
    }
}
