package cl.duoc.levelupapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.ui.home.components.AnimatedLogo

private val BrandShadow = Color(0xFF000000)
private val BrandMidnight = Color(0xFF010E1C)
private val BrandDeepBlue = Color(0xFF01142E)
private val BrandAccent = Color(0xFFA8BFCD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Level UP Gamer", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandDeepBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                )
            )
        }
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
            HomeContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick,
                onRecoverClick = onRecoverClick
            )
        }
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
        Text("¡Bienvenido!", color = Color.White)
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandAccent,
                contentColor = BrandDeepBlue
            )
        ) {
            Text("Login")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(
                onClick = onRegisterClick,
                colors = ButtonDefaults.textButtonColors(contentColor = BrandAccent)
            ) {
                Text("Registrarse")
            }
            TextButton(
                onClick = onRecoverClick,
                colors = ButtonDefaults.textButtonColors(contentColor = BrandAccent)
            ) {
                Text("Recuperar contraseña")
            }
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
