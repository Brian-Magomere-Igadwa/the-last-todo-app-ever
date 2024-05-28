package design.fiti.cool_do.presentation.screens.boards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import design.fiti.cool_do.R
import design.fiti.cool_do.presentation.navigation.AppRoutes


@Composable
fun BoardsScreen(navController: NavController) {
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
                    AddDo()
                }
                items(10) {
                    CardItem() {
                        navController.navigate(AppRoutes.Tasks.name)
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun AddDo() {
    Card(
        modifier = Modifier
            .heightIn(
                min = 220.dp * 0.3f,
                max = 220.dp * 0.3f
            )
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
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
private fun CardItem(navigateToSubTasksScreen: () -> Unit) {
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
            TaskHeadlines()
            TaskStats()

        }
    }
}

@Preview
@Composable
private fun EmojiSection() {
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
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.morevet)
            )
        }
    }
}

@Preview
@Composable
private fun TaskHeadlines() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Personal Task",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "12 tasks",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun TaskStats() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(8.dp))
        VProgressBar()
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Progress",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "70%",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun VProgressBar() {
    val height = 28.dp
    val width = 8.dp

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
        targetValue = if (isItemVisible) 0.6f else 0f,
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

