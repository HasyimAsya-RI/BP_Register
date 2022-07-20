package com.example.bp1832_register

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper( var context: Context ): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    /**  Deklarasi Objek  **/
    companion object {
        private val DATABASE_NAME    = "bp1832_register"
        private val DATABASE_VERSION = 1

        // Table Name
        private val TABLE_ACCOUNT    = "account"
        // Column Account Table
        private val COLUMN_EMAIL     = "email"
        private val COLUMN_NAME      = "name"
        private val COLUMN_LEVEL     = "level"
        private val COLUMN_PASSWORD  = "password"
    }

    /**  Deklarasi SQL Query untuk Membuat dan Menghapus Tabel Account  **/
    // Create Table Account SQL Query
    private val CREATE_ACCOUNT_TABLE = (
            "CREATE TABLE " + TABLE_ACCOUNT + "("
                    + COLUMN_EMAIL    + " TEXT PRIMARY KEY, "
                    + COLUMN_NAME     + " TEXT, "
                    + COLUMN_LEVEL    + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT)"
            )

    // Drop Table Account SQL Query
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    /**  Baris Kode untuk Fungsi onCreate() dan onUpgrade()  **/
    override fun onCreate( p0: SQLiteDatabase? ) {
        p0?.execSQL( CREATE_ACCOUNT_TABLE )
    }
    override fun onUpgrade( p0: SQLiteDatabase?, p1: Int, p2: Int ) {
        p0?.execSQL( DROP_ACCOUNT_TABLE )
        onCreate( p0 )
    }


    /**  Fungsi untuk Menambahkan Akun  **/
    fun addAccount( email:String, name:String, level:String, password:String ) {
        val db     = this.writableDatabase
        val values = ContentValues()

        // Input Data Email, Nama, Level, dan Password
        values.put( COLUMN_EMAIL,    email )
        values.put( COLUMN_NAME,     name )
        values.put( COLUMN_LEVEL,    level )
        values.put( COLUMN_PASSWORD, password )

        // Menampilkan Informasi Registrasi Sukses atau Gagal
        val result = db.insert( TABLE_ACCOUNT, null, values )
        if( result == (0).toLong() ) {
            Toast.makeText( context, "Register gagal!", Toast.LENGTH_SHORT ).show()
        }
        else {
            Toast.makeText( context, "Register berhasil. " + "Silakan masuk menggunakan akun baru Anda.", Toast.LENGTH_SHORT ).show()
        }
        db.close()
    }


    /**  Fungsi untuk Mengecek Data  **/
    @SuppressLint("Range")
    fun checkData( email:String ): String {
        val colums        = arrayOf( COLUMN_NAME )
        val db            = this.readableDatabase
        val selection     = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf( email )
        var name: String  = ""

        val cursor = db.query(
            TABLE_ACCOUNT,     // Table to Query
            colums,        // Colums to Return
            selection,     // Colums for Where Clause
            selectionArgs, // The Values for Where Clause
            null,  // Group by Rows
            null,   // Filter by Row Groups
            null   // The Sort Order
        )

        if( cursor.moveToFirst() ) {
            name = cursor.getString( cursor.getColumnIndex( COLUMN_NAME ) )
        }
        cursor.close()
        return name
    }

    /**  Fungsi untuk Melakukan Validasi Login  **/
    fun checkLogin( email:String, password:String): Boolean {
        val colums        = arrayOf( COLUMN_NAME )
        val db            = this.readableDatabase

        // Selection Criteria
        val selection     = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"

        // Selection Arguments
        val selectionArgs = arrayOf( email, password )

        val cursor        = db.query(
            TABLE_ACCOUNT,     // Table to Query
            colums,        // Colums to Return
            selection,     // Colums for Where Clause
            selectionArgs, // The Values for Where Clause
            null,  // Group by Rows
            null,   // Filter by Row Groups
            null   // The Sort Order
        )

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        // Check Data Available or Not
        if( cursorCount > 0 )
            return true
        else
            return false
    }
}