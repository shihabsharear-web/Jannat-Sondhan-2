package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color.Black,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = Color.White,
    secondary = IslamicGold,
    onSecondary = Color.Black,
    tertiary = CoralMint,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Color(0xFF1E2F29),
    onSurfaceVariant = Color(0xFFC0D2C9),
    outline = Color(0xFF334F45)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDeep,
    onPrimary = Color.White,
    primaryContainer = SoftMintBg,
    onPrimaryContainer = EmeraldDeep,
    secondary = IslamicBronze,
    onSecondary = Color.White,
    tertiary = CoralMint,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    surfaceVariant = SoftMintBg,
    onSurfaceVariant = Color(0xFF2C443D),
    outline = Color(0xFF90AFAA)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Force false to satisfy user request of having only Light Mode
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Constantly keep LightColorScheme active to completely remove dark mode switching support

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
