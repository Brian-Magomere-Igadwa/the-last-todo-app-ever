package design.fiti.cool_do.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import design.fiti.cool_do.presentation.screens.boards.BoardsScreen
import design.fiti.cool_do.presentation.screens.tasks.TasksScreen
import design.fiti.cool_do.presentation.screens.welcome.WelcomeScreen
import design.fiti.cool_do.presentation.viewmodel.GoalsViewModel

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: GoalsViewModel = hiltViewModel()

    Scaffold(modifier = modifier) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppRoutes.Welcome.name
        ) {
            composable(route = AppRoutes.Boards.name) {
                BoardsScreen(navController = navController, viewModel = viewModel)
            }
            composable(
                route = AppRoutes.Tasks.name + "/{goalId}",
                arguments = listOf(navArgument("goalId") { type = NavType.IntType })
            ) { backStackEntry ->
                TasksScreen(
                    navController = navController,
                    viewModel = viewModel,
                    goalId = backStackEntry.arguments?.getInt("goalId")!!
                )
            }
            composable(route = AppRoutes.Welcome.name) {
                WelcomeScreen(navController = navController)
            }
        }

    }
}

sealed class AppRoutes(val name: String) {

    data object Welcome : AppRoutes(name = "Welcome")
    data object Boards : AppRoutes(name = "Boards")
    data object Tasks : AppRoutes(name = "Tasks")
}