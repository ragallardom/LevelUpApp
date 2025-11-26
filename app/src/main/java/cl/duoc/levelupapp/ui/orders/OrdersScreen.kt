package cl.duoc.levelupapp.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.levelupapp.model.SaleResponse
import cl.duoc.levelupapp.ui.theme.BrandColors

// Usamos tus colores de marca para consistencia
private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandAccent = BrandColors.Accent
private val BrandOnDark = BrandColors.OnDark

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = viewModel()
) {
    val sales by viewModel.sales.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargar compras al entrar
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = BrandAccent
            )
        } else if (error != null) {
            Text(
                text = error ?: "Error desconocido",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (sales.isEmpty()) {
            Text(
                text = "No tienes compras registradas",
                color = BrandOnDark.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sales) { sale ->
                    OrderCard(sale)
                }
            }
        }
    }
}

@Composable
fun OrderCard(sale: SaleResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandDeepBlue.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera: ID y Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido #${sale.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandAccent,
                    fontWeight = FontWeight.Bold
                )
                // Formateo simple de fecha (toma los primeros 10 caracteres: YYYY-MM-DD)
                val fechaFormateada = if (sale.date.length >= 16) {
                    sale.date.take(10) + " " + sale.date.substring(11, 16)
                } else {
                    sale.date
                }

                Text(
                    text = fechaFormateada,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandOnDark.copy(alpha = 0.6f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = BrandAccent.copy(alpha = 0.2f)
            )

            // Lista de productos
            sale.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}x ${item.productName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandOnDark
                    )
                    Text(
                        text = "$${item.subTotal}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandOnDark.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total: $${sale.total}",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}