package cl.duoc.levelupapp.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.User
import cl.duoc.levelupapp.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loggedIn: Boolean = false,
    val user: User? = null,
    val message: String? = null   // para Snackbar one-shot (opcional)
)

class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(v: String)    = _ui.update { it.copy(email = v, emailError = null, error = null, message = null) }
    fun onPasswordChange(v: String) = _ui.update { it.copy(password = v, passwordError = null, error = null, message = null) }

    private fun validar(): Pair<String?, String?> {
        val s = _ui.value
        val emailError = if (!Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) "Email inválido" else null
        val passwordError = if (s.password.length < 6) "La clave debe tener al menos 6 caracteres" else null
        return emailError to passwordError
    }

    fun submit() {
        val (emailError, passwordError) = validar()
        if (emailError != null || passwordError != null) {
            _ui.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }
        viewModelScope.launch {
            _ui.update {
                it.copy(
                    loading = true,
                    error = null,
                    message = null,
                    emailError = null,
                    passwordError = null
                )
            }
            val user = repo.login(_ui.value.email, _ui.value.password)
            _ui.update {
                if (user != null) it.copy(loading = false, loggedIn = true, user = user, message = "Ingreso exitoso")
                else it.copy(loading = false, error = "Error al iniciar sesión")
            }
        }
    }

    fun messageConsumed() { _ui.update { it.copy(message = null) } }
}
