package cl.duoc.levelupapp.ui.theme

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
    primary = StellarBlue,
    onPrimary = Color.White,
    secondary = AuroraCyan,
    onSecondary = Color(0xFF00121A),
    tertiary = NeonViolet,
    onTertiary = Color.White,
    error = SignalMagenta,
    onError = Color.White,
    background = Midnight,
    onBackground = TextPrimary,
    surface = DeepSpace,
    onSurface = TextPrimary,
    surfaceVariant = CosmicSlate,
    onSurfaceVariant = TextSecondary,
    outline = TextSecondary,
    outlineVariant = TextSecondary.copy(alpha = 0.6f),
    scrim = Color.Black,
    inverseSurface = TextPrimary,
    inverseOnSurface = DeepSpace
)

private val LightColorScheme = lightColorScheme(
    primary = StellarBlue,
    onPrimary = Color.White,
    secondary = AuroraCyan,
    onSecondary = Color.White,
    tertiary = NeonViolet,
    onTertiary = Color.White,
    error = SignalMagenta,
    onError = Color.White,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = Midnight,
    inverseSurface = LightOnBackground,
    inverseOnSurface = LightSurface
)

@Composable
fun LevelUppAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
