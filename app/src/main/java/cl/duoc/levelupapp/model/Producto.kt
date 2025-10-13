package cl.duoc.levelupapp.model

import androidx.annotation.DrawableRes
import cl.duoc.levelupapp.R

data class Producto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: String,
    @DrawableRes val imagenRes: Int
)

val productosDemo = listOf(
    Producto(
        "JM001",
        "Juegos de Mesa",
        "Catan",
        "29.990",
        R.drawable.jm001
    ),
    Producto(
        "JM002",
        "Juegos de Mesa",
        "Carcassonne",
        "24.990",
        R.drawable.jm002
    ),
    Producto(
        "AC001",
        "Accesorios",
        "Control Inalámbrico Xbox Series X",
        "59.990",
        R.drawable.ac001
    ),
    Producto(
        "AC002",
        "Accesorios",
        "Auriculares Gamer HyperX Cloud II",
        "79.990",
        R.drawable.ac002
    ),
    Producto(
        "CO001",
        "Consolas",
        "PlayStation 5",
        "549.990",
        R.drawable.co001
    ),
    Producto(
        "CG001",
        "Computadores Gamers",
        "ASUS ROG Strix",
        "1.299.990",
        R.drawable.cg001
    ),
    Producto(
        "SG001",
        "Sillas Gamers",
        "Silla Gamer Secretlab Titan",
        "349.990",
        R.drawable.sg001
    ),
    Producto(
        "MS001",
        "Mouse",
        "Logitech G502 HERO",
        "49.990",
        R.drawable.ms001
    ),
    Producto(
        "MP001",
        "Mousepad",
        "Mousepad Razer Goliathus Extended Chroma",
        "29.990",
        R.drawable.mp001
    ),
    Producto(
        "PP001",
        "Poleras Personalizadas",
        "Polera Gamer Personalizada 'Level-Up'",
        "14.990",
        R.drawable.pp001
    )
)
