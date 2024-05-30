package design.fiti.cool_do.presentation.screens.boards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import design.fiti.cool_do.R
import design.fiti.cool_do.data.util.Resource
import design.fiti.cool_do.presentation.navigation.AppRoutes
import design.fiti.cool_do.presentation.viewmodel.GoalsForBoardsScreen
import design.fiti.cool_do.presentation.viewmodel.GoalsUiState
import design.fiti.cool_do.presentation.viewmodel.GoalsViewModel


@Composable
fun BoardsScreen(navController: NavController) {
    var addDoClicked by remember { mutableStateOf(false) }
    val viewModel: GoalsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            BoardHeaderSection()
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(

                    horizontal = 16.dp
                )
                .fillMaxSize(),
        ) {
            if (addDoClicked)
                DialogWithForm(
                    onDismissRequest = { addDoClicked = false },
                    state = state,
                    viewModel = viewModel,
                    onConfirmation = {})
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.welcome_boardScreen),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2)
            ) {
                item {
                    AddDo(onClick = { addDoClicked = true })
                }
                items(state.goalsForBoardsScreen) {
                    CardItem(goal = it, viewModel = viewModel) {
                        navController.navigate(AppRoutes.Tasks.name)
                    }
                }
            }

        }
    }
}

@Composable
fun DialogWithForm(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: GoalsViewModel,
    state: GoalsUiState
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    var showLoader by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = state.insertGoal_result) {
        when (state.insertGoal_result) {
            is Resource.Success -> {
                showLoader = false
                viewModel.getGoalsForBoardsScreen()
                onDismissRequest()
            }

            is Resource.Error -> {
                // show error
                showLoader = false
            }

            is Resource.Loading -> {
                // show loading
                showLoader = true
            }

            null -> {
                // do nothing
            }
        }

    }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        value = state.newGoalTitle,
                        onValueChange = { viewModel.updateNewGoalTitle(it) },
                        placeholder = {
                            Text("Enter your new goal")
                        },
                    )

                    Text(
                        text = "Are you sure you'd love to save this goal?",
                        modifier = Modifier.padding(16.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Not really")
                        }
                        TextButton(
                            onClick = {
                                viewModel.insertGoal()
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Yes, sure.")
                        }
                    }
                }
                if (showLoader)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceDim),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Loading")
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator()
                        }
                    }
            }

        }
    }
}


@Preview
@Composable
private fun AddDo(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .heightIn(
                min = 220.dp * 0.3f,
                max = 220.dp * 0.3f
            )
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.adddo)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add Do")
        }
    }
}

@Preview
@Composable
private fun BoardHeaderSection(modifier: Modifier = Modifier) {
    Column {
        Spacer(Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {

                Text(
                    text = "Cool Do", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )
                Text(
                    text = "Yay! Yet another todo app!",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "Doe",
                    style = MaterialTheme.typography.bodySmall
                )

            }
        }
    }

}

@Composable
private fun CardItem(
    goal: GoalsForBoardsScreen,
    viewModel: GoalsViewModel,
    navigateToSubTasksScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .heightIn(
                min = 220.dp,
                max = 220.dp
            )
            .padding(8.dp),
        shape = RoundedCornerShape(24.dp),
        onClick = navigateToSubTasksScreen
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            EmojiSection()
            TaskHeadlines(goal = goal)
            TaskStats(goal = goal, viewModel = viewModel)

        }
    }
}

@Preview
@Composable
private fun EmojiSection() {
    var showMenu by rememberSaveable {
        mutableStateOf(false)
    }

    Box() {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎉",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.morevet)
                )
            }


        }
        AnimatedVisibility(
            visible = showMenu,
            enter = fadeIn() + slideInVertically(
                animationSpec = spring(
                    stiffness = 100f,
                    dampingRatio = 0.1f,
                )
            ),
            exit = fadeOut() + slideOutVertically(
                animationSpec = spring(
                    stiffness = 100f,
                    dampingRatio = 0.1f
                )
            )
        ) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },

                modifier = Modifier.clip(
                    RoundedCornerShape(32.dp)
                )
            ) {
                DropdownMenuItem(text = { Text("Delete") }, onClick = { })
                HorizontalDivider()
                DropdownMenuItem(text = { Text("Edit") }, onClick = { /*TODO*/ })
                HorizontalDivider()
                DropdownMenuItem(text = { Text("Add Task") }, onClick = { /*TODO*/ })

            }
        }
    }
}

@Preview
@Composable
private fun TaskHeadlines(goal: GoalsForBoardsScreen) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = goal.goal.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${goal.tasksCount} tasks",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun TaskStats(goal: GoalsForBoardsScreen, viewModel: GoalsViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(8.dp))
        VProgressBar(goal, viewModel)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Progress",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = goal.taskCompletionRate,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun VProgressBar(goal: GoalsForBoardsScreen, viewModel: GoalsViewModel) {
    val height = 28.dp
    val width = 8.dp
    val targetController =
        if (goal.tasksCount != 0) viewModel.parsePercentage(goal.taskCompletionRate) / goal.tasksCount else 0.2f

    var isItemVisible by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        // When the component is first composed, set isItemVisible to true
        isItemVisible = true

        onDispose {
            // Clean up when the component is removed from the composition
            isItemVisible = false
        }
    }
    val multiplier by animateFloatAsState(
        targetValue = if (isItemVisible) targetController.toFloat() else 0f,
        animationSpec = spring(stiffness = 100f, dampingRatio = 0.1f)
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .width(width)
            .height(height)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(32.dp))
                .width(width)
                .height(height * multiplier)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

