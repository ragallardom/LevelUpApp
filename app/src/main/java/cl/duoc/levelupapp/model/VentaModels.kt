package cl.duoc.levelupapp.model

import com.google.gson.annotations.SerializedName


data class SaleRequest(
    val items: List<SaleItemRequest>
)

data class SaleItemRequest(
    val productId: Long,
    val quantity: Int
)

data class SaleResponse(
    val id: Long,
    val date: String,
    val total: Long,
    val userEmail: String,
    val items: List<SaleItemResponse>
)

data class SaleItemResponse(
    val productName: String,
    val quantity: Int,
    val price: Int,
    val subTotal: Int
)