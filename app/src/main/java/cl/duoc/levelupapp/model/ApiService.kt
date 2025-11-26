package cl.duoc.levelupapp.model

import cl.duoc.levelupapp.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("api/v1/products")
    suspend fun getProducts(): Response<List<Producto>>

    @POST("api/v1/users/sync")
    suspend fun syncUser(): Response<String>

    @POST("api/v1/sales")
    suspend fun createSale(@Body request: SaleRequest): Response<SaleResponse>

    @GET("api/v1/sales")
    suspend fun getSales(): Response<List<SaleResponse>>

    @GET("api/v1/users/me")
    suspend fun getMyProfile(): Response<UserResponse>

    @POST("api/v1/products")
    suspend fun createProduct(@Body request: ProductRequest): Response<Producto>

    @PUT("api/v1/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body request: ProductRequest
    ): Response<Producto>

}