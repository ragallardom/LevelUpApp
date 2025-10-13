package cl.duoc.levelupapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.Black,
    secondary = NeonGreen,
    onSecondary = Color.Black,
    tertiary = ElectricBlue,
    onTertiary = Color.Black,
    background = BackgroundBlack,
    onBackground = TextPrimaryWhite,
    surface = SurfaceDark,
    onSurface = TextPrimaryWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryGray,
    outline = TextSecondaryGray,
    outlineVariant = TextSecondaryGray,
    scrim = BackgroundBlack
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.Black,
    secondary = NeonGreen,
    onSecondary = Color.Black,
    tertiary = ElectricBlue,
    onTertiary = Color.Black,
    background = BackgroundBlack,
    onBackground = TextPrimaryWhite,
    surface = SurfaceDark,
    onSurface = TextPrimaryWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryGray,
    outline = TextSecondaryGray,
    outlineVariant = TextSecondaryGray,
    scrim = BackgroundBlack
)

@Composable
fun LevelUppAppTheme(
    darkTheme: Boolean = true,
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