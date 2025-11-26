package cl.duoc.levelupapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

import cl.duoc.levelupapp.ui.home.HomeScreen

class HomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun elementos_principales_deben_ser_visibles() {
        composeRule.setContent {
            HomeScreen(
                onLoginClick = {},
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        composeRule.onNodeWithText("Login").assertIsDisplayed()
        composeRule.onNodeWithText("Registrarse").assertIsDisplayed()
        composeRule.onNodeWithText("Recuperar contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("© 2025 LevelUp. Todos los derechos reservados.").assertIsDisplayed()
    }

    @Test
    fun click_en_login_debe_ejecutar_callback() {
        var loginClicked = false

        composeRule.setContent {
            HomeScreen(
                onLoginClick = { loginClicked = true },
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        // Simula el clic
        composeRule.onNodeWithText("Login").performClick()

        // Verifica que la variable cambió a true
        Assert.assertTrue("El botón de Login no ejecutó la acción", loginClicked)
    }

    @Test
    fun click_en_registrarse_debe_ejecutar_callback() {
        var registerClicked = false

        composeRule.setContent {
            HomeScreen(
                onLoginClick = {},
                onRegisterClick = { registerClicked = true },
                onRecoverClick = {}
            )
        }

        composeRule.onNodeWithText("Registrarse").performClick()
        Assert.assertTrue("El botón Registrarse no ejecutó la acción", registerClicked)
    }
}