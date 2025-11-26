package cl.duoc.levelupapp.ui.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.ui.theme.BrandColors
import cl.duoc.levelupapp.ui.carrito.components.UiCarritoCard

private val BrandShadow = BrandColors.Shadow
private val BrandMidnight = BrandColors.Midnight
private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandAccent = BrandColors.Accent
private val BrandSurface = BrandColors.Surface
private val BrandOnDark = BrandColors.OnDark
private val BrandOnMuted = BrandColors.OnMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onBack: () -> Unit,
    viewModel: CarritoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- DIÁLOGO DE COMPRA EXITOSA ---
    if (uiState.lastSale != null) {
        val venta = uiState.lastSale!!
        AlertDialog(
            onDismissRequest = { viewModel.cerrarDialogoCompra() },
            containerColor = BrandSurface,
            title = {
                Text(
                    "¡Compra Exitosa!",
                    color = BrandAccent,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("ID Venta: ${venta.id}", color = BrandOnDark)
                    Text("Total: $${venta.total}", color = BrandOnDark)
                    Spacer(Modifier.height(8.dp))
                    Text("Productos:", fontWeight = FontWeight.Bold, color = BrandOnDark)
                    venta.items.forEach { item ->
                        Text("- ${item.quantity}x ${item.productName}", color = BrandOnMuted)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.cerrarDialogoCompra()
                    onBack() // Volver al home opcionalmente
                }) {
                    Text("Aceptar", color = BrandAccent)
                }
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tu carrito",
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandDeepBlue,
                    titleContentColor = BrandAccent,
                    navigationIconContentColor = BrandAccent
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BrandShadow,
                            BrandMidnight,
                            BrandDeepBlue
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.items.isEmpty()) {
                    EmptyCartState()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.items, key = { it.producto.codigo }) { item ->
                            UiCarritoCard(
                                item = item,
                                onIncrease = { viewModel.incrementarCantidad(item.producto.codigo) },
                                onDecrease = { viewModel.decrementarCantidad(item.producto.codigo) },
                                onRemove = { viewModel.quitarProducto(item.producto.codigo) }
                            )
                        }
                    }

                    // Mensaje de error si falla el pago
                    uiState.error?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    SummarySection(
                        totalItems = uiState.totalItems,
                        formattedTotal = uiState.formattedTotal,
                        isPaying = uiState.isPaying, // Pasamos el estado de carga
                        onClear = { viewModel.limpiarCarrito() },
                        onCheckout = { viewModel.pagar() } // Conectamos la función pagar
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCartState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium,
            color = BrandOnDark,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Agrega productos desde la lista para verlos aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = BrandOnMuted
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SummarySection(
    totalItems: Int,
    formattedTotal: String,
    isPaying: Boolean,
    onClear: () -> Unit,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandSurface
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Resumen",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandOnDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Total de productos: $totalItems",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandOnMuted
                )
                Text(
                    text = "Total a pagar: $formattedTotal",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = BrandAccent
                )
            }

            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = BrandAccent.copy(alpha = 0.2f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    enabled = !isPaying,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, BrandAccent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BrandAccent
                    )
                ) {
                    Text(text = "Limpiar")
                }
                FilledTonalButton(
                    onClick = onCheckout,
                    enabled = !isPaying,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BrandAccent,
                        contentColor = BrandDeepBlue
                    )
                ) {
                    if (isPaying) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = BrandDeepBlue,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Pagar")
                    }
                }
            }
        }
    }
}