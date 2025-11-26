package cl.duoc.levelupapp


import cl.duoc.levelupapp.model.ApiService
import cl.duoc.levelupapp.model.ProductRequest
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.producto.ProductsViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()
    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    // Mock de la API
    val mockApi = mockk<ApiService>()

    "crearProducto debe llamar a la API correctamente" {
        // 1. Preparamos los datos
        val requestEsperada = ProductRequest(
            codigo = "TEST-1",
            nombre = "Prueba",
            descripcion= "Desc",
            precio = 100,
            stock = 5,
            categoria = "Test",
            imageBase64 = null
        )

        // 2. Simulamos que la API responde éxito (200 OK)
        coEvery { mockApi.createProduct(any()) } returns Response.success(
            Producto(1, "TEST-1", "Prueba", "Desc", 100, "Test", 5)
        )

        // Simulamos también la recarga de productos
        coEvery { mockApi.getProducts() } returns Response.success(emptyList())

        // 3. Iniciamos el ViewModel con la API falsa
        val vm = ProductsViewModel(apiService = mockApi)

        // 4. Ejecutamos la acción
        vm.crearProducto("TEST-1", "Prueba", "Desc", 100, 5, "Test", null)
        dispatcher.scheduler.advanceUntilIdle()

        // 5. Verificamos que se llamó a la API con los datos correctos
        coVerify {
            mockApi.createProduct(match {
                it.codigo == "TEST-1" && it.precio == 100
            })
        }
    }

    "cargarProductos debe actualizar la lista si la API responde éxito" {
        // Datos simulados que vienen de Oracle
        val listaSimulada = listOf(
            Producto(1, "P1", "Prod 1", "D1", 100, "C1", 10),
            Producto(2, "P2", "Prod 2", "D2", 200, "C2", 20)
        )

        coEvery { mockApi.getProducts() } returns Response.success(listaSimulada)

        val vm = ProductsViewModel(apiService = mockApi)
        vm.cargarProductos()
        dispatcher.scheduler.advanceUntilIdle()

        // Verificamos que el estado del ViewModel tenga 2 productos
        vm.productos.value.size shouldBe 2
        vm.productos.value[0].nombre shouldBe "Prod 1"
    }
})