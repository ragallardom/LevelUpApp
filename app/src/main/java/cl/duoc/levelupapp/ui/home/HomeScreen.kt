package cl.duoc.levelupapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.levelupapp.ui.home.components.AnimatedLogo
import cl.duoc.levelupapp.ui.theme.LevelUppAppTheme
import cl.duoc.levelupapp.ui.theme.TechBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
) {
    TechBackground {
        Scaffold(
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                ) {}
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                HomeContent(
                    modifier = Modifier.fillMaxSize(),
                    onLoginClick = onLoginClick,
                    onRegisterClick = onRegisterClick,
                    onRecoverClick = onRecoverClick
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Text(
                text = "Potencia tu setup gamer",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 26.sp
                )
            )
            Text(
                text = "Explora hardware de última generación, periféricos y ofertas exclusivas para subir de nivel.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 12.dp,
            shadowElevation = 18.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Iniciar sesión")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onRegisterClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Crear cuenta")
                    }
                    TextButton(
                        onClick = onRecoverClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Recuperar contraseña")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LevelUppAppTheme {
        HomeScreen(
            onLoginClick = {},
            onRegisterClick = {},
            onRecoverClick = {}
        )
    }
}
