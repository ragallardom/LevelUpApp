package cl.duoc.levelupapp.model

import androidx.annotation.DrawableRes
import cl.duoc.levelupapp.R

data class Producto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: String,
    val descripcion: String,
    @DrawableRes val imagenRes: Int
)

val productosDemo = listOf(
    Producto(
        "JM001",
        "Juegos de Mesa",
        "Catan",
        "29.990",
        "Un clásico juego de estrategia donde compites por colonizar y expandirte en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos.",
        R.drawable.jm001
    ),
    Producto(
        "JM002",
        "Juegos de Mesa",
        "Carcassonne",
        "24.990",
        "Juego de colocación de fichas donde construyes paisajes medievales alrededor de la fortaleza de Carcassonne. Fácil de aprender y entretenido para 2-5 jugadores.",
        R.drawable.jm002
    ),
    Producto(
        "AC001",
        "Accesorios",
        "Control Inalámbrico Xbox Series X",
        "59.990",
        "Control inalámbrico con botones mapeables y respuesta táctil mejorada para una experiencia cómoda. Compatible con consolas Xbox y PC.",
        R.drawable.ac001
    ),
    Producto(
        "AC002",
        "Accesorios",
        "Auriculares Gamer HyperX Cloud II",
        "79.990",
        "Auriculares con sonido envolvente de alta calidad, micrófono desmontable y almohadillas de espuma viscoelástica para sesiones prolongadas de juego.",
        R.drawable.ac002
    ),
    Producto(
        "CO001",
        "Consolas",
        "PlayStation 5",
        "549.990",
        "La consola de nueva generación de Sony con gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia completamente inmersiva.",
        R.drawable.co001
    ),
    Producto(
        "CG001",
        "Computadores Gamers",
        "ASUS ROG Strix",
        "1.299.990",
        "PC gamer de alto rendimiento equipado con los últimos componentes para cumplir con las exigencias de los jugadores más competitivos.",
        R.drawable.cg001
    ),
    Producto(
        "SG001",
        "Sillas Gamers",
        "Silla Gamer Secretlab Titan",
        "349.990",
        "Silla con diseño ergonómico y opciones de personalización para ofrecer máximo confort en sesiones de juego extensas.",
        R.drawable.sg001
    ),
    Producto(
        "MS001",
        "Mouse",
        "Logitech G502 HERO",
        "49.990",
        "Mouse gamer con sensor de alta precisión y botones programables para un control exacto y personalización al detalle.",
        R.drawable.ms001
    ),
    Producto(
        "MP001",
        "Mousepad",
        "Mousepad Razer Goliathus Extended Chroma",
        "29.990",
        "Mousepad de superficie amplia y uniforme con iluminación RGB personalizable para complementar tu setup gamer.",
        R.drawable.mp001
    ),
    Producto(
        "PP001",
        "Poleras Personalizadas",
        "Polera Gamer Personalizada 'Level-Up'",
        "14.990",
        "Polera cómoda y estilizada que puedes personalizar con tu gamer tag o diseño favorito para lucir tu estilo.",
        R.drawable.pp001
    )
)
