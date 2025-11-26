package cl.duoc.levelupapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {

    fun uriToBase64(context: Context, uri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null

            // Redimensionar para no explotar la base de datos (Max 800px)
            val resizedBitmap = getResizedBitmap(originalBitmap, 800)

            val outputStream = ByteArrayOutputStream()
            // Comprimir a JPEG calidad 70
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArrays = outputStream.toByteArray()

            return Base64.encodeToString(byteArrays, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}

fun String.toImageBitmap(): ImageBitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}