package cl.duoc.levelupapp.ui.app

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelStoreOwner
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
import cl.duoc.levelupapp.ui.principal.PrincipalViewModel
import cl.duoc.levelupapp.ui.principal.ProductDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val activity = LocalActivity.current as? ViewModelStoreOwner
        ?: error("AppNavHost must be hosted in a ComponentActivity that is a ViewModelStoreOwner")
    val carritoViewModel: CarritoViewModel = viewModel(viewModelStoreOwner = activity)
    val principalViewModel: PrincipalViewModel = viewModel(viewModelStoreOwner = activity)

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
                onProductSelected = { codigo ->
                    nav.navigate(Route.ProductDetail.create(codigo)) {
                        launchSingleTop = true
                    }
                },
                viewModel = principalViewModel,
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
                navArgument(Route.ProductDetail.ARG_PRODUCTO_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString(Route.ProductDetail.ARG_PRODUCTO_ID)
            if (productoId != null) {
                ProductDetailScreen(
                    productId = productoId,
                    viewModel = principalViewModel,
                    onBack = { nav.popBackStack() },
                    onAddToCart = { producto -> carritoViewModel.agregarProducto(producto) },
                    onNavigateToProduct = { codigo ->
                        if (codigo != productoId) {
                            nav.navigate(Route.ProductDetail.create(codigo)) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            } else {
                nav.popBackStack()
            }
        }
    }
}
