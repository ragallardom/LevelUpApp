package cl.duoc.levelupapp.model

import com.google.gson.annotations.SerializedName

data class ProductRequest(
    @SerializedName("code") val codigo: String,
    @SerializedName("name") val nombre: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("price") val precio: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("category") val categoria: String,
    @SerializedName("imageBase64") val imageBase64: String?
)