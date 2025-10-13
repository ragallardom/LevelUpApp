package cl.duoc.levelupapp.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.levelupapp.model.User
import cl.duoc.levelupapp.repository.auth.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false,
    val user: User? = null,
    val message: String? = null   // para Snackbar one-shot (opcional)
)

class LoginViewModel(
    private val repo: AuthRepository = AuthRepository(),
    private val networkChecker: () -> Boolean = { true }
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(v: String)    = _ui.update { it.copy(email = v, error = null, message = null) }
    fun onPasswordChange(v: String) = _ui.update { it.copy(password = v, error = null, message = null) }

    private fun validar(): String? {
        val s = _ui.value
        if (!Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) return "Email inválido"
        if (s.password.length < 6) return "La clave debe tener al menos 6 caracteres"
        return null
    }

    fun submit() {
        val err = validar()
        if (err != null) {
            _ui.update { it.copy(error = err) }
            return
        }
        if (!networkChecker()) {
            _ui.update {
                it.copy(
                    error = "Sin conexión a internet. Verifica tu red e inténtalo nuevamente.",
                    loading = false
                )
            }
            return
        }
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null, message = null) }
            val result = loginWithRetry(_ui.value.email, _ui.value.password)
            result.fold(
                onSuccess = { user ->
                    _ui.update {
                        it.copy(
                            loading = false,
                            loggedIn = true,
                            user = user,
                            message = "Ingreso exitoso"
                        )
                    }
                },
                onFailure = { throwable ->
                    _ui.update {
                        it.copy(
                            loading = false,
                            error = mapFirebaseError(throwable)
                        )
                    }
                }
            )
        }
    }

    fun messageConsumed() { _ui.update { it.copy(message = null) } }

    private suspend fun loginWithRetry(email: String, password: String, maxAttempts: Int = 2): Result<User> {
        var attempt = 0
        var lastNetworkError: Throwable? = null
        while (attempt < maxAttempts) {
            val result = repo.login(email, password)
            if (result.isSuccess) {
                return result
            }
            val error = result.exceptionOrNull()
            if (error !is FirebaseNetworkException) {
                return Result.failure(error ?: IllegalStateException("Error desconocido"))
            }
            lastNetworkError = error
            attempt++
            if (attempt < maxAttempts) {
                delay(800)
            }
        }
        return Result.failure(lastNetworkError ?: FirebaseNetworkException("Error de red"))
    }

    private fun mapFirebaseError(throwable: Throwable): String = when (throwable) {
        is FirebaseNetworkException ->
            "No pudimos contactar a los servicios de autenticación. " +
                "Verifica tu conexión a internet, desactiva VPN o proxies y confirma que Google Play Services esté actualizado antes de reintentar."
        is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas. Verifica tu correo y contraseña."
        is FirebaseAuthInvalidUserException -> "No existe un usuario registrado con este correo."
        else -> throwable.localizedMessage?.takeIf { it.isNotBlank() }
            ?: "Error al iniciar sesión"
    }
}

class LoginViewModelFactory(
    private val repo: AuthRepository = AuthRepository(),
    private val networkChecker: () -> Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repo, networkChecker) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${'$'}modelClass")
    }
}
