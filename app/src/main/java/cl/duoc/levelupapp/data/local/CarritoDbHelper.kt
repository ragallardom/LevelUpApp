package cl.duoc.levelupapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal object CarritoContract {
    const val DATABASE_NAME = "carrito.db"
    const val DATABASE_VERSION = 1

    object CarritoEntry {
        const val TABLE_NAME = "carrito_items"
        const val COLUMN_CODIGO = "codigo"
        const val COLUMN_CANTIDAD = "cantidad"
    }
}

class CarritoDbHelper(context: Context) :
    SQLiteOpenHelper(context, CarritoContract.DATABASE_NAME, null, CarritoContract.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${CarritoContract.CarritoEntry.TABLE_NAME} (" +
                "${CarritoContract.CarritoEntry.COLUMN_CODIGO} TEXT PRIMARY KEY," +
                "${CarritoContract.CarritoEntry.COLUMN_CANTIDAD} INTEGER NOT NULL" +
                ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${CarritoContract.CarritoEntry.TABLE_NAME}")
        onCreate(db)
    }
}
