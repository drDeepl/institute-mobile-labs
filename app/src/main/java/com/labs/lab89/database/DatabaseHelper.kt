package com.labs.lab89.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.labs.lab89.models.BookCreateModel
import com.labs.lab89.models.BookModel
import java.sql.Timestamp

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "elibrary.db"
        private  val TABLES = arrayOf("users", "genres", "books")
        private val TAG = this.javaClass.simpleName
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY," +
                "username VARCHAR(128) UNIQUE," +
                "password_hash VARCHAR(255)," +
                "role VARCHAR(255),"+
                "isAdmin BOOLEAN" +
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
                "created_at TIMESTAMP DEFAUlT CURRENT_TiMESTAMP," +
                "count_likes INTEGER DEFAULT 0," +
                "FOREIGN KEY (genre_id) REFERENCES genres(id)" +
                ")"

        val queries: Array<String> = arrayOf(createUserTableQuery,createGenresTableQuery,createBooksTable)
        queries.forEach{query -> db.execSQL(query)}


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        TABLES.forEach{tableName ->
            val dropTableQuery = "DROP TABLE IF EXISTS $tableName"
            db.execSQL(dropTableQuery)
        }
        onCreate(db)
    }

    fun addedInitGenres(){
        // TODO: ADD INIT GENRES
        val genres: List<String> = listOf("приключения", "детектив", "фэнтези", "фанфик")
        genres.forEach{genreLabel -> addToGenreTable("genre", genreLabel)}
    }

    fun addToGenreTable(columnName: String, value: String){

        Log.i(TAG, "addToGenreTable\t $value")
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

    fun getGenres(): List<String>{
        val genres: MutableList<String> = mutableListOf();
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM genres", null)
        cursor?.let {
            if(it.moveToFirst()){
                do {
                    val genreName = cursor.getString(cursor.getColumnIndex("genre"))
                    genres.add(genreName)
                    Log.i(TAG, "getGenre: $genreName")

                }
                    while(cursor.moveToNext())
            }
        }
        cursor?.close();
        return genres;
    }

    fun getGenreIdByName(genreName: String): Int{
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT id FROM genres WHERE genre = '$genreName'", null)
        var genreId: Int = -1

        cursor?.let {
            if(it.moveToFirst()){
                genreId = cursor.getInt(cursor.getColumnIndex("id"))
            }
        }
        cursor?.close();
        return  genreId;
    }

    fun addBookInTable(bookCreateModel: BookCreateModel): Boolean{
        var successPut: Boolean = false
        Log.i(TAG, "addToGenreTable")
        val contentValues = ContentValues()
        val database: SQLiteDatabase = this.writableDatabase
        contentValues.put("name", bookCreateModel.name)
        contentValues.put("author", bookCreateModel.author)
        contentValues.put("genre_id", bookCreateModel.genreId)
        contentValues.put("description", bookCreateModel.description)
        try{
            database.insertOrThrow("books", null, contentValues)
            database.close()
            successPut = true;
            Log.i(TAG, "addToBookTable: success put")
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        return successPut
    }

    fun getBooks(): ArrayList<BookModel>{
        val books: ArrayList<BookModel> = ArrayList();
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM books INNER JOIN genres ON genres.id = books.genre_id ORDER BY books.id DESC", null)
        cursor?.let {
            if(it.moveToFirst()){
                do {

                    val name:String = cursor.getString(cursor.getColumnIndex("name"))
                    val author: String = cursor.getString(cursor.getColumnIndex("author"))
                    val genre: String = cursor.getString(cursor.getColumnIndex("genre"))
                    val countLikes: Int = cursor.getInt(cursor.getColumnIndex("count_likes"))
                    val bookModel: BookModel =  BookModel(name, author, genre, countLikes)
                    books.add(bookModel)
                    Log.i(TAG, "getBooks: $author")
                }
                while(cursor.moveToNext())
            }
        }
        cursor?.close();
        return books;
    }

    fun getCountLikesBookByName(bookName: String): Int{
        Log.i(TAG, "getCountLikesBookByName")
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM books WHERE name = '$bookName'", null)
        var countLikes: Int = -1

        cursor?.let {
            if(it.moveToFirst()){
                countLikes = cursor.getInt(cursor.getColumnIndex("count_likes"))
            }
        }
        cursor?.close();
        return  countLikes;
    }

    fun setLikeForBook(bookName: String): Int{
        Log.i(TAG, "setLikeForBooks")
        val db = this.writableDatabase
        val values = ContentValues()
        var currentCountLikesBook: Int = getCountLikesBookByName(bookName)
        val newCountLikes = currentCountLikesBook + 1
        values.put("count_likes", newCountLikes)
        val selection = "name = ?"
        val selectionArgs = arrayOf(bookName)
        try{

//            val cursor: Cursor? = db.rawQuery("UPDATE books SET count_likes = ${newCountLikes} WHERE name = '$bookName'", null)
            db.update("books", values, selection, selectionArgs)

            return newCountLikes
        }
        catch (e: Exception){
            Log.e(TAG, e.message.toString())
            return currentCountLikesBook;
        }
        finally {
            db.close()
        }



    }
}
