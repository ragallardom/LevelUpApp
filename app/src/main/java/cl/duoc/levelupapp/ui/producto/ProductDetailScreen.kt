package cl.duoc.levelupapp.ui.producto

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.R
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.theme.BrandColors
import kotlinx.coroutines.launch

// --- COLORES ---
private val BrandShadow = BrandColors.Shadow
private val BrandMidnight = BrandColors.Midnight
private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandSurface = BrandColors.Surface
private val BrandSurfaceElevated = BrandColors.SurfaceElevated
private val BrandAccent = BrandColors.Accent
private val BrandOnDark = BrandColors.OnDark
private val BrandOnMuted = BrandColors.OnMuted

fun String.toImageBitmap(): ImageBitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    producto: Producto,
    sugeridos: List<Producto>,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    onSuggestedProductClick: (Producto) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val imageBitmap = remember(producto.imageBase64) {
        producto.imageBase64?.toImageBitmap()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = producto.nombre, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandDeepBlue,
                    titleContentColor = BrandAccent,
                    navigationIconContentColor = BrandAccent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(BrandShadow, BrandMidnight, BrandDeepBlue)
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandSurfaceElevated
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = producto.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {

                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Imagen no disponible",
                                modifier = Modifier
                                    .size(100.dp)
                                    .alpha(0.5f),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = producto.nombre,
                        color = BrandOnDark,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "${producto.descripcion ?: "Sin descripción"} • ${producto.codigo}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandOnMuted
                    )
                    Text(
                        text = "$${producto.precio} CLP",
                        style = MaterialTheme.typography.headlineSmall,
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                Text(
                    text = "Descripción",
                    color = BrandOnDark,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            item {
                Text(
                    text = producto.descripcion ?: "No hay descripción disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandOnMuted
                )
            }

            item {
                Button(
                    onClick = {
                        onAddToCart()
                        coroutineScope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = "Producto agregado al carrito",
                                withDismissAction = false,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandAccent,
                        contentColor = BrandDeepBlue
                    )
                ) {
                    Text(text = "Añadir al carrito")
                }
            }

            if (sugeridos.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "También te podría interesar",
                        color = BrandOnDark,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sugeridos, key = { it.id }) { sugerido ->
                            SuggestedProductCard(
                                producto = sugerido,
                                onClick = { onSuggestedProductClick(sugerido) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestedProductCard(
    producto: Producto,
    onClick: () -> Unit
) {

    val imageBitmap = remember(producto.imageBase64) {
        producto.imageBase64?.toImageBitmap()
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Bloque de imagen condicional
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder si no hay imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = BrandOnDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}