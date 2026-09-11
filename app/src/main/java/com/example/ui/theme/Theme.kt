package com.example.ui.theme

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
    primary = AccentRose,
    onPrimary = Color.White,
    primaryContainer = PrimaryCrimsonDark,
    onPrimaryContainer = PrimaryCrimsonLight,
    secondary = GoldYellow,
    onSecondary = SlateDark,
    background = CrimsonDarkBackground,
    surface = CrimsonDarkSurface,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryCrimson,
    onPrimary = Color.White,
    primaryContainer = PrimaryCrimsonLight,
    onPrimaryContainer = PrimaryCrimsonDark,
    secondary = SlateDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1F5F9),
    onSecondaryContainer = SlateDark,
    tertiary = AccentRose,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = SlateDark,
    onSurface = SlateDark,
    outline = BorderRose,
    outlineVariant = SurfaceBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand crimson by default
    content: @Composable () -> Unit,
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
