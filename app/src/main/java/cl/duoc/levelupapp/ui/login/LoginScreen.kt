package cl.duoc.levelupapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.levelupapp.R
import androidx.compose.material3.TextFieldDefaults

private val BrandShadow = Color(0xFF000000)
private val BrandMidnight = Color(0xFF010E1C)
private val BrandDeepBlue = Color(0xFF01142E)
private val BrandAccent = Color(0xFFA8BFCD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    vm: LoginViewModel = viewModel()
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

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp),
                color = BrandDeepBlue
            ) {}
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BrandShadow, BrandMidnight, BrandDeepBlue)
                    )
                )
                .padding(inner)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(180.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo LevelUp",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Inicia sesión",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = BrandAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Ingresa tus credenciales para continuar",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = BrandAccent.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = vm::onEmailChange,
                            label = { Text("Correo electrónico") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = BrandAccent,
                                unfocusedTextColor = BrandAccent,
                                cursorColor = BrandAccent,
                                unfocusedBorderColor = BrandAccent.copy(alpha = 0.5f),
                                unfocusedLabelColor = BrandAccent.copy(alpha = 0.7f),
                                focusedBorderColor = BrandAccent,
                                focusedLabelColor = BrandAccent
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
                                focusedTextColor = BrandAccent,
                                unfocusedTextColor = BrandAccent,
                                cursorColor = BrandAccent,
                                unfocusedBorderColor = BrandAccent.copy(alpha = 0.5f),
                                unfocusedLabelColor = BrandAccent.copy(alpha = 0.7f),
                                focusedBorderColor = BrandAccent,
                                focusedLabelColor = BrandAccent
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
                                containerColor = BrandAccent,
                                contentColor = BrandDeepBlue
                            )
                        ) {
                            Text(if (state.loading) "Ingresando..." else "Ingresar")
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 32.dp, start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandAccent
                )
            }

            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = BrandAccent
                )
            }
        }
    }
}
