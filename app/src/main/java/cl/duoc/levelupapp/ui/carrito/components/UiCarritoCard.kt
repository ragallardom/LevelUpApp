package cl.duoc.levelupapp.ui.carrito.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.carrito.CarritoItem
import cl.duoc.levelupapp.ui.theme.BrandColors

private val BrandAccent = BrandColors.Accent
private val BrandSurface = BrandColors.SurfaceElevated
private val BrandOnDark = BrandColors.OnDark
private val BrandOnMuted = BrandColors.OnMuted

@Composable
fun UiCarritoCard(
    item: CarritoItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductoImagen(producto = item.producto)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.producto.nombre,
                    color = BrandOnDark,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "$${item.producto.precio} CLP",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandAccent
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDecrease,
                        enabled = item.cantidad > 1
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Disminuir cantidad",
                            tint = BrandAccent
                        )
                    }
                    Text(
                        text = item.cantidad.toString(),
                        color = BrandOnDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(onClick = onIncrease) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Aumentar cantidad",
                            tint = BrandAccent
                        )
                    }
                }
            }

            FilledTonalButton(
                onClick = onRemove,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = BrandSurface,
                    contentColor = BrandAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Quitar del carrito",
                    tint = BrandAccent
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Quitar")
            }
        }
    }
}

@Composable
private fun ProductoImagen(producto: Producto) {
    Image(
        painter = painterResource(id = producto.imagenRes),
        contentDescription = producto.nombre,
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}
