package cl.duoc.levelupapp.ui.carrito

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.model.productosDemo
import cl.duoc.levelupapp.repository.auth.AuthRepository
import cl.duoc.levelupapp.repository.carrito.CarritoEntity
import cl.duoc.levelupapp.repository.carrito.CarritoRepository
import cl.duoc.levelupapp.repository.carrito.SqliteCarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

class CarritoViewModel(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadPersistedItems()
        }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.addOrIncrement(producto.codigo)
            actualizarEstado { itemsActuales ->
                val mutableItems = itemsActuales.toMutableList()
                val indice = mutableItems.indexOfFirst { it.producto.codigo == producto.codigo }
                if (indice >= 0) {
                    val existente = mutableItems[indice]
                    mutableItems[indice] = existente.copy(cantidad = existente.cantidad + 1)
                } else {
                    mutableItems.add(CarritoItem(producto, cantidad = 1))
                }
                mutableItems
            }
        }
    }

    fun incrementarCantidad(codigoProducto: String) {
        val itemsActuales = _uiState.value.items
        val item = itemsActuales.firstOrNull { it.producto.codigo == codigoProducto } ?: return
        val nuevaCantidad = item.cantidad + 1
        viewModelScope.launch {
            repository.updateQuantity(codigoProducto, nuevaCantidad)
            actualizarEstado { items ->
                items.map { current ->
                    if (current.producto.codigo == codigoProducto) {
                        current.copy(cantidad = nuevaCantidad)
                    } else {
                        current
                    }
                }
            }
        }
    }

    fun decrementarCantidad(codigoProducto: String) {
        val itemsActuales = _uiState.value.items
        val item = itemsActuales.firstOrNull { it.producto.codigo == codigoProducto } ?: return
        val nuevaCantidad = item.cantidad - 1
        viewModelScope.launch {
            repository.updateQuantity(codigoProducto, nuevaCantidad)
            actualizarEstado { items ->
                if (nuevaCantidad > 0) {
                    items.map { current ->
                        if (current.producto.codigo == codigoProducto) {
                            current.copy(cantidad = nuevaCantidad)
                        } else {
                            current
                        }
                    }
                } else {
                    items.filterNot { it.producto.codigo == codigoProducto }
                }
            }
        }
    }

    fun quitarProducto(codigoProducto: String) {
        viewModelScope.launch {
            repository.removeItem(codigoProducto)
            actualizarEstado { items ->
                items.filterNot { it.producto.codigo == codigoProducto }
            }
        }
    }

    fun limpiarCarrito() {
        viewModelScope.launch {
            repository.clear()
            _uiState.value = CarritoUiState()
        }
    }

    private suspend fun loadPersistedItems() {
        val almacenados = repository.getItems()
        _uiState.value = CarritoUiState(items = mapearAUi(almacenados))
    }

    private fun mapearAUi(entities: List<CarritoEntity>): List<CarritoItem> {
        return entities.mapNotNull { entity ->
            val producto = productosDemo.find { it.codigo == entity.codigo }
            producto?.let { CarritoItem(it, entity.cantidad) }
        }
    }

    private fun actualizarEstado(transform: (List<CarritoItem>) -> List<CarritoItem>) {
        _uiState.value = CarritoUiState(items = transform(_uiState.value.items))
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            val authRepository = AuthRepository()
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
                        val repository = SqliteCarritoRepository(appContext) {
                            authRepository.currentUser()?.uid
                        }
                        return CarritoViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}

private fun Producto.precioComoEntero(): Int {
    return precio
        .replace(".", "")
        .replace(",", "")
        .toIntOrNull() ?: 0
}

private val currencyLocale: Locale = Locale.forLanguageTag("es-CL")

private fun formatCurrency(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(currencyLocale)
    return formatter.format(value)
}
