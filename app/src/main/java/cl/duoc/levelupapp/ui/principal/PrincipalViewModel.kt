package cl.duoc.levelupapp.ui.principal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.network.RetrofitClient
import cl.duoc.levelupapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PrincipalUiState(
    val email: String? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false,
    val isAdmin: Boolean = false
)

class PrincipalViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {


    private val _ui = MutableStateFlow(PrincipalUiState())
    val ui: StateFlow<PrincipalUiState> = _ui.asStateFlow()


    private val _categoriaSel = MutableStateFlow("Todos")
    val categoriaSel: StateFlow<String> = _categoriaSel.asStateFlow()

    init {
        refreshSession()
        verificarRolUsuario()
    }

    fun setCategoria(cat: String) {
        _categoriaSel.value = cat
    }

    fun refreshHome() {
        _categoriaSel.value = "Todos"

    }

    fun logout() {
        authRepository.logout()
        _ui.value = _ui.value.copy(loading = true, email = null)
        viewModelScope.launch {
            // Simulamos un pequeño delay o proceso de limpieza si fuera necesario
            _ui.value = _ui.value.copy(loading = false, loggedOut = true)
        }
    }

    fun refreshSession() {
        val currentUser = authRepository.currentUser()
        _ui.value = _ui.value.copy(email = currentUser?.email)
    }
    private fun verificarRolUsuario() {
        viewModelScope.launch {
            try {
                Log.d("ADMIN_CHECK", "Iniciando verificación de rol...")

                val response = RetrofitClient.api.getMyProfile()

                if (response.isSuccessful) {
                    val user = response.body()
                    val role = user?.role

                    Log.d("ADMIN_CHECK", "Respuesta exitosa. Rol recibido: '$role'")

                    // Comparamos con cuidado (trim para quitar espacios invisibles)
                    val esAdmin = role?.trim() == "ADMIN"

                    Log.d("ADMIN_CHECK", "Es Admin?: $esAdmin")

                    _ui.value = _ui.value.copy(isAdmin = esAdmin)
                } else {
                    Log.e("ADMIN_CHECK", "Error en API: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ADMIN_CHECK", "Excepción al verificar rol", e)
                e.printStackTrace()
            }
        }
    }

}