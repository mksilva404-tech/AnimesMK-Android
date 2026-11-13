package br.com.animesmk.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnimesMKDarkColorScheme = darkColorScheme(
    primary = orange,
    secondary = purple,
    background = black,
    surface = panel
)

@Composable
fun AnimesMKTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnimesMKDarkColorScheme,
        content = content
    )
}
