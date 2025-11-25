package cl.duoc.levelupapp.ui.carrito

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.Producto
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
        // CAMBIO 1: El precio ya es Int, no necesitamos convertirlo
        get() = items.sumOf { it.producto.precio * it.cantidad }

    val formattedTotal: String
        get() = formatCurrency(totalAmount)
}

class CarritoViewModel(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    // Lista temporal para caché de productos (para resolver nombres/fotos)
    private var catalogoProductos: List<Producto> = emptyList()

    init {
        viewModelScope.launch {
            loadPersistedItems()
        }
    }

    // Función para actualizar el catálogo (llamada desde PrincipalScreen)
    fun actualizarCatalogo(productos: List<Producto>) {
        this.catalogoProductos = productos
        // Recargamos el carrito para que aparezcan los datos correctos
        viewModelScope.launch { loadPersistedItems() }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.addOrIncrement(producto.codigo)
            // Actualizamos el estado localmente para reflejo inmediato
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
        // CAMBIO 2: Usamos el catálogo dinámico, no productosDemo
        // Si el catálogo está vacío (al inicio), intentamos buscar en los items actuales para no perder info visual
        return entities.mapNotNull { entity ->
            // Buscamos en el catálogo descargado de Oracle
            var producto = catalogoProductos.find { it.codigo == entity.codigo }

            // Fallback: Si no está en el catálogo, intentamos buscarlo en el estado actual
            // (por si ya lo habíamos agregado antes)
            if (producto == null) {
                producto = _uiState.value.items.find { it.producto.codigo == entity.codigo }?.producto
            }

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

// CAMBIO 3: Eliminamos la función "precioComoEntero" porque ya no sirve.

private val currencyLocale: Locale = Locale("es", "CL")

private fun formatCurrency(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(currencyLocale)
    return "$" + formatter.format(value)
}