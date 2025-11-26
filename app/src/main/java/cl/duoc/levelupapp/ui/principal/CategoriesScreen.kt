package cl.duoc.levelupapp.ui.principal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.ui.theme.BrandColors

private val BrandAccent = BrandColors.Accent
private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandSurface = BrandColors.SurfaceElevated

@Composable
fun CategoriesScreen(
    categorias: List<String>,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Explorar Categorías",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BrandAccent
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(categorias.filter { it != "Todos" }) { categoria ->
                CategoryCardLarge(
                    categoria = categoria,
                    onClick = { onCategoryClick(categoria) }
                )
            }
        }
    }
}

@Composable
fun CategoryCardLarge(
    categoria: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BrandDeepBlue.copy(alpha = 0.6f),
                            BrandDeepBlue.copy(alpha = 0.9f)
                        )
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = BrandAccent.copy(alpha = 0.1f),
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 10.dp, y = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = categoria,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = BrandAccent,
                    modifier = Modifier.size(20.dp).align(Alignment.End)
                )
            }
        }
    }
}