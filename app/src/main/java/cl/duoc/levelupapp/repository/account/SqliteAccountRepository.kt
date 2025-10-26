package cl.duoc.levelupapp.repository.account

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cl.duoc.levelupapp.data.local.AccountContract
import cl.duoc.levelupapp.data.local.AccountDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

data class AccountEntity(
    val email: String,
    val nombreCompleto: String,
    val telefono: String,
    val direccion: String,
    val notas: String,
    val recibirNovedades: Boolean,
    val profilePhoto: Bitmap?
)

class SqliteAccountRepository(context: Context) {

    private val dbHelper = AccountDbHelper(context.applicationContext)

    suspend fun save(userId: String, entity: AccountEntity) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AccountContract.AccountEntry.COLUMN_USER_ID, userId)
            put(AccountContract.AccountEntry.COLUMN_EMAIL, entity.email)
            put(AccountContract.AccountEntry.COLUMN_NOMBRE, entity.nombreCompleto)
            put(AccountContract.AccountEntry.COLUMN_TELEFONO, entity.telefono)
            put(AccountContract.AccountEntry.COLUMN_DIRECCION, entity.direccion)
            put(AccountContract.AccountEntry.COLUMN_NOTAS, entity.notas)
            put(
                AccountContract.AccountEntry.COLUMN_RECIBIR_NOVEDADES,
                if (entity.recibirNovedades) 1 else 0
            )
            put(
                AccountContract.AccountEntry.COLUMN_PROFILE_PHOTO,
                entity.profilePhoto?.toByteArray()
            )
        }
        db.insertWithOnConflict(
            AccountContract.AccountEntry.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    suspend fun findByUserId(userId: String): AccountEntity? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AccountContract.AccountEntry.TABLE_NAME,
            arrayOf(
                AccountContract.AccountEntry.COLUMN_EMAIL,
                AccountContract.AccountEntry.COLUMN_NOMBRE,
                AccountContract.AccountEntry.COLUMN_TELEFONO,
                AccountContract.AccountEntry.COLUMN_DIRECCION,
                AccountContract.AccountEntry.COLUMN_NOTAS,
                AccountContract.AccountEntry.COLUMN_RECIBIR_NOVEDADES,
                AccountContract.AccountEntry.COLUMN_PROFILE_PHOTO
            ),
            "${AccountContract.AccountEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
        cursor.use { result ->
            if (!result.moveToFirst()) return@use null
            val email = result.getString(0)
            val nombre = result.getString(1) ?: ""
            val telefono = result.getString(2) ?: ""
            val direccion = result.getString(3) ?: ""
            val notas = result.getString(4) ?: ""
            val recibir = result.getInt(5) == 1
            val photoBytes = result.getBlob(6)
            AccountEntity(
                email = email,
                nombreCompleto = nombre,
                telefono = telefono,
                direccion = direccion,
                notas = notas,
                recibirNovedades = recibir,
                profilePhoto = photoBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            )
        }
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}
