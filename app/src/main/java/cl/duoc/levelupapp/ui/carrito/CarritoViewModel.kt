package cl.duoc.levelupapp.ui.carrito

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.model.SaleItemRequest
import cl.duoc.levelupapp.model.SaleRequest
import cl.duoc.levelupapp.model.SaleResponse
import cl.duoc.levelupapp.network.RetrofitClient
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
    val items: List<CarritoItem> = emptyList(),
    val isPaying: Boolean = false,
    val lastSale: SaleResponse? = null,
    val error: String? = null
) {
    val totalItems: Int
        get() = items.sumOf { it.cantidad }

    val totalAmount: Int
        // El precio ya es Int en el modelo nuevo, se multiplica directo
        get() = items.sumOf { it.producto.precio * it.cantidad }

    val formattedTotal: String
        get() = formatCurrency(totalAmount)
}

class CarritoViewModel(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    // Lista temporal para caché de productos (para resolver nombres/fotos reales de Oracle)
    private var catalogoProductos: List<Producto> = emptyList()

    init {
        viewModelScope.launch {
            loadPersistedItems()
        }
    }

    // Función para actualizar el catálogo (llamada desde PrincipalScreen al cargar API)
    fun actualizarCatalogo(productos: List<Producto>) {
        this.catalogoProductos = productos
        viewModelScope.launch { loadPersistedItems() }
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

    // --- LÓGICA DE PAGO CON RETROFIT ---
    fun pagar() {
        val itemsActuales = _uiState.value.items
        if (itemsActuales.isEmpty()) return

        viewModelScope.launch {
            // 1. Estado de carga
            _uiState.value = _uiState.value.copy(isPaying = true, error = null)

            // 2. Convertir CarritoItem -> SaleItemRequest
            val requestItems = itemsActuales.map {
                SaleItemRequest(productId = it.producto.id, quantity = it.cantidad)
            }
            val request = SaleRequest(items = requestItems)

            try {
                // 3. Llamada a la API
                val response = RetrofitClient.api.createSale(request)

                if (response.isSuccessful && response.body() != null) {
                    // 4. ÉXITO: Limpiamos BD Local y guardamos la boleta para mostrarla
                    repository.clear()
                    _uiState.value = CarritoUiState(
                        items = emptyList(),
                        lastSale = response.body(),
                        isPaying = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isPaying = false, error = "Error en el pago: ${response.code()}")
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isPaying = false, error = "Fallo de conexión: ${e.message}")
            }
        }
    }

    fun cerrarDialogoCompra() {
        _uiState.value = _uiState.value.copy(lastSale = null)
    }

    private suspend fun loadPersistedItems() {
        val almacenados = repository.getItems()
        _uiState.value = CarritoUiState(items = mapearAUi(almacenados))
    }

    private fun mapearAUi(entities: List<CarritoEntity>): List<CarritoItem> {
        return entities.mapNotNull { entity ->
            // 1. Buscamos en el catálogo descargado de Oracle
            var producto = catalogoProductos.find { it.codigo == entity.codigo }

            // 2. Fallback: Si no está en el catálogo, intentamos buscarlo en el estado actual
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


private val currencyLocale: Locale = Locale.forLanguageTag("es-CL")

private fun formatCurrency(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(currencyLocale)
    return "$" + formatter.format(value)
}