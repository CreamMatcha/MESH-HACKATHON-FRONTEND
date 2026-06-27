package com.mesh.voda.presentation.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VodaColorScheme = lightColorScheme(
    primary = Color(0xFF4E8A3F),
    onPrimary = Color.White,
    secondary = Color(0xFF7CAE6B),
    background = Color(0xFFFBF7EF),
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
