package cl.duoc.levelupapp.ui.app

sealed class Route(val path: String) {
    data object HomeRoot : Route("homeRoot")
    data object Login : Route("login")
    data object Principal : Route("principal")
    data object Cart : Route("carrito")
    data object Register : Route("registrarse")
    data object RecoverPassword : Route("recuperarContrasena")
    data object ProductDetail : Route("producto/{productoId}") {
        const val ARG_PRODUCT_ID = "productoId"
        fun create(productId: String) = "producto/$productId"
    }
}
