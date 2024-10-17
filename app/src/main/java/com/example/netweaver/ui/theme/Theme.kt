package com.example.netweaver.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0a66c2),
    onPrimary = Color(0xE6FFFFFF),

    // background of the whole container
    background = Color.Black,
    onBackground = Color(0xE6FFFFFF),

    // posts
    surface = Color(0xFF1b1f23),
    onSurface = Color(0xE6FFFFFF),

    // hyperlinks, numbers etc.
    secondary = Color(0xff71b7fb),

    // search bar
    secondaryContainer = Color(0xFF38434f),

    // search bar icon & placeholder text, buttons, extra details
    tertiary = Color(0xBFFFFFFF),
    onTertiary = Color(0x99FFFFFF),

    outline = Color(0xFF2a2e31)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0a66c2),
    onPrimary = Color.White,

    // background of the whole container
    background = Color(0xFFf4f2ee),
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

    outline = Color(0xFFebebeb)


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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}