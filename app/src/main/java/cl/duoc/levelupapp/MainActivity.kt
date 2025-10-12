package cl.duoc.levelupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.levelupapp.ui.app.AppNavHost
import cl.duoc.levelupapp.ui.theme.LevelUppAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUppAppTheme {
                AppNavHost()
            }
        }
    }
}
