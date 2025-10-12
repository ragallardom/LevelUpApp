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
    // Animación de entrada: escala y opacidad iniciales para aparición sutil
    var appeared by remember { mutableStateOf(false) }
    val enterScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.9f,
        // Puedes modificar targetValue para ajustar el tamaño inicial
        // y durationMillis/easing para controlar la suavidad del ingreso
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "enterScale"
    )
    val enterAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        // Ajusta durationMillis para acelerar o frenar la aparición
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "enterAlpha"
    )

    // Animaciones infinitas: pulso y balanceo muy sutiles
    val infinite = rememberInfiniteTransition(label = "logoInfinite")
    val pulse by infinite.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        // Modifica los valores de escala (initial/target) y durationMillis
        // para intensificar o ralentizar el pulso
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val tilt by infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        // Cambia los grados de inclinación (initial/target) y durationMillis
        // si quieres un balanceo más marcado o rápido
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
        // Ajusta targetValue para la compresión y durationMillis para el tiempo de respuesta
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
                // Escala total = entrada * pulso infinito * feedback de presión
                // Ajusta la combinación modificando cualquiera de las tres animaciones anteriores
                val finalScale = enterScale * pulse * pressScale
                scaleX = finalScale
                scaleY = finalScale
                // Ajusta los grados de inclinación modificando la animación "tilt"
                rotationZ = tilt
                // Controla la transparencia cambiando la animación "enterAlpha"
                alpha = enterAlpha
            }
            // Para que el press funcione aunque no tenga onClick (solo feedback táctil visual)
            .then(Modifier) // placeholder para extender si luego lo haces clickeable
        ,
    )
}

annotation class AnimatedLogo
