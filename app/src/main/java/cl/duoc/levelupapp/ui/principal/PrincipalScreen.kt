package cl.duoc.levelupapp.ui.principal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.levelupapp.ui.theme.LevelUppAppTheme
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

private val BrandShadow = Color(0xFF000000)
private val BrandMidnight = Color(0xFF010E1C)
private val BrandDeepBlue = Color(0xFF01142E)
private val BrandAccent = Color(0xFFA8BFCD)

private val categories = listOf(
    "Juegos de Mesa",
    "Accesorios",
    "Consolas",
    "Computadores Gamers",
    "Sillas Gamers",
    "Mouse",
    "Mousepad",
    "Poleras Personalizadas"
)

private val products = listOf(
    Product("JM001", "Juegos de Mesa", "Juegos de Mesa Catan", "29.990"),
    Product("JM002", "Juegos de Mesa", "Juegos de Mesa Carcassonne", "24.990"),
    Product("AC001", "Accesorios", "Controlador Inalámbrico Xbox Series X", "59.990"),
    Product("AC002", "Accesorios", "Auriculares Gamer HyperX Cloud II", "79.990"),
    Product("CO001", "Consolas", "Consolas PlayStation 5", "549.990"),
    Product("CG001", "Computadores Gamers", "Computadores Gamers PC Gamer ASUS ROG Strix", "1.299.990"),
    Product("SG001", "Sillas Gamers", "Sillas Gamers Silla Gamer Secretlab Titan", "349.990"),
    Product("MS001", "Mouse", "Mouse Gamer Logitech G502 HERO", "49.990"),
    Product("MP001", "Mousepad", "Mousepad Razer Goliathus Extended Chroma", "29.990"),
    Product("PP001", "Poleras Personalizadas", "Polera Gamer Personalizada 'Level-Up'", "14.990")
)

private val carouselItems = listOf(
    "Promociones Exclusivas",
    "Novedades Gaming",
    "Tu Setup Ideal"
)

data class Product(
    val code: String,
    val category: String,
    val name: String,
    val price: String
)

enum class BottomOption { HOME, ACCOUNT, CATEGORIES, ORDERS, LOGOUT }

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val address: String) : LocationUiState
    data class Error(val message: String) : LocationUiState
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PrincipalScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(BottomOption.HOME) }
    var locationState by remember { mutableStateOf<LocationUiState>(LocationUiState.Idle) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (granted) {
            scope.launch {
                requestAddress(onStateChange = { locationState = it }, context = context)
            }
        } else {
            locationState = LocationUiState.Error("Permiso de ubicación denegado")
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && locationState !is LocationUiState.Success) {
            requestAddress(onStateChange = { locationState = it }, context = context)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                color = BrandDeepBlue,
                content = {}
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = BrandDeepBlue,
                contentColor = BrandAccent
            ) {
                NavigationBarItem(
                    selected = selectedOption == BottomOption.HOME,
                    onClick = { selectedOption = BottomOption.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandDeepBlue,
                        selectedTextColor = BrandAccent,
                        unselectedIconColor = BrandAccent.copy(alpha = 0.7f),
                        unselectedTextColor = BrandAccent.copy(alpha = 0.7f),
                        indicatorColor = BrandAccent
                    )
                )
                NavigationBarItem(
                    selected = selectedOption == BottomOption.ACCOUNT,
                    onClick = { selectedOption = BottomOption.ACCOUNT },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Mi cuenta") },
                    label = { Text("Mi cuenta") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandDeepBlue,
                        selectedTextColor = BrandAccent,
                        unselectedIconColor = BrandAccent.copy(alpha = 0.7f),
                        unselectedTextColor = BrandAccent.copy(alpha = 0.7f),
                        indicatorColor = BrandAccent
                    )
                )
                NavigationBarItem(
                    selected = selectedOption == BottomOption.CATEGORIES,
                    onClick = { selectedOption = BottomOption.CATEGORIES },
                    icon = { Icon(Icons.Default.Category, contentDescription = "Categorías") },
                    label = { Text("Categorías") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandDeepBlue,
                        selectedTextColor = BrandAccent,
                        unselectedIconColor = BrandAccent.copy(alpha = 0.7f),
                        unselectedTextColor = BrandAccent.copy(alpha = 0.7f),
                        indicatorColor = BrandAccent
                    )
                )
                NavigationBarItem(
                    selected = selectedOption == BottomOption.ORDERS,
                    onClick = { selectedOption = BottomOption.ORDERS },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Compras") },
                    label = { Text("Compras") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandDeepBlue,
                        selectedTextColor = BrandAccent,
                        unselectedIconColor = BrandAccent.copy(alpha = 0.7f),
                        unselectedTextColor = BrandAccent.copy(alpha = 0.7f),
                        indicatorColor = BrandAccent
                    )
                )
                NavigationBarItem(
                    selected = selectedOption == BottomOption.LOGOUT,
                    onClick = {
                        selectedOption = BottomOption.LOGOUT
                        onLogout()
                    },
                    icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
                    label = { Text("Logout") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandDeepBlue,
                        selectedTextColor = BrandAccent,
                        unselectedIconColor = BrandAccent.copy(alpha = 0.7f),
                        unselectedTextColor = BrandAccent.copy(alpha = 0.7f),
                        indicatorColor = BrandAccent
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(BrandShadow, BrandMidnight, BrandDeepBlue)
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp, top = 24.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Buscar productos o categorías") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandAccent,
                                unfocusedBorderColor = BrandAccent.copy(alpha = 0.6f),
                                focusedTextColor = BrandAccent,
                                unfocusedTextColor = BrandAccent,
                                cursorColor = BrandAccent,
                                focusedLeadingIconColor = BrandAccent,
                                unfocusedLeadingIconColor = BrandAccent.copy(alpha = 0.8f),
                                unfocusedPlaceholderColor = BrandAccent.copy(alpha = 0.7f),
                                focusedPlaceholderColor = BrandAccent.copy(alpha = 0.7f)
                            )
                        )
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = CircleShape,
                            color = BrandAccent.copy(alpha = 0.2f)
                        ) {
                            IconButton(onClick = { /* TODO: abrir carrito */ }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito de compras",
                                    tint = BrandAccent
                                )
                            }
                        }
                    }
                }

                item {
                    LocationCard(
                        state = locationState,
                        hasPermission = hasLocationPermission,
                        onRequestPermission = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        onRefresh = {
                            if (hasLocationPermission) {
                                scope.launch {
                                    requestAddress(onStateChange = { locationState = it }, context = context)
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    )
                }

                item {
                    CarouselSection()
                }

                item {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = BrandAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                text = category,
                                onClick = { /* TODO: navegar a categoría */ }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = BrandAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                items(products, key = { it.code }) { product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun CarouselSection() {
    val pagerState = rememberPagerState(pageCount = { carouselItems.size })

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            HorizontalPager(state = pagerState) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandAccent.copy(alpha = 0.2f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = carouselItems[page],
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = BrandAccent,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(carouselItems.size) { index ->
                val indicatorColor = if (pagerState.currentPage == index) BrandAccent else BrandAccent.copy(alpha = 0.4f)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(width = 24.dp, height = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(indicatorColor)
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        },
        shape = RoundedCornerShape(24.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = BrandAccent.copy(alpha = 0.15f),
            labelColor = BrandAccent
        ),
        border = AssistChipDefaults.assistChipBorder(
            borderColor = BrandAccent.copy(alpha = 0.4f),
            borderWidth = 1.dp
        )
    )
}

@Composable
private fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BrandDeepBlue.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = BrandAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "${product.category} • ${product.code}",
                style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent.copy(alpha = 0.8f))
            )
            Text(
                text = "$${product.price} CLP",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = BrandAccent,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun LocationCard(
    state: LocationUiState,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        colors = CardDefaults.cardColors(containerColor = BrandDeepBlue.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Dirección de entrega",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = BrandAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )
            when (state) {
                LocationUiState.Idle -> {
                    Text(
                        text = "Comparte tu ubicación para obtener tu dirección.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent.copy(alpha = 0.8f))
                    )
                }
                LocationUiState.Loading -> {
                    Text(
                        text = "Obteniendo dirección...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent.copy(alpha = 0.8f))
                    )
                }
                is LocationUiState.Success -> {
                    Text(
                        text = state.address,
                        style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent)
                    )
                }
                is LocationUiState.Error -> {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!hasPermission) {
                    TextButton(onClick = onRequestPermission) {
                        Text("Permitir ubicación")
                    }
                }
                TextButton(onClick = onRefresh) {
                    Text("Actualizar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PrincipalScreenPreview() {
    LevelUppAppTheme {
        PrincipalScreen(onLogout = {})
    }
}

private suspend fun requestAddress(
    onStateChange: (LocationUiState) -> Unit,
    context: Context
) {
    onStateChange(LocationUiState.Loading)
    val address = fetchAddress(context)
    if (address != null) {
        onStateChange(LocationUiState.Success(address))
    } else {
        onStateChange(LocationUiState.Error("No se pudo obtener la dirección"))
    }
}

@SuppressLint("MissingPermission")
private suspend fun fetchAddress(context: Context): String? {
    return withContext(Dispatchers.IO) {
        val location = obtainLocation(context) ?: return@withContext null
        resolveAddress(context, location)
    }
}

@SuppressLint("MissingPermission")
private suspend fun obtainLocation(context: Context): Location? {
    val provider = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { continuation ->
        provider.lastLocation
            .addOnSuccessListener { location ->
                if (continuation.isActive) {
                    continuation.resume(location)
                }
            }
            .addOnFailureListener {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
    }
}

private suspend fun resolveAddress(context: Context, location: Location): String? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (continuation.isActive) {
                                continuation.resume(addresses.firstOrNull()?.getAddressLine(0))
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        }
                    })
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0)
            }
        } catch (_: IOException) {
            null
        }
    }
}
