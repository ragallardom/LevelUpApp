package cl.duoc.levelupapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelupapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "homeRoot") {
        @Composable("homeRoot") {
            Scaffold (
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Level Up App") }
                    )
                } ) {inner -> HomeContent(
                    modifier = Modifier.padding(inner)
                        .fillMaxSize()
                        .padding(16.dp),
                    onLoginClick = { nav.navigate("login") }
                )}


        }

        @Composable("login") {
            LoginScreen(onBack = { nav.popBackStack() },
                onLoginSuccess = { nav.navigate("principal") })
        }

        composable("principal") {
            PrincipalScreen(onLogout = {
                nav.navigate("homeRoot")
                popUpTo("homeRoot") { inclusive = true}
            })
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Level Up App") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(20.dp).fillMaxSize()) {
            Text(text = "Welcome to the Level Up App!")
            Button (onClick = { /* TODO: Handle button click */ }) {
                Text(text = "Pónele Wendy")
            }
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo App",
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun HomeContent(modifier: Modifier, onLoginClick: () -> navigate) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}