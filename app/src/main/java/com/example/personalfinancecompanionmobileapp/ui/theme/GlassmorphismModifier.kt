package com.example.personalfinancecompanionmobileapp.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates an Earthy Gradient Background to make the entire application feel unified.
 * This background enables the glassmorphism effects to actually show through.
 */
@Composable
fun EarthBackground(
    darkTheme: Boolean = com.example.personalfinancecompanionmobileapp.ui.theme.LocalAppDarkTheme.current,
    content: @Composable () -> Unit
) {
    val gradientColors = if (darkTheme) {
        listOf(EarthForest, Color(0xFF1E2812), EarthForest)
    } else {
        listOf(EarthCream, Color(0xFFF3EAC0), EarthCream)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(gradientColors))
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            content()
        }
    }
}

/**
 * A modifier extension that applies a frosted glass card effect.
 * It uses a semi-transparent background and a thin bright border.
 */
fun Modifier.glassCard(
    shape: Shape = RoundedCornerShape(16.dp),
    darkTheme: Boolean = false,
    blurRadius: Dp = 16.dp
): Modifier {
    val surfaceColor = if (darkTheme) GlassSurfaceDark else GlassSurfaceWhite
    val borderColor = if (darkTheme) GlassBorderDark else GlassBorderWhite

    var mod = this
        .clip(shape) // Clip the content to the shape first

    return mod
        .background(surfaceColor, shape)
        .border(1.dp, borderColor, shape)
}
