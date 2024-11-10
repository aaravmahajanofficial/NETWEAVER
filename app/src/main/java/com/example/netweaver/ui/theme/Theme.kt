package com.example.netweaver.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0a66c2),
    onPrimary = Color(0xE6FFFFFF),

    // background of the whole container
    background = Color.Black,
    onBackground = Color(0xE6FFFFFF),

    // posts
    surface = Color(0xFF1D2226),
    onSurface = Color(0xE6FFFFFF),

    // hyperlinks, numbers etc.
    secondary = Color(0xFF71b7fc),

    // search bar
    secondaryContainer = Color(0xFF38434f),

    // search bar icon & placeholder text, buttons, extra details
    tertiary = Color(0xBFFFFFFF),
    onTertiary = Color(0x99FFFFFF),

    outline = Color(0xFF2a2e31),

    onError = Color(0xFFd11124)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0a66c2),
    onPrimary = Color.White,

    // background of the whole container
    background = Color(0xFFe9e5df),
    onBackground = Color(0xE6000000),

    // posts
    surface = Color.White,
    onSurface = Color(0xE6000000),

    // hyperlinks, numbers etc.
    secondary = Color(0xFF0a66c2),

    // search bar
    secondaryContainer = Color(0xFFedf3f8),

    // search bar icon & placeholder text, buttons, extra details
    tertiary = Color(0xFF161718),
    onTertiary = Color(0x99000000),

    outline = Color(0xFFe8e8e8),

    onError = Color(0xFFd11124)
)

@Composable
fun NETWEAVERTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
            WindowCompat.setDecorFitsSystemWindows(window, false)

            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()

            val insetsController = WindowInsetsControllerCompat(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}