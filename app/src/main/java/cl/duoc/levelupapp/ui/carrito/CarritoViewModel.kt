package cl.duoc.levelupapp.ui.carrito

import androidx.lifecycle.ViewModel
import cl.duoc.levelupapp.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

data class CarritoItem(
    val producto: Producto,
    val cantidad: Int
)

data class CarritoUiState(
    val items: List<CarritoItem> = emptyList()
) {
    val totalItems: Int
        get() = items.sumOf { it.cantidad }

    val totalAmount: Int
        get() = items.sumOf { it.producto.precioComoEntero() * it.cantidad }

    val formattedTotal: String
        get() = formatCurrency(totalAmount)
}

class CarritoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    fun agregarProducto(producto: Producto) {
        val itemsActuales = _uiState.value.items.toMutableList()
        val indice = itemsActuales.indexOfFirst { it.producto.codigo == producto.codigo }
        if (indice >= 0) {
            val existente = itemsActuales[indice]
            itemsActuales[indice] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            itemsActuales.add(CarritoItem(producto, cantidad = 1))
        }
        _uiState.value = CarritoUiState(items = itemsActuales)
    }

    fun incrementarCantidad(codigoProducto: String) {
        val itemsActuales = _uiState.value.items.toMutableList()
        val indice = itemsActuales.indexOfFirst { it.producto.codigo == codigoProducto }
        if (indice >= 0) {
            val item = itemsActuales[indice]
            itemsActuales[indice] = item.copy(cantidad = item.cantidad + 1)
            _uiState.value = CarritoUiState(items = itemsActuales)
        }
    }

    fun decrementarCantidad(codigoProducto: String) {
        val itemsActuales = _uiState.value.items.toMutableList()
        val indice = itemsActuales.indexOfFirst { it.producto.codigo == codigoProducto }
        if (indice >= 0) {
            val item = itemsActuales[indice]
            if (item.cantidad > 1) {
                itemsActuales[indice] = item.copy(cantidad = item.cantidad - 1)
            } else {
                itemsActuales.removeAt(indice)
            }
            _uiState.value = CarritoUiState(items = itemsActuales)
        }
    }

    fun quitarProducto(codigoProducto: String) {
        val itemsActuales = _uiState.value.items.filterNot { it.producto.codigo == codigoProducto }
        _uiState.value = CarritoUiState(items = itemsActuales)
    }

    fun limpiarCarrito() {
        _uiState.value = CarritoUiState()
    }
}

private fun Producto.precioComoEntero(): Int {
    return precio
        .replace(".", "")
        .replace(",", "")
        .toIntOrNull() ?: 0
}

private fun formatCurrency(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return formatter.format(value)
}
