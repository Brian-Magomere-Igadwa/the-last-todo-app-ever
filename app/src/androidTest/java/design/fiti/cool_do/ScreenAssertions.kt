package design.fiti.cool_do

import android.util.Log
import androidx.navigation.NavController
import junit.framework.TestCase.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    Log.d(
        "AppNavigationTest",
        "assertCurrentRouteName: $expectedRouteName ${currentBackStackEntry?.destination?.route}"
    )
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}