package design.fiti.cool_do

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import design.fiti.cool_do.presentation.screens.boards.BoardsScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUpAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
//            BoardsScreen(navController = navController, viewModel = viewModel)

        }
    }

    @Test
    fun appnavhost_verifyStartDestination() {
        composeTestRule.onNodeWithStringId(R.string.welcome_boardScreen).assertExists()
    }

    @Test
    fun appnavhost_testNavigation() {
        composeTestRule.onNodeWithStringId(R.string.go_to_tasksScreen).performClick()
        composeTestRule.onNodeWithStringId(R.string.welcome_taskScreen).assertExists()
        composeTestRule.onNodeWithStringId(R.string.go_to_boardsScreen).performClick()
        composeTestRule.onNodeWithStringId(R.string.welcome_boardScreen).assertExists()
    }

}