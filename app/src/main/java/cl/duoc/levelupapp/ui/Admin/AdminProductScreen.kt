package cl.duoc.levelupapp.ui.admin // Asegúrate de que el paquete sea correcto (minúscula 'admin')

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duoc.levelupapp.model.Producto
import cl.duoc.levelupapp.ui.theme.BrandColors
import cl.duoc.levelupapp.utils.ImageUtils
import cl.duoc.levelupapp.utils.toImageBitmap

// Reutilizamos tus colores
private val BrandDeepBlue = BrandColors.DeepBlue
private val BrandAccent = BrandColors.Accent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductScreen(
    productToEdit: Producto? = null,
    onBack: () -> Unit,
    onSave: (String, String, String, Int, Int, String, String?) -> Unit
) {
    val context = LocalContext.current

    var code by remember { mutableStateOf(productToEdit?.codigo ?: "") }
    var name by remember { mutableStateOf(productToEdit?.nombre ?: "") }
    var description by remember { mutableStateOf(productToEdit?.descripcion ?: "") }
    var price by remember { mutableStateOf(productToEdit?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(productToEdit?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(productToEdit?.categoria ?: "") }


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64Image by remember { mutableStateOf(productToEdit?.imageBase64) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            // AHORA SÍ FUNCIONA: Usamos ImageUtils importado correctamente
            val b64 = ImageUtils.uriToBase64(context, uri)
            base64Image = b64
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productToEdit == null) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandDeepBlue,
                    titleContentColor = BrandAccent,
                    navigationIconContentColor = BrandAccent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- SELECTOR DE IMAGEN ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .border(1.dp, BrandAccent, RoundedCornerShape(12.dp))
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (base64Image != null) {
                    val bitmap = remember(base64Image) { base64Image?.toImageBitmap() }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = BrandAccent, modifier = Modifier.size(48.dp))
                        Text("Toca para agregar imagen", color = BrandAccent)
                    }
                }
            }

            // --- CAMPOS ---
            OutlinedTextField(
                value = code, onValueChange = { code = it },
                label = { Text("Código (SKU)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = productToEdit == null
            )

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Categoría (ej: Consolas)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = price, onValueChange = { price = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = stock, onValueChange = { stock = it },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    onSave(
                        code,
                        name,
                        description,
                        price.toIntOrNull() ?: 0,
                        stock.toIntOrNull() ?: 0,
                        category,
                        base64Image
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandAccent, contentColor = BrandDeepBlue)
            ) {
                Text(if (productToEdit == null) "Crear Producto" else "Guardar Cambios")
            }
        }
    }
}