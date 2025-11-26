package cl.duoc.levelupapp.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.SaleResponse
import cl.duoc.levelupapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val _sales = MutableStateFlow<List<SaleResponse>>(emptyList())
    val sales: StateFlow<List<SaleResponse>> = _sales

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadOrders() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null // Ahora sí funciona
            try {
                val response = RetrofitClient.api.getSales()
                if (response.isSuccessful) {
                    // Invertimos la lista para ver la compra más reciente arriba
                    _sales.value = response.body()?.reversed() ?: emptyList()
                } else {
                    _error.value = "Error al cargar compras: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}