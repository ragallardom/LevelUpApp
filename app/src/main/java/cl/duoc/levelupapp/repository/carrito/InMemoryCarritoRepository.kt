package cl.duoc.levelupapp.repository.carrito

class InMemoryCarritoRepository(
    initialItems: List<CarritoEntity> = emptyList()
) : CarritoRepository {

    private val items = initialItems.associateBy { it.codigo }.toMutableMap()

    override suspend fun getItems(): List<CarritoEntity> = items.values.map { it.copy() }

    override suspend fun addOrIncrement(codigo: String) {
        val current = items[codigo]?.cantidad ?: 0
        items[codigo] = CarritoEntity(codigo, current + 1)
    }

    override suspend fun updateQuantity(codigo: String, cantidad: Int) {
        if (cantidad > 0) {
            items[codigo] = CarritoEntity(codigo, cantidad)
        } else {
            items.remove(codigo)
        }
    }

    override suspend fun removeItem(codigo: String) {
        items.remove(codigo)
    }

    override suspend fun clear() {
        items.clear()
    }
}
