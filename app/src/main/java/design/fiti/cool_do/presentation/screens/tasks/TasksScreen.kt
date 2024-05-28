package design.fiti.cool_do.presentation.screens.tasks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import design.fiti.cool_do.R
import design.fiti.cool_do.presentation.navigation.AppRoutes
import design.fiti.cool_do.presentation.ui.theme.coolBlackWeak_d
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TasksScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TaskTopBar() { navController.navigateUp() }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(10) {
                SwipeableTaskCard(modifier = Modifier.fillMaxWidth(0.9f))
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}

@Composable
private fun TaskTopBar(modifier: Modifier = Modifier, navigateUp: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(0.9f)
        ) {
            Button(
                onClick = { navigateUp() },
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
            ) {

                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = MaterialTheme
                        .colorScheme.onBackground,
                    contentDescription = stringResource(id = R.string.up)
                )
            }

            Text(
                text = stringResource(id = R.string.tasks),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

}

@Composable
private fun SwipeableTaskCard(modifier: Modifier = Modifier) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val maxSwipe = 600f
    val swipeThreshold = maxSwipe / 2

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (offsetX.value > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .fillMaxWidth(0.9f)
                    .height(80.dp)
                    .background(Color.Red)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Delete",
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        } else if (offsetX.value < 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .fillMaxWidth(0.9f)
                    .height(80.dp)
                    .background(Color.Blue)
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Edit",
                    color = Color.White,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }

        Card(
            onClick = { /*TODO*/ },
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            modifier = modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (
                                    offsetX.value > swipeThreshold ||
                                    offsetX.value < -swipeThreshold
                                ) {
                                    // Perform action if swiped more than the threshold
                                } else {
                                    // Animate back to the original position
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount).coerceIn(
                                    -maxSwipe,
                                    maxSwipe
                                )
                            )
                        }
                    }
                }
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Go to the market.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            tint = coolBlackWeak_d.copy(alpha = 0.4f),
                            modifier = Modifier.size(12.dp),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Today 10:00 - 12:00 PM",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = coolBlackWeak_d.copy(
                                    alpha = 0.4f
                                )
                            )
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .padding(2.dp)
                            .background(Color.Green.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "COMPLETED",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCard(modifier: Modifier = Modifier) {
    Card(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Go to the market.",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Today 10:00 - 12:00 PM",
                        style = MaterialTheme.typography.bodySmall
                    )

                }
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .padding(2.dp)
                        .background(Color.Green.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "COMPLETED",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}