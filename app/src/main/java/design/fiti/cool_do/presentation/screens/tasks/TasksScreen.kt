package design.fiti.cool_do.presentation.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import design.fiti.cool_do.R
import design.fiti.cool_do.presentation.navigation.AppRoutes

@Composable
fun TasksScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.welcome_taskScreen))
        Button(onClick = { navController.navigate(AppRoutes.Boards.name) }) {
            Text(text = stringResource(id = R.string.go_to_boardsScreen))
        }
    }
}