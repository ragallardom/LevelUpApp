package cl.duoc.levelupapp.model

import androidx.annotation.DrawableRes
import cl.duoc.levelupapp.R

data class Producto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: String,
    @DrawableRes val imagenRes: Int,
    val descripcion: String
)

val productosDemo = listOf(
    Producto(
        "JM001",
        "Juegos de Mesa",
        "Catan",
        "29.990",
        R.drawable.jm001,
        "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos."
    ),
    Producto(
        "JM002",
        "Juegos de Mesa",
        "Carcassonne",
        "24.990",
        R.drawable.jm002,
        "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de aprender."
    ),
    Producto(
        "AC001",
        "Accesorios",
        "Control Inalámbrico Xbox Series X",
        "59.990",
        R.drawable.ac001,
        "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC."
    ),
    Producto(
        "AC002",
        "Accesorios",
        "Auriculares Gamer HyperX Cloud II",
        "79.990",
        R.drawable.ac002,
        "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad durante largas sesiones de juego."
    ),
    Producto(
        "CO001",
        "Consolas",
        "PlayStation 5",
        "549.990",
        R.drawable.co001,
        "La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva."
    ),
    Producto(
        "CG001",
        "Computadores Gamers",
        "ASUS ROG Strix",
        "1.299.990",
        R.drawable.cg001,
        "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional en cualquier juego."
    ),
    Producto(
        "SG001",
        "Sillas Gamers",
        "Silla Gamer Secretlab Titan",
        "349.990",
        R.drawable.sg001,
        "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas."
    ),
    Producto(
        "MS001",
        "Mouse",
        "Logitech G502 HERO",
        "49.990",
        R.drawable.ms001,
        "Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización."
    ),
    Producto(
        "MP001",
        "Mousepad",
        "Mousepad Razer Goliathus Extended Chroma",
        "29.990",
        R.drawable.mp001,
        "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse."
    ),
    Producto(
        "PP001",
        "Poleras Personalizadas",
        "Polera Gamer Personalizada 'Level-Up'",
        "14.990",
        R.drawable.pp001,
        "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito."
    )
)
