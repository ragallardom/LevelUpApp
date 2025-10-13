package cl.duoc.levelupapp.model

data class Producto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: String
)

val productosDemo = listOf(
    Producto("JM001", "Juegos de Mesa", "Catan", "29.990"),
    Producto("JM002", "Juegos de Mesa", "Carcassonne", "24.990"),
    Producto("AC001", "Accesorios", "Control Inalámbrico Xbox Series X", "59.990"),
    Producto("AC002", "Accesorios", "Auriculares Gamer HyperX Cloud II", "79.990"),
    Producto("CO001", "Consolas", "PlayStation 5", "549.990"),
    Producto("CG001", "Computadores Gamers", "ASUS ROG Strix", "1.299.990"),
    Producto("SG001", "Sillas Gamers", "Silla Gamer Secretlab Titan", "349.990"),
    Producto("MS001", "Mouse", "Logitech G502 HERO", "49.990"),
    Producto("MP001", "Mousepad", "Mousepad Razer Goliathus Extended Chroma", "29.990"),
    Producto("PP001", "Poleras Personalizadas", "Polera Gamer Personalizada 'Level-Up'", "14.990")
)
