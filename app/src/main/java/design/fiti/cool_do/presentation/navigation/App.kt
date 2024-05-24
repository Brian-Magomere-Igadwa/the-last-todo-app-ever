package design.fiti.cool_do.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import design.fiti.cool_do.presentation.screens.boards.BoardsScreen
import design.fiti.cool_do.presentation.screens.tasks.TasksScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(modifier = modifier) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Boards.name
        ) {
            composable(route = AppRoutes.Boards.name) {
                BoardsScreen(navController)
            }
            composable(route = AppRoutes.Tasks.name) {
                TasksScreen()
            }
        }

    }
}

sealed class AppRoutes(val name: String) {
    data object Boards : AppRoutes(name = "Boards")
    data object Tasks : AppRoutes(name = "Tasks")
}