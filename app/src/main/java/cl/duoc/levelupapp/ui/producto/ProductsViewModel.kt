package cl.duoc.levelupapp.ui.producto

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {
    private val _productos = mutableStateOf<List<Producto>>(emptyList())
    val productos: State<List<Producto>> = _productos

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    _productos.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obtenerProductoPorCodigo(codigo: String): Producto? {
        return _productos.value.find { it.codigo == codigo }
    }
}