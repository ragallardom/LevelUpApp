package cl.duoc.levelupapp.model

import cl.duoc.levelupapp.R

data class Producto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: String,
    val imagenUrl: String
)

val productosDemo = listOf(
    Producto(
        "JM001",
        "Juegos de Mesa",
        "Catan",
        "29.990",
        "https://images.unsplash.com/photo-1521737604893-d14cc237f11d"
    ),
    Producto(
        "JM002",
        "Juegos de Mesa",
        "Carcassonne",
        "24.990",
        "https://images.unsplash.com/photo-1610890716563-4979b21aa22c"
    ),
    Producto(
        "AC001",
        "Accesorios",
        "Control Inalámbrico Xbox Series X",
        "59.990",
        "https://images.unsplash.com/photo-1618005198919-d3d4b5a92eee"
    ),
    Producto(
        "AC002",
        "Accesorios",
        "Auriculares Gamer HyperX Cloud II",
        "79.990",
        "https://images.unsplash.com/photo-1512496951746-71c93eaa2d37"
    ),
    Producto(
        "CO001",
        "Consolas",
        "PlayStation 5",
        "549.990",
        "https://images.unsplash.com/photo-1606813902914-8b94d0df0f3c"
    ),
    Producto(
        "CG001",
        "Computadores Gamers",
        "ASUS ROG Strix",
        "1.299.990",
        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"
    ),
    Producto(
        "SG001",
        "Sillas Gamers",
        "Silla Gamer Secretlab Titan",
        "349.990",
        "https://images.unsplash.com/photo-1587613990891-041a5c3a93a7"
    ),
    Producto(
        "MS001",
        "Mouse",
        "Logitech G502 HERO",
        "49.990",
        "https://images.unsplash.com/photo-1587202372775-98927f85fc1b"
    ),
    Producto(
        "MP001",
        "Mousepad",
        "Mousepad Razer Goliathus Extended Chroma",
        "29.990",
        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg"
    ),
    Producto(
        "PP001",
        "Poleras Personalizadas",
        "Polera Gamer Personalizada 'Level-Up'",
        "14.990",
        "https://images.unsplash.com/photo-1512436991641-6745cdb1723f"
    )
)
