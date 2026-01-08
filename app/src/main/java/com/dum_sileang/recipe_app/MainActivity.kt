package com.dum_sileang.recipe_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.dum_sileang.recipe_app.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RecipeMainApp() }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecipeMainApp() {
    val navController = rememberNavController()
    val items = listOf("home", "explore", "favorites")
    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Favorite)
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = screen) },
                        label = { Text(screen.replaceFirstChar { it.uppercase() }) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(screen)
                        }
                    )
                }
            }
        }
    ) {
        AppNavGraph(navController = navController)
    }
}
