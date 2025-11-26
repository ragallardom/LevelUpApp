package cl.duoc.levelupapp

import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.repository.carrito.CarritoEntity
import cl.duoc.levelupapp.repository.carrito.CarritoRepository
import cl.duoc.levelupapp.ui.carrito.CarritoItem
import cl.duoc.levelupapp.ui.carrito.CarritoUiState
import cl.duoc.levelupapp.ui.carrito.CarritoViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest : StringSpec({

    // Configuración de Corutinas
    val dispatcher = StandardTestDispatcher()
    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    // Mock del repositorio
    val repo = mockk<CarritoRepository>(relaxed = true)

    // Datos de prueba (CORREGIDOS para usar tus nombres en español)
    val productoPrueba = Producto(
        id = 1,
        codigo = "PS5",       // <-- Antes decía code
        nombre = "PlayStation 5", // <-- Antes decía name
        descripcion = "Consola",
        precio = 500000,
        stock = 10,
        categoria = "Consolas",
        imageBase64 = null
    )

    "El estado inicial debe estar vacío si el repo no tiene items" {
        coEvery { repo.getItems() } returns emptyList()

        val vm = CarritoViewModel(repo)
        dispatcher.scheduler.advanceUntilIdle()

        vm.uiState.value.items.size shouldBe 0
        vm.uiState.value.totalAmount shouldBe 0
    }

    "agregarProducto debe llamar al repositorio" {
        // Simulamos que el repo ya tiene 1 item guardado
        coEvery { repo.getItems() } returns listOf(CarritoEntity("PS5", 1))

        val vm = CarritoViewModel(repo)

        // Inyectamos el catálogo para que reconozca el producto
        vm.actualizarCatalogo(listOf(productoPrueba))

        vm.agregarProducto(productoPrueba)
        dispatcher.scheduler.advanceUntilIdle()

        // Verificamos que se llamó a la función de base de datos correcta
        coVerify { repo.addOrIncrement("PS5") }
    }

    "Calcular total debe multiplicar precio por cantidad correctamente" {
        // Creamos un estado manual para probar solo la matemática
        val item = CarritoItem(productoPrueba, cantidad = 2)
        val estado = CarritoUiState(items = listOf(item))

        // 500.000 * 2 = 1.000.000
        estado.totalAmount shouldBe 1000000
        estado.totalItems shouldBe 2
    }
})