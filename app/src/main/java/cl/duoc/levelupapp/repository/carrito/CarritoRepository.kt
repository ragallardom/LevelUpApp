package cl.duoc.levelupapp.repository.carrito

data class CarritoEntity(
    val codigo: String,
    val cantidad: Int
)

interface CarritoRepository {
    suspend fun getItems(): List<CarritoEntity>
    suspend fun addOrIncrement(codigo: String)
    suspend fun updateQuantity(codigo: String, cantidad: Int)
    suspend fun removeItem(codigo: String)
    suspend fun clear()
}
