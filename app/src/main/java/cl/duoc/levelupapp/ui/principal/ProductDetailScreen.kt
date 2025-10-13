package cl.duoc.levelupapp.ui.principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.model.Producto

private val BrandShadow = Color(0xFF000000)
private val BrandMidnight = Color(0xFF010E1C)
private val BrandDeepBlue = Color(0xFF01142E)
private val BrandAccent = Color(0xFFA8BFCD)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BrandShadow, BrandMidnight, BrandDeepBlue)
                )
            )
    ) {
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
                                tint = BrandAccent
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BrandDeepBlue.copy(alpha = 0.95f),
                        navigationIconContentColor = BrandAccent,
                        titleContentColor = BrandAccent
                    )
                )
            }
        ) { innerPadding ->
            if (producto == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No encontramos la información de este producto.",
                            style = MaterialTheme.typography.bodyLarge.copy(color = BrandAccent)
                        )
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandAccent,
                                contentColor = BrandDeepBlue
                            )
                        ) {
                            Text(text = "Volver")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
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
                                    color = BrandAccent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = producto.categoria,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = BrandAccent.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = "Código: ${producto.codigo}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = BrandAccent.copy(alpha = 0.8f)
                                )
                            )
                            Text(
                                text = "$${producto.precio} CLP",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = BrandAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Button(
                                onClick = { onAddToCart(producto) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BrandAccent,
                                    contentColor = BrandDeepBlue
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
                                    color = BrandAccent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = BrandDeepBlue.copy(alpha = 0.7f)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = producto.descripcion,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = BrandAccent.copy(alpha = 0.9f)
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
                                Divider(color = BrandAccent.copy(alpha = 0.2f))
                                Text(
                                    text = "También te puede interesar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = BrandAccent,
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

@Composable
private fun RelatedProductCard(
    producto: Producto,
    onViewDetail: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandDeepBlue.copy(alpha = 0.75f))
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
                        color = BrandAccent,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = BrandAccent.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandAccent,
                    contentColor = BrandDeepBlue
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
                    color = BrandAccent,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
