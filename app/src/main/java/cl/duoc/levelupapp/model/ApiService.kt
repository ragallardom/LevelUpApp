package cl.duoc.levelupapp.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("api/v1/products")
    suspend fun getProducts(): Response<List<Producto>>


    @POST("api/v1/users/sync")
    suspend fun syncUser(): Response<String>

    @POST("api/v1/sales")
    suspend fun createSale(@Body request: SaleRequest): Response<Any>
}