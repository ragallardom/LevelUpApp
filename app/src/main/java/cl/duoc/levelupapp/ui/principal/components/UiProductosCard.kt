package cl.duoc.levelupapp.ui.principal.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import cl.duoc.levelupapp.model.Producto

@Composable
fun UiProductosCard(
    producto: Producto,
    onAgregar: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    var agregado by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Valor: $${producto.precio} CLP",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Spacer(Modifier.weight(1f))

            val interactionSource = remember { MutableInteractionSource() }
            val presionado by interactionSource.collectIsPressedAsState()

            val escala by animateFloatAsState(
                targetValue = if (presionado) 0.95f else 1f,
                animationSpec = tween(durationMillis = 100),
                label = "scaleAnim"
            )

            val colorFondo by animateColorAsState(
                targetValue = if (agregado) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.primary
                },
                animationSpec = tween(durationMillis = 250),
                label = "colorAnim"
            )

            Button(
                onClick = {
                    agregado = !agregado
                    onAgregar(producto)
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorFondo,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = escala
                        scaleY = escala
                    }
                    .animateContentSize()
            ) {
                Text(
                    text = if (agregado) "Agregado" else "Agregar al carrito",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
