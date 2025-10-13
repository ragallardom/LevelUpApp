package cl.duoc.levelupapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance

@Composable
fun TechBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val gradient = remember(colorScheme) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.background,
                colorScheme.surface,
                colorScheme.background
            )
        )
    }
    val glowPrimary = remember(colorScheme) { colorScheme.primary.copy(alpha = 0.35f) }
    val glowSecondary = remember(colorScheme) { colorScheme.secondary.copy(alpha = 0.28f) }
    val overlayColor = remember(colorScheme) {
        val baseLuminance = colorScheme.background.luminance()
        if (baseLuminance < 0.25f) {
            Color.Black.copy(alpha = 0.25f)
        } else {
            Color.White.copy(alpha = 0.35f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .drawBehind {
                val width = size.width
                val height = size.height
                drawCircle(
                    color = glowPrimary,
                    radius = width * 0.85f,
                    center = Offset(x = -width * 0.25f, y = height * 0.1f)
                )
                drawCircle(
                    color = glowSecondary,
                    radius = width * 0.75f,
                    center = Offset(x = width * 1.15f, y = height * 0.9f)
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
        ) {
            content()
        }
    }
}
