package cl.duoc.levelupapp.ui.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.R

@Composable
fun AnimatedLogo(
    modifier: Modifier = Modifier,
    logoRes: Int = R.drawable.logo,
    contentScale: ContentScale = ContentScale.Fit
) {
    var appeared by remember { mutableStateOf(false) }
    val enterScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.9f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "enterScale"
    )
    val enterAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "enterAlpha"
    )

    val infinite = rememberInfiniteTransition(label = "logoInfinite")
    val pulse by infinite.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val tilt by infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tilt"
    )

    // Feedback al toque (press): escala leve al presionar
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "pressScale"
    )

    LaunchedEffect(Unit) { appeared = true }

    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Logo App",
        contentScale = contentScale,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .graphicsLayer {
                val finalScale = enterScale * pulse * pressScale
                scaleX = finalScale
                scaleY = finalScale
                rotationZ = tilt
                alpha = enterAlpha
            }
            .then(Modifier)
        ,
    )
}

