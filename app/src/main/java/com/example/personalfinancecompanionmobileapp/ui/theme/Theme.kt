package com.example.personalfinancecompanionmobileapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val LocalAppDarkTheme = androidx.compose.runtime.staticCompositionLocalOf { false }

private val DarkColorScheme = darkColorScheme(
    primary = EarthGold,
    secondary = EarthOlive,
    tertiary = EarthTerra,
    background = EarthForest,
    surface = EarthForest,
    onPrimary = EarthForest,
    onSecondary = EarthCream,
    onTertiary = EarthCream,
    onBackground = EarthCream,
    onSurface = EarthCream
)

private val LightColorScheme = lightColorScheme(
    primary = EarthForest,
    secondary = EarthOlive,
    tertiary = EarthTerra,
    background = EarthCream,
    surface = EarthCream,
    onPrimary = EarthCream,
    onSecondary = EarthCream,
    onTertiary = EarthCream,
    onBackground = EarthForest,
    onSurface = EarthForest
)

@Composable
fun PersonalFinanceCompanionMobileAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We intentionally disable dynamic color to strictly adhere to the requested palette
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

    androidx.compose.runtime.CompositionLocalProvider(LocalAppDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}