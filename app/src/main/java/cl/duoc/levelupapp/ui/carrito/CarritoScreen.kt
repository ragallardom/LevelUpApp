package cl.duoc.levelupapp.ui.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

                    SummarySection(
                        totalItems = uiState.totalItems,
                        formattedTotal = uiState.formattedTotal,
                        onClear = { viewModel.limpiarCarrito() }
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
    onClear: () -> Unit,
    onCheckout: () -> Unit = {}
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
                    text = "Total a pagar: $formattedTotal ",
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
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, BrandAccent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BrandAccent
                    )
                ) {
                    Text(text = "Limpiar carrito")
                }
                FilledTonalButton(
                    onClick = onCheckout,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BrandAccent,
                        contentColor = BrandDeepBlue
                    )
                ) {
                    Text(text = "Pagar")
                }
            }
        }
    }
}
