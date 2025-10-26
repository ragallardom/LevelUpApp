package cl.duoc.levelupapp.ui.account

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AccountUiState(
    val email: String = "",
    val nombreCompleto: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val notas: String = "",
    val recibirNovedades: Boolean = true,
    val profilePhoto: Bitmap? = null,
    val message: String? = null,
    val error: String? = null
)

class AccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun setEmail(email: String?) {
        val sanitized = email.orEmpty()
        if (sanitized.isBlank()) return
        if (_uiState.value.email == sanitized) return
        _uiState.update { it.copy(email = sanitized) }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombreCompleto = value) }
    }

    fun onTelefonoChange(value: String) {
        _uiState.update { it.copy(telefono = value) }
    }

    fun onDireccionChange(value: String) {
        _uiState.update { it.copy(direccion = value) }
    }

    fun onNotasChange(value: String) {
        _uiState.update { it.copy(notas = value) }
    }

    fun onRecibirNovedadesChange(value: Boolean) {
        _uiState.update { it.copy(recibirNovedades = value) }
    }

    fun onPhotoSelected(bitmap: Bitmap) {
        _uiState.update { it.copy(profilePhoto = bitmap, error = null) }
    }

    fun saveProfile() {
        val nombre = _uiState.value.nombreCompleto.ifBlank { "Tu perfil" }
        _uiState.update {
            it.copy(
                message = "$nombre se ha actualizado correctamente",
                error = null
            )
        }
    }

    fun onMessageShown() {
        if (_uiState.value.message != null) {
            _uiState.update { it.copy(message = null) }
        }
    }

    fun onError(message: String) {
        _uiState.update { it.copy(error = message, message = null) }
    }

    fun onErrorShown() {
        if (_uiState.value.error != null) {
            _uiState.update { it.copy(error = null) }
        }
    }
}
