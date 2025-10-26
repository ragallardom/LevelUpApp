package cl.duoc.levelupapp.repository.carrito

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cl.duoc.levelupapp.data.local.CarritoContract
import cl.duoc.levelupapp.data.local.CarritoDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SqliteCarritoRepository(
    context: Context,
    private val userIdProvider: () -> String?
) : CarritoRepository {

    private val dbHelper = CarritoDbHelper(context.applicationContext)

    private fun resolveUserId(): String {
        return userIdProvider().orEmpty().ifBlank { ANONYMOUS_USER_ID }
    }

    override suspend fun getItems(): List<CarritoEntity> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val userId = resolveUserId()
        val cursor = db.query(
            CarritoContract.CarritoEntry.TABLE_NAME,
            arrayOf(
                CarritoContract.CarritoEntry.COLUMN_USER_ID,
                CarritoContract.CarritoEntry.COLUMN_CODIGO,
                CarritoContract.CarritoEntry.COLUMN_CANTIDAD
            ),
            "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
        cursor.use { resultCursor ->
            val items = mutableListOf<CarritoEntity>()
            while (resultCursor.moveToNext()) {
                val codigoIndex = resultCursor
                    .getColumnIndexOrThrow(CarritoContract.CarritoEntry.COLUMN_CODIGO)
                val cantidadIndex =
                    resultCursor.getColumnIndexOrThrow(CarritoContract.CarritoEntry.COLUMN_CANTIDAD)
                items.add(
                    CarritoEntity(
                        codigo = resultCursor.getString(codigoIndex),
                        cantidad = resultCursor.getInt(cantidadIndex)
                    )
                )
            }
            items
        }
    }

    override suspend fun addOrIncrement(codigo: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val userId = resolveUserId()
        db.beginTransaction()
        try {
            val currentQuantity = queryQuantity(db, userId, codigo)
            val newQuantity = currentQuantity + 1
            val values = ContentValues().apply {
                put(CarritoContract.CarritoEntry.COLUMN_USER_ID, userId)
                put(CarritoContract.CarritoEntry.COLUMN_CODIGO, codigo)
                put(CarritoContract.CarritoEntry.COLUMN_CANTIDAD, newQuantity)
            }
            db.insertWithOnConflict(
                CarritoContract.CarritoEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override suspend fun updateQuantity(codigo: String, cantidad: Int) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val userId = resolveUserId()
        db.beginTransaction()
        try {
            if (cantidad > 0) {
                val values = ContentValues().apply {
                    put(CarritoContract.CarritoEntry.COLUMN_CANTIDAD, cantidad)
                }
                db.update(
                    CarritoContract.CarritoEntry.TABLE_NAME,
                    values,
                    "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ? AND " +
                        "${CarritoContract.CarritoEntry.COLUMN_CODIGO} = ?",
                    arrayOf(userId, codigo)
                )
            } else {
                db.delete(
                    CarritoContract.CarritoEntry.TABLE_NAME,
                    "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ? AND " +
                        "${CarritoContract.CarritoEntry.COLUMN_CODIGO} = ?",
                    arrayOf(userId, codigo)
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override suspend fun removeItem(codigo: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val userId = resolveUserId()
        db.delete(
            CarritoContract.CarritoEntry.TABLE_NAME,
            "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ? AND " +
                "${CarritoContract.CarritoEntry.COLUMN_CODIGO} = ?",
            arrayOf(userId, codigo)
        )
        Unit
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val userId = resolveUserId()
        db.delete(
            CarritoContract.CarritoEntry.TABLE_NAME,
            "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ?",
            arrayOf(userId)
        )
        Unit
    }

    private fun queryQuantity(db: SQLiteDatabase, userId: String, codigo: String): Int {
        val cursor = db.query(
            CarritoContract.CarritoEntry.TABLE_NAME,
            arrayOf(CarritoContract.CarritoEntry.COLUMN_CANTIDAD),
            "${CarritoContract.CarritoEntry.COLUMN_USER_ID} = ? AND " +
                "${CarritoContract.CarritoEntry.COLUMN_CODIGO} = ?",
            arrayOf(userId, codigo),
            null,
            null,
            null
        )
        cursor.use {
            return if (it.moveToFirst()) {
                val index = it.getColumnIndexOrThrow(CarritoContract.CarritoEntry.COLUMN_CANTIDAD)
                it.getInt(index)
            } else {
                0
            }
        }
    }

    companion object {
        private const val ANONYMOUS_USER_ID = "anonymous"
    }
}
