package cl.duoc.levelupapp.ui.principal.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.R
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.producto.toImageBitmap
import cl.duoc.levelupapp.ui.theme.BrandColors


private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandAccent = BrandColors.Accent

@Composable
fun ProductCard(
    producto: Producto,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    // Decodificamos la imagen
    val imageBitmap = remember(producto.imageBase64) {
        producto.imageBase64?.toImageBitmap()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = BrandDeepBlue.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IMAGEN CORREGIDA
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder (logo) si no hay imagen
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Asegúrate de tener este recurso
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "${producto.categoria} • ${producto.codigo}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent.copy(alpha = 0.8f))
                )
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = BrandAccent,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            FilledTonalButton(onClick = onAddToCart) {
                Text(text = "Agregar")
            }
        }
    }
}