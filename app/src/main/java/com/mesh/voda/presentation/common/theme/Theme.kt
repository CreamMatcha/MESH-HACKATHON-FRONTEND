package com.mesh.voda.presentation.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VodaColorScheme = lightColorScheme(
    primary = Color(0xFF3D7BFF),
    onPrimary = Color.White,
    secondary = Color(0xFF6C9FFF),
    background = Color(0xFFF9F9F9),
    surface = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
)

@Composable
fun VodaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VodaColorScheme,
        typography = VodaTypography,
        content = content
    )
}
