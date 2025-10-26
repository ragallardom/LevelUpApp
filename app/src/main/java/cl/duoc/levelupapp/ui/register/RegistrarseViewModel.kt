package cl.duoc.levelupapp.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.User
import cl.duoc.levelupapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val registered: Boolean = false,
    val user: User? = null,
    val message: String? = null
)

class RegisterViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui: StateFlow<RegisterUiState> = _ui

    fun onEmailChange(v: String)    = _ui.update { it.copy(email = v, emailError = null, error = null, message = null) }
    fun onPasswordChange(v: String) = _ui.update { it.copy(password = v, passwordError = null, error = null, message = null) }
    fun onConfirmChange(v: String)  = _ui.update { it.copy(confirm = v, confirmError = null, error = null, message = null) }

    private fun validar(): Triple<String?, String?, String?> {
        val s = _ui.value
        val emailError = if (!Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) "Email inválido" else null
        val passwordError = if (s.password.length < 6) "La clave debe tener al menos 6 caracteres" else null
        val confirmError = if (passwordError == null && s.password != s.confirm) "Las claves no coinciden" else null
        return Triple(emailError, passwordError, confirmError)
    }

    fun submit() {
        val (emailError, passwordError, confirmError) = validar()
        if (emailError != null || passwordError != null || confirmError != null) {
            _ui.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmError = confirmError
                )
            }
            return
        }
        viewModelScope.launch {
            _ui.update {
                it.copy(
                    loading = true,
                    error = null,
                    message = null,
                    emailError = null,
                    passwordError = null,
                    confirmError = null
                )
            }
            val user = repo.signUp(_ui.value.email, _ui.value.password)
            _ui.update {
                if (user != null) it.copy(loading = false, registered = true, user = user, message = "Cuenta creada. Inicia sesión.")
                else it.copy(loading = false, error = "No se pudo crear la cuenta")
            }
        }
    }

    fun messageConsumed() { _ui.update { it.copy(message = null) } }
}
