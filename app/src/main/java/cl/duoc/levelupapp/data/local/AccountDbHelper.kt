package cl.duoc.levelupapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal object AccountContract {
    const val DATABASE_NAME = "account.db"
    const val DATABASE_VERSION = 1

    object AccountEntry {
        const val TABLE_NAME = "account_profiles"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_DIRECCION = "direccion"
        const val COLUMN_NOTAS = "notas"
        const val COLUMN_RECIBIR_NOVEDADES = "recibir_novedades"
        const val COLUMN_PROFILE_PHOTO = "profile_photo"
    }
}

class AccountDbHelper(context: Context) :
    SQLiteOpenHelper(context, AccountContract.DATABASE_NAME, null, AccountContract.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${AccountContract.AccountEntry.TABLE_NAME} (" +
                "${AccountContract.AccountEntry.COLUMN_USER_ID} TEXT PRIMARY KEY," +
                "${AccountContract.AccountEntry.COLUMN_EMAIL} TEXT NOT NULL," +
                "${AccountContract.AccountEntry.COLUMN_NOMBRE} TEXT," +
                "${AccountContract.AccountEntry.COLUMN_TELEFONO} TEXT," +
                "${AccountContract.AccountEntry.COLUMN_DIRECCION} TEXT," +
                "${AccountContract.AccountEntry.COLUMN_NOTAS} TEXT," +
                "${AccountContract.AccountEntry.COLUMN_RECIBIR_NOVEDADES} INTEGER NOT NULL," +
                "${AccountContract.AccountEntry.COLUMN_PROFILE_PHOTO} BLOB" +
                ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${AccountContract.AccountEntry.TABLE_NAME}")
        onCreate(db)
    }
}
