package com.labs.lab89

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.labs.lab89.database.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val TAG = this.javaClass.simpleName
    private lateinit var testGenre: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        dbHelper = DatabaseHelper(this)
//        database = dbHelper?.writableDatabase


//        dbHelper.addToGenreTable("genre", "фэнтези")
        dbHelper.getGenres()

    }

    fun init(){
        testGenre = findViewById<TextView>(R.id.genre_text)
    }

    private fun generateButtons(btns: Array<String>){
        val distanceInDp = 20
        val layout: ConstraintLayout = findViewById(R.id.main_layout)
        btns.forEachIndexed{ index, buttonText ->
            val btn: Button = Button(this)
            btn.text = buttonText

            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            params.setMargins(0, getPxFromDp(distanceInDp) * index, 0, 0)

        }
    }


    private fun getPxFromDp(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onDestroy() {
        database?.close()
        dbHelper?.close()
        super.onDestroy()
    }
}
