package cl.duoc.levelupapp.ui.account

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.repository.account.AccountEntity
import cl.duoc.levelupapp.repository.account.SqliteAccountRepository
import cl.duoc.levelupapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val repository: SqliteAccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadPersistedAccount()
        }
    }

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
        viewModelScope.launch {
            val userId = resolveUserId()
            val currentState = _uiState.value
            try {
                repository.save(
                    userId,
                    AccountEntity(
                        email = currentState.email,
                        nombreCompleto = currentState.nombreCompleto,
                        telefono = currentState.telefono,
                        direccion = currentState.direccion,
                        notas = currentState.notas,
                        recibirNovedades = currentState.recibirNovedades,
                        profilePhoto = currentState.profilePhoto
                    )
                )
                _uiState.update {
                    it.copy(
                        message = "$nombre se ha actualizado correctamente",
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "No se pudo guardar el perfil. Intenta nuevamente.",
                        message = null
                    )
                }
            }
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

    private suspend fun loadPersistedAccount() {
        val userId = resolveUserId()
        val stored = repository.findByUserId(userId) ?: return
        _uiState.update {
            it.copy(
                email = stored.email,
                nombreCompleto = stored.nombreCompleto,
                telefono = stored.telefono,
                direccion = stored.direccion,
                notas = stored.notas,
                recibirNovedades = stored.recibirNovedades,
                profilePhoto = stored.profilePhoto,
                message = null,
                error = null
            )
        }
    }

    private fun resolveUserId(): String {
        return authRepository.currentUser()?.uid.orEmpty().ifBlank { ANONYMOUS_USER_ID }
    }

    companion object {
        private const val ANONYMOUS_USER_ID = "anonymous"

        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                        return AccountViewModel(
                            authRepository = AuthRepository(),
                            repository = SqliteAccountRepository(appContext)
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
