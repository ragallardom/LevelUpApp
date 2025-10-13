package cl.duoc.levelupapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.levelupapp.R
import cl.duoc.levelupapp.ui.theme.TechBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    vm: LoginViewModel = viewModel(),
) {
    val state by vm.ui.collectAsState()

    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) onLoginSuccess()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            vm.messageConsumed()
        }
    }

    TechBackground {
        Scaffold(
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { inner ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                val colorScheme = MaterialTheme.colorScheme

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo LevelUp",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        tonalElevation = 16.dp,
                        shadowElevation = 20.dp,
                        shape = MaterialTheme.shapes.large,
                        color = colorScheme.surface.copy(alpha = 0.88f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Inicia sesión",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        color = colorScheme.onBackground,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "Accede a tu cuenta y continúa construyendo tu setup.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp
                                    )
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.email,
                                    onValueChange = vm::onEmailChange,
                                    label = { Text("Correo electrónico") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = colorScheme.onBackground,
                                        unfocusedTextColor = colorScheme.onBackground,
                                        cursorColor = colorScheme.primary,
                                        unfocusedBorderColor = colorScheme.primary.copy(alpha = 0.5f),
                                        focusedBorderColor = colorScheme.primary,
                                        focusedLabelColor = colorScheme.primary,
                                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                                        focusedContainerColor = colorScheme.surface.copy(alpha = 0.35f),
                                        unfocusedContainerColor = colorScheme.surface.copy(alpha = 0.2f),
                                        disabledContainerColor = colorScheme.surface.copy(alpha = 0.1f)
                                    )
                                )

                                OutlinedTextField(
                                    value = state.password,
                                    onValueChange = vm::onPasswordChange,
                                    label = { Text("Contraseña") },
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = colorScheme.onBackground,
                                        unfocusedTextColor = colorScheme.onBackground,
                                        cursorColor = colorScheme.primary,
                                        unfocusedBorderColor = colorScheme.primary.copy(alpha = 0.5f),
                                        focusedBorderColor = colorScheme.primary,
                                        focusedLabelColor = colorScheme.primary,
                                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                                        focusedContainerColor = colorScheme.surface.copy(alpha = 0.35f),
                                        unfocusedContainerColor = colorScheme.surface.copy(alpha = 0.2f),
                                        disabledContainerColor = colorScheme.surface.copy(alpha = 0.1f)
                                    )
                                )

                                if (state.error != null) {
                                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                                }

                                Button(
                                    onClick = vm::submit,
                                    enabled = !state.loading,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorScheme.primary,
                                        contentColor = colorScheme.onPrimary
                                    )
                                ) {
                                    Text(if (state.loading) "Ingresando..." else "Ingresar")
                                }

                                TextButton(
                                    onClick = { vm.onRecoverPassword() },
                                    modifier = Modifier.align(Alignment.End),
                                    colors = ButtonDefaults.textButtonColors(contentColor = colorScheme.secondary)
                                ) {
                                    Text("¿Olvidaste tu contraseña?")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(56.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
