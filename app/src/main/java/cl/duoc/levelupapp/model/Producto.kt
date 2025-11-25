package cl.duoc.levelupapp.model

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id")
    val id: Long = 0,

    @SerializedName("code")
    val codigo: String = "",

    @SerializedName("name")
    val nombre: String = "",

    @SerializedName("description")
    val descripcion: String? = null,

    @SerializedName("price")
    val precio: Int = 0,

    @SerializedName("category")
    val categoria: String? = null,

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("imageBase64")
    val imageBase64: String? = null
)