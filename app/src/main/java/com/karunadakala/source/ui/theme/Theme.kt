package com.karunadakala.source.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = KarnatakaRed,
    onPrimary = Color.White,
    primaryContainer = KarnatakaYellow.copy(alpha = 0.35f),
    onPrimaryContainer = KarnatakaBrown,
    secondary = KarnatakaYellow,
    onSecondary = KarnatakaBrown,
    tertiary = KarnatakaBrown,
    onTertiary = Color.White,
    background = KarnatakaCream,
    onBackground = KarnatakaBrown,
    surface = Color.White,
    onSurface = KarnatakaBrown,
    surfaceVariant = KarnatakaYellow.copy(alpha = 0.22f),
    onSurfaceVariant = KarnatakaBrown.copy(alpha = 0.85f),
    outline = KarnatakaBrown.copy(alpha = 0.35f),
)

private val DarkColors = darkColorScheme(
    primary = KarnatakaYellow,
    onPrimary = KarnatakaBrown,
    secondary = KarnatakaRed,
    onSecondary = Color.White,
    background = Color(0xFF12100E),
    onBackground = Color(0xFFF7EFDE),
    surface = Color(0xFF1B1714),
    onSurface = Color(0xFFF7EFDE),
)

@Composable
fun KarunadaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        typography = KarunadaTypography,
        content = content,
    )
}
