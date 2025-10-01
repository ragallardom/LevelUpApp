package cl.duoc.levelupapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelupapp.R


//@OptIn(ExperimentalMaterial3Api::class):le dices al compilador que aceptas usar APIs experimentales de Material 3 (por ejemplo, el TopAppBar que aún puede cambiar)
@OptIn(ExperimentalMaterial3Api::class)

//@Composable fun HomeScreen(): declara un composable, es decir, una función que pinta UI en Compose.
@Composable
fun HomeScreen() {

    //Crea y memoriza un NavController para esta UI.
    //remember… evita que se cree uno nuevo en cada recomposición: el controlador se mantiene estable mientras el composable vive.
    val nav = rememberNavController()

    //NavHost es el contenedor de tu grafo (mapa) de pantallas.
    //startDestination = "homeRoot": la ruta inicial al abrir HomeScreen será "homeRoot".
    //Hoy hay definidos 3 destinos: homeroot, login, principal
    NavHost(navController = nav, startDestination = "homeRoot") {

        composable("homeRoot") {
            Scaffold( //layout base con zonas comunes (aquí solo topBar).
                topBar = { TopAppBar(title = { Text("Mi App Kotlin") }) } // barra superior con el título.
            ) { inner -> //El inner que llega al lambda del Scaffold es el padding que deja el topBar; lo aplicas con .padding(inner) para que el contenido no quede bajo la barra.
                HomeContent( //composable separado con el contenido del Home.
                    modifier = Modifier
                        .padding(inner) // respeta el espacio del topBar
                        .fillMaxSize()
                        .padding(16.dp), // margen interno de 16dp
                    onLoginClick = { nav.navigate("login") },
                    onRegisterClick = { nav.navigate("registrarse") },
                    onRecoverClick = { nav.navigate("recuoerarContrasena") }
                )
            }
        }

        //Renderiza tu LoginScreen.
        composable("login") {
            LoginScreen(
                onBack = { nav.popBackStack() }, //onBack: hace popBackStack(), o sea, vuelve a la pantalla anterior en el stack (en este caso, homeRoot).
                onLoginSuccess = {
                    nav.navigate("principal") {
                        // deja homeRoot en el back stack para futuro, si quieres limpiar:
                        // popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }

        //Renderiza PrincipalScreen
        composable("principal") {
            PrincipalScreen(
                onLogout = {
                    // cierra sesión y vuelve al homeRoot limpiando el back stack
                    nav.navigate("homeRoot") {
                        //popUpTo("homeRoot") { inclusive = true }
                    }
                }
            )
        }

        //Renderiza Registrarse
        composable ("registrarse") {
            RegistrarseScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate("login") {
                        popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }

        //Renderiza Recuperar contraseña
        composable("recuoerarContrasena") {
            RecuperarPasswordScreen(
                onBack = { nav.popBackStack() },
                onSent = {
                    nav.navigate("login") {
                        popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier, // modifier: permite a quien use HomeContent decidir tamaños/márgenes desde afuera (buena práctica en Compose).
    onLoginClick: () -> Unit, //onLoginClick: el evento del botón se inyecta desde arriba (desacopla UI de navegación).
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(), //// ocupa toda la pantalla
        verticalArrangement = Arrangement.spacedBy(20.dp), // espaciado y alineación vertical
        horizontalAlignment = Alignment.CenterHorizontally // centra los hijos horizontalmente
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo), //carga el drawable logo
            contentDescription = "Logo App",
            modifier = Modifier
                .fillMaxWidth() //todo el ancho
                .height(150.dp), //y 150dp de alto
            contentScale = ContentScale.Fit //ajusta la imagen sin recortarla para que quepa
        )
        Text("¡Bienvenido!")
        Button(onClick = onLoginClick) {
            Text("Login")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onRegisterClick) { Text("Registrarse") }
            TextButton(onClick = onRecoverClick) { Text("Recuperar contraseña") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
