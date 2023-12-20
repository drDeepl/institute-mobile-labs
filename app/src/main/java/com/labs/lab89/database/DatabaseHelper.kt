package com.labs.lab89.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "elibrary.db"
        private  val TABLES = arrayOf("users", "genres", "books", "bills")
        private val TAG = this.javaClass.simpleName
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY," +
                "username VARCHAR(128) UNIQUE," +
                "password_hash VARCHAR(255)," +
                "role VARCHAR(255)"+
                ")"

        val createGenresTableQuery = "CREATE TABLE genres (" +
                "id INTEGER PRIMARY KEY," +
                "genre VARCHAR(128) UNIQUE"+
                ")"

        val createBooksTable: String = "CREATE TABLE books (" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR(255) UNIQUE," +
                "author VARCHAR(255)," +
                "genre_id INTEGER," +
                "description VARCHAR(255)," +
                "price REAL," +
                "FOREIGN KEY (genre_id) REFERENCES genres(id)" +
                ")"
        val createBillsQuery: String = "CREATE TABLE bills ("+
                "id INTEGER PRIMARY KEY," +
                "user_id INTEGER," +
                "book_id INTEGER," +
                "start_date TIMESTAMP," +
                "end_date TIMESTAMP," +
                "price REAL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (book_id) REFERENCES books(id)"+
                ")"

        val queries: Array<String> = arrayOf(createUserTableQuery,createGenresTableQuery,createBooksTable,createBillsQuery)
        queries.forEach{query -> db.execSQL(query)}


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        TABLES.forEach{tableName ->
            val dropTableQuery = "DROP TABLE IF EXISTS $tableName"
            db.execSQL(dropTableQuery)
        }
        onCreate(db)
    }

    fun addToGenreTable(columnName: String, value: String){
        Log.i(TAG, "addToGenreTable")
        val contentValues = ContentValues()
        val database: SQLiteDatabase = this.writableDatabase
        contentValues.put(columnName, value)
        try{
            database.insertOrThrow("genres", null, contentValues)
            database.close()
            Log.i(TAG, "addToGenreTable: success put")
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    fun getGenres(){
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM genres", null)
        cursor?.let {
            if(it.moveToFirst()){
                do {
                    val genreName = cursor.getString(cursor.getColumnIndex("genre"))
                    Log.i(TAG, "getGenre: $genreName")
                }
                    while(cursor.moveToNext())
            }
        }
    }
}
