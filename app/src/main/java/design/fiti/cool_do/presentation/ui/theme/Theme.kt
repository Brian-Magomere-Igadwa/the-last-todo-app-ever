package design.fiti.cool_do.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = coolPrimary_d,
    secondary = coolSecondary_d,
    tertiary = coolTertiary_d,
    background = coolWhiteWeak_d,
    surface = coolWhite_d,
    onPrimary = coolWhite_d,
    onSecondary = coolWhite_d,
    onTertiary = coolBlackWeak_d,
    onBackground = coolBlack_d,
    onSurface = coolBlack_d,
)

private val LightColorScheme = lightColorScheme(
    primary = coolPrimary_l,
    secondary = coolSecondary_l,
    tertiary = coolTertiary_l,
    background = coolWhiteWeak_l,
    surface = coolWhite_l,
    onPrimary = coolWhite_l,
    onSecondary = coolWhite_l,
    onTertiary = coolBlackWeak_l,
    onBackground = coolBlack_l,
    onSurface = coolBlack_l,
)

@Composable
fun CooldoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}