package cl.duoc.levelupapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LevelUpPrimary,
    onPrimary = LevelUpOnPrimary,
    secondary = LevelUpSecondary,
    onSecondary = LevelUpOnSecondary,
    tertiary = LevelUpTertiary,
    onTertiary = LevelUpOnTertiary,
    background = LevelUpBackground,
    onBackground = LevelUpOnBackground,
    surface = LevelUpSurface,
    onSurface = LevelUpOnSurface,
    surfaceVariant = LevelUpSurfaceVariant,
    onSurfaceVariant = LevelUpOnSurfaceVariant,
    surfaceContainerLowest = LevelUpSurface,
    surfaceContainerLow = LevelUpSurfaceContainerLow,
    surfaceContainer = LevelUpSurfaceContainer,
    surfaceContainerHigh = LevelUpSurfaceContainerHigh,
    outline = LevelUpOutline,
    outlineVariant = LevelUpOutlineVariant,
    error = LevelUpError,
    onError = LevelUpOnError
)

private val LightColorScheme = lightColorScheme(
    primary = LevelUpOnPrimary,
    onPrimary = LevelUpPrimary,
    secondary = LevelUpSecondary,
    onSecondary = LevelUpOnSecondary,
    tertiary = LevelUpTertiary,
    onTertiary = LevelUpOnTertiary,
    background = LevelUpOnBackground,
    onBackground = LevelUpBackground,
    surface = LevelUpPrimary,
    onSurface = LevelUpOnPrimary,
    surfaceVariant = LevelUpSecondary,
    onSurfaceVariant = LevelUpOnSecondary,
    surfaceContainerLowest = LevelUpPrimary,
    surfaceContainerLow = LevelUpPrimary.copy(alpha = 0.85f),
    surfaceContainer = LevelUpPrimary.copy(alpha = 0.75f),
    surfaceContainerHigh = LevelUpPrimary.copy(alpha = 0.65f),
    outline = LevelUpOutline,
    outlineVariant = LevelUpOutlineVariant,
    error = LevelUpError,
    onError = LevelUpOnError
)

@Composable
fun LevelUppAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}