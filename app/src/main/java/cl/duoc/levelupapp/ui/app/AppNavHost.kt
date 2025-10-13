package cl.duoc.levelupapp.ui.app

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.levelupapp.ui.carrito.CarritoScreen
import cl.duoc.levelupapp.ui.carrito.CarritoViewModel
import cl.duoc.levelupapp.ui.home.HomeScreen
import cl.duoc.levelupapp.ui.login.LoginScreen
import cl.duoc.levelupapp.ui.principal.PrincipalScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val activity = LocalContext.current as ComponentActivity
    val carritoViewModel: CarritoViewModel = viewModel(activity)

    NavHost(navController = nav, startDestination = Route.HomeRoot.path) {

        composable(Route.HomeRoot.path) {
            HomeScreen(
                onLoginClick = { nav.navigate(Route.Login.path) },
                onRegisterClick = { nav.navigate(Route.Register.path) },
                onRecoverClick = { nav.navigate(Route.RecoverPassword.path) }
            )
        }

        composable(Route.Login.path) {
            LoginScreen(
                onBack = { nav.popBackStack() },
                onLoginSuccess = {
                    nav.navigate(Route.Principal.path) {
                        launchSingleTop = true
                        popUpTo(Route.HomeRoot.path) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(Route.Principal.path) {
            PrincipalScreen(
                onLogout = {
                    nav.navigate(Route.HomeRoot.path) {
                        launchSingleTop = true
                        popUpTo(Route.HomeRoot.path) {
                            inclusive = true
                        }
                    }
                },
                onCartClick = { nav.navigate(Route.Cart.path) },
                carritoViewModel = carritoViewModel
            )
        }

        composable(Route.Cart.path) {
            CarritoScreen(
                onBack = { nav.popBackStack() },
                viewModel = carritoViewModel
            )
        }
    }
}
