package cl.duoc.levelupapp.ui.app

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import cl.duoc.levelupapp.ui.recover.RecuperarPasswordScreen
import cl.duoc.levelupapp.ui.register.RegistrarseScreen
import cl.duoc.levelupapp.ui.producto.ProductsViewModel

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val activity = LocalContext.current as ComponentActivity

    val carritoViewModel: CarritoViewModel = viewModel(
        activity,
        factory = CarritoViewModel.provideFactory(activity.applicationContext)
    )


    val productsViewModel: ProductsViewModel = viewModel()

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
                    productsViewModel.cargarProductos()
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
                    // Navegamos usando el código del producto real
                    nav.navigate(Route.ProductDetail.create(producto.codigo))
                },
                onAdminClick = { codigoParaEditar ->
                    if (codigoParaEditar == null) {
                        nav.navigate("admin_product")
                    } else {
                        nav.navigate("admin_product?code=$codigoParaEditar")
                    }
                },
                carritoViewModel = carritoViewModel,
                productsViewModel = productsViewModel // <--- Pasamos el VM con datos reales
            )
        }

        composable(
            route = "admin_product?code={code}",
            arguments = listOf(navArgument("code") { defaultValue = "" })
        ) { backStackEntry ->

            val productCode = backStackEntry.arguments?.getString("code")

            val productToEdit = productsViewModel.productos.value.find { it.codigo == productCode }

            cl.duoc.levelupapp.ui.admin.AdminProductScreen(
                productToEdit = productToEdit,
                onBack = { nav.popBackStack() },
                onSave = { code, name, desc, price, stock, cat, img ->

                    if (productToEdit == null) {
                        productsViewModel.crearProducto(code, name, desc, price, stock, cat, img)
                    } else {
                        productsViewModel.actualizarProducto(
                            id = productToEdit.id,
                            code = code,
                            name = name,
                            desc = desc,
                            price = price,
                            stock = stock,
                            category = cat,
                            imgBase64 = img
                        )
                    }
                    nav.popBackStack()
                }
            )
        }

        composable(Route.Cart.path) {
            CarritoScreen(
                onBack = { nav.popBackStack() },
                viewModel = carritoViewModel
            )
        }

        // --- RUTA DE DETALLE DE PRODUCTO ---
        composable(
            route = Route.ProductDetail.path,
            arguments = listOf(
                navArgument(Route.ProductDetail.ARG_PRODUCT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Route.ProductDetail.ARG_PRODUCT_ID)


            val listaReal = productsViewModel.productos.value
            val product = listaReal.find { it.codigo == productId }

            if (product != null) {
                val suggested = listaReal.filter {
                    it.categoria == product.categoria && it.codigo != product.codigo
                }.take(5) // Limitamos a 5 para que no sea infinito

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
                    },
                    isAdmin = true,
                    onEditClick = {
                        nav.navigate("admin_product?code=${product.codigo}")
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