package cl.duoc.levelupapp.ui.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.model.productosDemo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PrincipalUiState(
    val email: String? = "usuario@demo.com",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false
)

class PrincipalViewModel : ViewModel() {

    private val _ui = MutableStateFlow(PrincipalUiState())
    val ui: StateFlow<PrincipalUiState> = _ui.asStateFlow()

    private val fuente: List<Producto> = productosDemo

    val categorias: List<String> = listOf("Todos") + fuente.map { it.categoria }.distinct()

    private val _categoriaSel = MutableStateFlow("Todos")
    val categoriaSel: StateFlow<String> = _categoriaSel.asStateFlow()

    private val _productosFiltrados = MutableStateFlow<List<Producto>>(emptyList())
    val productosFiltrados: StateFlow<List<Producto>> = _productosFiltrados.asStateFlow()

    fun setCategoria(cat: String) {
        _categoriaSel.value = cat
        aplicarFiltro()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, error = null)
            try {
                aplicarFiltro()
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(error = e.message ?: "Error al cargar productos")
            } finally {
                _ui.value = _ui.value.copy(loading = false)
            }
        }
    }

    fun refreshHome() {
        _categoriaSel.value = "Todos"
        cargarProductos()
    }

    fun logout() {
        _ui.value = _ui.value.copy(loading = true)
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = false, loggedOut = true)
        }
    }

    private fun aplicarFiltro() {
        val cat = _categoriaSel.value
        _productosFiltrados.value = if (cat == "Todos") {
            fuente
        } else {
            fuente.filter { it.categoria == cat }
        }
    }
}
