package cl.duoc.levelupapp.model

data class SaleRequest(
    val items: List<SaleItemRequest>
)

data class SaleItemRequest(
    val productId: Long,
    val quantity: Int
)