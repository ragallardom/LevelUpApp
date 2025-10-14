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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.annotation.DrawableRes
import cl.duoc.levelupapp.R
import cl.duoc.levelupapp.ui.carrito.CarritoViewModel
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.theme.LevelUppAppTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.io.IOException
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

private val BrandShadow = Color(0xFF000000)
private val BrandMidnight = Color(0xFF010E1C)
private val BrandDeepBlue = Color(0xFF01142E)
private val BrandAccent = Color(0xFFA8BFCD)

data class CarouselItem(
    @DrawableRes val imageRes: Int,
    val contentDescription: String
)

private val carouselItems = listOf(
    CarouselItem(R.drawable.promociones_exclusivas, "Promociones Exclusivas"),
    CarouselItem(R.drawable.novedades_gaming, "Novedades Gaming"),
    CarouselItem(R.drawable.setup_ideal, "Arma tu setup ideal")
)

enum class BottomOption { HOME, ACCOUNT, CATEGORIES, ORDERS, LOGOUT }

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val address: String) : LocationUiState
    data class Error(val message: String) : LocationUiState
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PrincipalScreen(
    onLogout: () -> Unit,
    onCartClick: () -> Unit = {},
    onProductClick: (Producto) -> Unit = {},
    viewModel: PrincipalViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
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

    val uiState by viewModel.ui.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSel.collectAsState()
    val productos by viewModel.productosFiltrados.collectAsState()
    val scope = rememberCoroutineScope()
    val carritoUiState by carritoViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) {
            onLogout()
        }
    }

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

    val displayedProducts = if (searchQuery.isBlank()) {
        productos
    } else {
        val query = searchQuery.trim().lowercase(Locale.getDefault())
        productos.filter { producto ->
            producto.nombre.lowercase(Locale.getDefault()).contains(query) ||
                producto.categoria.lowercase(Locale.getDefault()).contains(query) ||
                producto.codigo.lowercase(Locale.getDefault()).contains(query)
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
                    onClick = {
                        selectedOption = BottomOption.HOME
                        viewModel.refreshHome()
                    },
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
                        viewModel.logout()
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout") },
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
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    color = BrandAccent.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (carritoUiState.totalItems > 0) {
                                        Badge { Text(text = carritoUiState.totalItems.toString()) }
                                    }
                                }
                            ) {
                                IconButton(onClick = onCartClick) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Carrito de compras",
                                        tint = BrandAccent
                                    )
                                }
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
                        items(viewModel.categorias) { category ->
                            CategoryChip(
                                text = category,
                                selected = category == categoriaSeleccionada,
                                onClick = { viewModel.setCategoria(category) }
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

                if (uiState.loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BrandAccent)
                        }
                    }
                }

                uiState.error?.let { error ->
                    item {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (!uiState.loading && displayedProducts.isEmpty()) {
                    item {
                        Text(
                            text = "No se encontraron productos",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = BrandAccent.copy(alpha = 0.8f)
                            )
                        )
                    }
                } else {
                    items(displayedProducts, key = { it.codigo }) { product ->
                        ProductCard(
                            producto = product,
                            onAddToCart = { carritoViewModel.agregarProducto(product) },
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarouselSection() {
    val pagerState = rememberPagerState(pageCount = { carouselItems.size })
    val coroutineScope = rememberCoroutineScope()

    if (carouselItems.isEmpty()) {
        return
    }

    LaunchedEffect(pagerState) {
        while (true) {
            delay(5_000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            HorizontalPager(state = pagerState) { page ->
                val item = carouselItems[page]
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.contentDescription,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, BrandDeepBlue.copy(alpha = 0.7f))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = item.contentDescription,
                                style = MaterialTheme.typography.titleMedium.copy(
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
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
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
            containerColor = if (selected) BrandAccent.copy(alpha = 0.3f) else BrandAccent.copy(alpha = 0.15f),
            labelColor = BrandAccent
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) BrandAccent else BrandAccent.copy(alpha = 0.4f)
        )
    )
}

@Composable
private fun ProductCard(
    producto: Producto,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = BrandDeepBlue.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "${producto.categoria} • ${producto.codigo}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = BrandAccent.copy(alpha = 0.8f))
                )
                Text(
                    text = "$${producto.precio} CLP",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = BrandAccent,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            FilledTonalButton(onClick = onAddToCart) {
                Text(text = "Agregar")
            }
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
                text = "Tu ubicación",
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
        PrincipalScreen(
            onLogout = {},
            onCartClick = {},
            onProductClick = {},
            viewModel = PrincipalViewModel(),
            carritoViewModel = CarritoViewModel()
        )
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

    val lastKnown = suspendCancellableCoroutine { continuation: kotlinx.coroutines.CancellableContinuation<Location?> ->
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

    if (lastKnown != null) {
        return lastKnown
    }

    val cancellationTokenSource = CancellationTokenSource()

    return suspendCancellableCoroutine { continuation: kotlinx.coroutines.CancellableContinuation<Location?> ->
        continuation.invokeOnCancellation { cancellationTokenSource.cancel() }
        provider
            .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token)
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
