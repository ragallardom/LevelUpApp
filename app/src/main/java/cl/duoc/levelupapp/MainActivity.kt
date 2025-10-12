package cl.duoc.levelupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.levelupapp.ui.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen(
                onLoginClick = { /*TODO*/ },
                onRegisterClick = { /*TODO*/ },
                onRecoverClick = { /*TODO*/ }
            )
        }
    }
}
