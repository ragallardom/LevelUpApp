package cl.duoc.levelupapp.ui.principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.theme.TechBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: PrincipalViewModel,
    onBack: () -> Unit,
    onAddToCart: (Producto) -> Unit,
    onNavigateToProduct: (String) -> Unit
) {
    val producto = viewModel.obtenerProductoPorCodigo(productId)
    val relacionados = producto?.let { viewModel.productosRelacionados(it.categoria, it.codigo) }.orEmpty()
    val colorScheme = MaterialTheme.colorScheme

    TechBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = producto?.nombre ?: "Detalle del producto",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.surface,
                        navigationIconContentColor = colorScheme.primary,
                        titleContentColor = colorScheme.onSurface
                    )
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                color = colorScheme.surface.copy(alpha = 0.32f),
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 12.dp,
                shadowElevation = 18.dp
            ) {
                if (producto == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "No encontramos la información de este producto.",
                                style = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.onBackground)
                            )
                            Button(
                                onClick = onBack,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorScheme.primary,
                                    contentColor = colorScheme.onPrimary
                                )
                            ) {
                                Text(text = "Volver")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 32.dp)
                    ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Image(
                                painter = painterResource(id = producto.imagenRes),
                                contentDescription = producto.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = producto.categoria,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = colorScheme.secondary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = "Código: ${producto.codigo}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = colorScheme.onSurfaceVariant
                                )
                            )
                            Text(
                                text = "$${producto.precio} CLP",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Button(
                                onClick = { onAddToCart(producto) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorScheme.secondary,
                                    contentColor = colorScheme.onSecondary
                                )
                            ) {
                                Text(text = "Agregar al carrito")
                            }
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = colorScheme.surface.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.2f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Text(
                                    text = producto.descripcion,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onBackground
                                    ),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    if (relacionados.isNotEmpty()) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Divider(color = colorScheme.outline.copy(alpha = 0.4f))
                                Text(
                                    text = "También te puede interesar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(relacionados, key = { it.codigo }) { relacionado ->
                                    RelatedProductCard(
                                        producto = relacionado,
                                        onViewDetail = { onNavigateToProduct(relacionado.codigo) },
                                        onAddToCart = { onAddToCart(relacionado) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}


@Composable
private fun RelatedProductCard(
    producto: Producto,
    onViewDetail: () -> Unit,
    onAddToCart: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .width(220.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface.copy(alpha = 0.42f)),
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = producto.imagenRes),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onSecondary
                )
            ) {
                Text(text = "Agregar al carrito")
            }
            TextButton(
                onClick = onViewDetail,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ver detalle",
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
