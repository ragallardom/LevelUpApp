package cl.duoc.levelupapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.ui.home.components.AnimatedLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Level UP Gamer") }) }
    ) { inner ->
        HomeContent(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick,
            onRecoverClick = onRecoverClick
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo App",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Fit
        )*/
        AnimatedLogo(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            // playing = ui.desdeVM, si tienes VM; por ahora puede quedar por defecto
        )
        Text("¡Bienvenido!")
        Button(onClick = onLoginClick) { Text("Login") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onRegisterClick) { Text("Registrarse") }
            TextButton(onClick = onRecoverClick) { Text("Recuperar contraseña") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onLoginClick = {},
        onRegisterClick = {},
        onRecoverClick = {}
    )
}
