package cl.duoc.levelupapp.ui.producto

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.ProductRequest
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.network.RetrofitClient
import cl.duoc.levelupapp.model.ApiService

import kotlinx.coroutines.launch

class ProductsViewModel(
    private val apiService: ApiService = RetrofitClient.api
) : ViewModel() {

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
                val response = apiService.getProducts() // Usa la variable del constructor

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

    fun crearProducto(
        code: String,
        name: String,
        desc: String,
        price: Int,
        stock: Int,
        category: String,
        imgBase64: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = ProductRequest(
                    codigo = code,
                    nombre = name,
                    descripcion = desc,
                    precio = price,
                    stock = stock,
                    categoria = category,
                    imageBase64 = imgBase64
                )

                val response = apiService.createProduct(request) // Usa la variable del constructor

                if (response.isSuccessful) {
                    cargarProductos()
                } else {
                    println("Error al crear: ${response.code()}")
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

    fun actualizarProducto(
        id: Long,
        code: String,
        name: String,
        desc: String,
        price: Int,
        stock: Int,
        category: String,
        imgBase64: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = ProductRequest(
                    codigo = code,
                    nombre = name,
                    descripcion = desc,
                    precio = price,
                    stock = stock,
                    categoria = category,
                    imageBase64 = imgBase64
                )

                // --- CORRECCIÓN AQUÍ ---
                // Antes usabas RetrofitClient.api, ahora usamos apiService para consistencia
                val response = apiService.updateProduct(id, request)

                if (response.isSuccessful) {
                    cargarProductos()
                } else {
                    println("Error al editar: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}