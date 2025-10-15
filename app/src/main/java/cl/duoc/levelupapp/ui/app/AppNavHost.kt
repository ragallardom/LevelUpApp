package cl.duoc.levelupapp.ui.app

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.levelupapp.ui.carrito.CarritoScreen
import cl.duoc.levelupapp.ui.carrito.CarritoViewModel
import cl.duoc.levelupapp.ui.home.HomeScreen
import cl.duoc.levelupapp.ui.login.LoginScreen
import cl.duoc.levelupapp.ui.principal.PrincipalScreen
import cl.duoc.levelupapp.ui.producto.ProductDetailScreen
import cl.duoc.levelupapp.model.productosDemo
import cl.duoc.levelupapp.ui.recover.RecuperarPasswordScreen
import cl.duoc.levelupapp.ui.register.RegistrarseScreen

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
                onProductClick = { producto ->
                    nav.navigate(Route.ProductDetail.create(producto.codigo))
                },
                carritoViewModel = carritoViewModel
            )
        }

        composable(Route.Cart.path) {
            CarritoScreen(
                onBack = { nav.popBackStack() },
                viewModel = carritoViewModel
            )
        }

        composable(
            route = Route.ProductDetail.path,
            arguments = listOf(
                navArgument(Route.ProductDetail.ARG_PRODUCT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Route.ProductDetail.ARG_PRODUCT_ID)
            val product = productosDemo.find { it.codigo == productId }
            if (product != null) {
                val suggested = productosDemo.filter {
                    it.categoria == product.categoria && it.codigo != product.codigo
                }
                ProductDetailScreen(
                    producto = product,
                    sugeridos = suggested,
                    onBack = { nav.popBackStack() },
                    onAddToCart = {
                        carritoViewModel.agregarProducto(product)
                    },
                    onSuggestedProductClick = { sugerido ->
                        nav.navigate(Route.ProductDetail.create(sugerido.codigo)) {
                            launchSingleTop = true
                        }
                    }
                )
            } else {
                nav.popBackStack()
            }
        }

        composable(Route.Register.path) {
            RegistrarseScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate(Route.Login.path) {
                        popUpTo(Route.HomeRoot.path) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Route.RecoverPassword.path) {
            RecuperarPasswordScreen(
                onBack = { nav.popBackStack() },
                onSent = {
                    nav.navigate(Route.Login.path) {
                        popUpTo(Route.HomeRoot.path) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
